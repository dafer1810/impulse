package com.example.impulseapp;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class inventario_lista_completa extends AppCompatActivity {

    private TextInputEditText edtCodigo, edtCategoria, edtNombre, edtStock, edtPrecioCompra, edtPrecioVenta;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_lista_completa);

        db = FirebaseFirestore.getInstance();

        edtCodigo = findViewById(R.id.edtCodigoProducto);
        edtCategoria = findViewById(R.id.edtNuevaCategoria);
        edtNombre = findViewById(R.id.edtNuevoNombre);
        edtStock = findViewById(R.id.edtNuevoStock);
        edtPrecioCompra = findViewById(R.id.edtNuevoPrecioCompra);
        edtPrecioVenta = findViewById(R.id.edtNuevoPrecioVenta);

        MaterialButton btnGuardar = findViewById(R.id.btnGuardarProducto);
        MaterialButton btnCancelar = findViewById(R.id.btnCancelarProducto);

        btnGuardar.setOnClickListener(v -> guardarProducto());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void guardarProducto() {
        String codigo = edtCodigo.getText().toString().trim();
        String categoria = edtCategoria.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String stockStr = edtStock.getText().toString().trim();
        String precioCStr = edtPrecioCompra.getText().toString().trim();
        String precioVStr = edtPrecioVenta.getText().toString().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || stockStr.isEmpty()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int stock = Integer.parseInt(stockStr);
        double precioCompra = Double.parseDouble(precioCStr.isEmpty() ? "0" : precioCStr);
        double precioVenta = Double.parseDouble(precioVStr.isEmpty() ? "0" : precioVStr);

        Map<String, Object> producto = new HashMap<>();
        producto.put("codigo", codigo);
        producto.put("categoria", categoria);
        producto.put("nombre", nombre);
        producto.put("stock", stock);
        producto.put("precioCompra", precioCompra);
        producto.put("precioVenta", precioVenta);

        db.collection("inventario").document(codigo)
                .set(producto)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto guardado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show());
    }
}