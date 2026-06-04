package com.example.impulseapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class detalle_venta extends AppCompatActivity {

    private TextView txtDetalleId, txtDetalleFecha, txtDetalleTotal;
    private RecyclerView rvDetalleItems;
    private ItemFacturaAdapter adapter;
    private List<ItemFactura> listaItems;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle_venta);

        db = FirebaseFirestore.getInstance();
        txtDetalleId = findViewById(R.id.txtDetalleId);
        txtDetalleFecha = findViewById(R.id.txtDetalleFecha);
        txtDetalleTotal = findViewById(R.id.txtDetalleTotal);
        rvDetalleItems = findViewById(R.id.rvDetalleItems);

        listaItems = new ArrayList<>();
        // Reutilizamos el adaptador pero pasamos null al listener de borrar ya que es solo lectura
        adapter = new ItemFacturaAdapter(listaItems, null);
        
        rvDetalleItems.setLayoutManager(new LinearLayoutManager(this));
        rvDetalleItems.setAdapter(adapter);

        String idVenta = getIntent().getStringExtra("idVenta");
        if (idVenta != null) {
            cargarDetalle(idVenta);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarDetalle(String idVenta) {
        db.collection("ventas").document(idVenta).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Venta venta = documentSnapshot.toObject(Venta.class);
                if (venta != null) {
                    txtDetalleId.setText("Venta: #" + (venta.getId() != null ? venta.getId().substring(0, 8) : "---"));
                    txtDetalleFecha.setText("Fecha: " + venta.getFecha());
                    txtDetalleTotal.setText(String.format(Locale.getDefault(), "$%.2f", venta.getTotal()));
                    
                    if (venta.getItems() != null) {
                        listaItems.clear();
                        listaItems.addAll(venta.getItems());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al cargar detalles", Toast.LENGTH_SHORT).show();
        });
    }
}