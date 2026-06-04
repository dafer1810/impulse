package com.example.impulseapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class facturar_ventas extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteProductos;
    private TextInputEditText edtCantidadItem;
    private MaterialButton btnAddItem, btnFinalizarVenta;
    private RecyclerView rvItemsFactura;
    private TextView txtTotalFactura;

    private FirebaseFirestore db;
    private List<Producto> listaSugerencias;
    private List<ItemFactura> itemsEnFactura;
    private ItemFacturaAdapter adapter;
    private Producto productoSeleccionado;
    private double totalFactura = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_facturar_ventas);

        db = FirebaseFirestore.getInstance();
        itemsEnFactura = new ArrayList<>();
        listaSugerencias = new ArrayList<>();

        autoCompleteProductos = findViewById(R.id.autoCompleteProductos);
        edtCantidadItem = findViewById(R.id.edtCantidadItem);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnFinalizarVenta = findViewById(R.id.btnFinalizarVenta);
        rvItemsFactura = findViewById(R.id.rvItemsFactura);
        txtTotalFactura = findViewById(R.id.txtTotalFactura);

        // Configurar RecyclerView
        adapter = new ItemFacturaAdapter(itemsEnFactura, position -> {
            itemsEnFactura.remove(position);
            adapter.notifyItemRemoved(position);
            actualizarTotal();
        });
        rvItemsFactura.setLayoutManager(new LinearLayoutManager(this));
        rvItemsFactura.setAdapter(adapter);

        cargarSugerencias();

        btnAddItem.setOnClickListener(v -> agregarItemAFactura());
        btnFinalizarVenta.setOnClickListener(v -> finalizarVenta());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarSugerencias() {
        db.collection("inventario").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaSugerencias.clear();
                List<String> nombresCodigos = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Producto p = doc.toObject(Producto.class);
                    listaSugerencias.add(p);
                    nombresCodigos.add(p.getNombre() + " (" + p.getCodigo() + ")");
                }

                ArrayAdapter<String> autoAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_dropdown_item_1line, nombresCodigos);
                autoCompleteProductos.setAdapter(autoAdapter);

                autoCompleteProductos.setOnItemClickListener((parent, view, position, id) -> {
                    String seleccion = (String) parent.getItemAtPosition(position);
                    for (Producto p : listaSugerencias) {
                        if (seleccion.contains(p.getCodigo())) {
                            productoSeleccionado = p;
                            break;
                        }
                    }
                });
            }
        });
    }

    private void agregarItemAFactura() {
        if (productoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un producto de la lista", Toast.LENGTH_SHORT).show();
            return;
        }

        String cantStr = edtCantidadItem.getText().toString();
        if (cantStr.isEmpty()) return;

        int cantidad = Integer.parseInt(cantStr);
        if (cantidad > productoSeleccionado.getStock()) {
            Toast.makeText(this, "Stock insuficiente (" + productoSeleccionado.getStock() + ")", Toast.LENGTH_SHORT).show();
            return;
        }

        ItemFactura item = new ItemFactura(
                productoSeleccionado.getNombre(),
                productoSeleccionado.getCodigo(),
                cantidad,
                productoSeleccionado.getPrecioVenta()
        );

        itemsEnFactura.add(item);
        adapter.notifyItemInserted(itemsEnFactura.size() - 1);
        actualizarTotal();

        // Limpiar para el siguiente
        autoCompleteProductos.setText("");
        edtCantidadItem.setText("1");
        productoSeleccionado = null;
    }

    private void actualizarTotal() {
        totalFactura = 0;
        for (ItemFactura item : itemsEnFactura) {
            totalFactura += item.getSubtotal();
        }
        txtTotalFactura.setText(String.format(Locale.getDefault(), "$%.2f", totalFactura));
    }

    private void finalizarVenta() {
        if (itemsEnFactura.isEmpty()) {
            Toast.makeText(this, "La factura está vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        WriteBatch batch = db.batch();
        String idVenta = db.collection("ventas").document().getId();
        DocumentReference ventaRef = db.collection("ventas").document(idVenta);

        Map<String, Object> ventaData = new HashMap<>();
        ventaData.put("id", idVenta);
        ventaData.put("total", totalFactura);
        ventaData.put("fecha", new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date()));
        ventaData.put("items", itemsEnFactura);

        batch.set(ventaRef, ventaData);

        // Actualizar stock de cada producto
        for (ItemFactura item : itemsEnFactura) {
            // Buscamos el documento por código para actualizarlo
            db.collection("inventario")
                .whereEqualTo("Código", item.getCodigo())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentReference docRef = queryDocumentSnapshots.getDocuments().get(0).getReference();
                        int stockActual = queryDocumentSnapshots.getDocuments().get(0).getLong("Stock actual").intValue();
                        docRef.update("Stock actual", stockActual - item.getCantidad());
                    }
                });
        }

        batch.commit().addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Venta registrada con éxito", Toast.LENGTH_LONG).show();
            itemsEnFactura.clear();
            adapter.notifyDataSetChanged();
            actualizarTotal();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al procesar venta", Toast.LENGTH_SHORT).show();
        });
    }
}