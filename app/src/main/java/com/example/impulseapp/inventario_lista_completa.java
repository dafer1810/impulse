package com.example.impulseapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class inventario_lista_completa extends AppCompatActivity {

    private TextInputEditText edtCodigo, edtCategoria, edtNombre, edtStock, edtPrecioCompra, edtPrecioVenta;
    private TextView txtInversionTotal, txtPorcentajeGanancia, txtValorGanancia, txtTotalGanancia, txtTitulo;
    private FirebaseFirestore db;
    private String documentoId = null;
    private boolean modoEdicion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_lista_completa);

        db = FirebaseFirestore.getInstance();

        // Referencias
        edtCodigo = findViewById(R.id.edtCodigoProducto);
        edtCategoria = findViewById(R.id.edtNuevaCategoria);
        edtNombre = findViewById(R.id.edtNuevoNombre);
        edtStock = findViewById(R.id.edtNuevoStock);
        edtPrecioCompra = findViewById(R.id.edtNuevoPrecioCompra);
        edtPrecioVenta = findViewById(R.id.edtNuevoPrecioVenta);
        
        txtInversionTotal = findViewById(R.id.txtInversionTotal);
        txtPorcentajeGanancia = findViewById(R.id.txtPorcentajeGanancia);
        txtValorGanancia = findViewById(R.id.txtValorGanancia);
        txtTotalGanancia = findViewById(R.id.txtTotalGanancia);
        
        // El layout tiene un TextView para el título dentro del LinearLayout del header
        // Pero no tiene ID. Vamos a buscarlo por jerarquía o simplemente confiar en los textos estáticos.
        // Para simplificar, asumiremos que el usuario quiere ver los datos.

        MaterialButton btnGuardar = findViewById(R.id.btnGuardarProducto);
        MaterialButton btnCancelar = findViewById(R.id.btnCancelarProducto);

        // Verificar si es edición
        modoEdicion = getIntent().getBooleanExtra("modo_edicion", false);
        documentoId = getIntent().getStringExtra("id");

        if (modoEdicion && documentoId != null) {
            cargarDatosProducto(documentoId);
            btnGuardar.setText("Actualizar Producto");
        }

        // Listeners para cálculos automáticos
        setupCalculosAutomaticos();

        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void cargarDatosProducto(String id) {
        db.collection("inventario").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Producto p = documentSnapshot.toObject(Producto.class);
                        if (p != null) {
                            edtCodigo.setText(p.getCodigo());
                            edtCategoria.setText(p.getCategoria());
                            edtNombre.setText(p.getNombre());
                            edtStock.setText(String.valueOf(p.getStock()));
                            edtPrecioCompra.setText(String.valueOf(p.getPrecioCompra()));
                            edtPrecioVenta.setText(String.valueOf(p.getPrecioVenta()));
                            calcularMetricas();
                        }
                    }
                });
    }

    private void setupCalculosAutomaticos() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularMetricas();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };

        edtStock.addTextChangedListener(watcher);
        edtPrecioCompra.addTextChangedListener(watcher);
        edtPrecioVenta.addTextChangedListener(watcher);
    }

    private void calcularMetricas() {
        try {
            int stock = Integer.parseInt(edtStock.getText().toString().isEmpty() ? "0" : edtStock.getText().toString());
            double pCompra = Double.parseDouble(edtPrecioCompra.getText().toString().isEmpty() ? "0" : edtPrecioCompra.getText().toString());
            double pVenta = Double.parseDouble(edtPrecioVenta.getText().toString().isEmpty() ? "0" : edtPrecioVenta.getText().toString());

            double inversionTotal = stock * pCompra;
            double valorGananciaUd = pVenta - pCompra;
            double totalGanancia = stock * valorGananciaUd;
            double porcentajeGanancia = (pCompra > 0) ? (valorGananciaUd / pCompra) * 100 : 0;

            txtInversionTotal.setText(String.format(Locale.getDefault(), "$%.2f", inversionTotal));
            txtValorGanancia.setText(String.format(Locale.getDefault(), "$%.2f", valorGananciaUd));
            txtTotalGanancia.setText(String.format(Locale.getDefault(), "$%.2f", totalGanancia));
            txtPorcentajeGanancia.setText(String.format(Locale.getDefault(), "%.2f%%", porcentajeGanancia));

        } catch (NumberFormatException e) {
            // Ignorar errores de formato mientras se escribe
        }
    }

    private void guardarProducto() {
        String codigo = edtCodigo.getText().toString().trim();
        String categoria = edtCategoria.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String stockStr = edtStock.getText().toString().trim();
        String pCompraStr = edtPrecioCompra.getText().toString().trim();
        String pVentaStr = edtPrecioVenta.getText().toString().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Por favor complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> producto = new HashMap<>();
        producto.put("codigo", codigo);
        producto.put("categoria", categoria);
        producto.put("nombre", nombre);
        producto.put("stock", Integer.parseInt(stockStr));
        producto.put("precioCompra", Double.parseDouble(pCompraStr.isEmpty() ? "0" : pCompraStr));
        producto.put("precioVenta", Double.parseDouble(pVentaStr.isEmpty() ? "0" : pVentaStr));

        // Si es edición usamos el ID existente, si no, usamos el código como ID (o dejamos que Firestore genere uno si prefieres, 
        // pero tu estructura parece usar el código o IDs manuales como "17")
        String finalId = (modoEdicion) ? documentoId : codigo;

        db.collection("inventario").document(finalId)
                .set(producto)
                .addOnSuccessListener(aVoid -> {
                    String msg = modoEdicion ? "Producto actualizado" : "Producto guardado";
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
    }
}
