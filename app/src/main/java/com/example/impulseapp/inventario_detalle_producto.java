package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class inventario_detalle_producto extends AppCompatActivity {

    private TextView txtNombre, txtStock, txtPrecio, txtCategoria, txtCodigo;
    private FirebaseFirestore db;
    private String documentoId; // El ID real del documento en Firestore
    private Producto productoActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_detalle_producto);

        db = FirebaseFirestore.getInstance();

        txtNombre = findViewById(R.id.txtDetalleNombre);
        txtStock = findViewById(R.id.txtDetalleStock);
        txtPrecio = findViewById(R.id.txtDetallePrecio);
        txtCategoria = findViewById(R.id.txtDetalleCategoria);
        txtCodigo = findViewById(R.id.txtDetalleCodigo);

        documentoId = getIntent().getStringExtra("codigo"); // En ver_inventario pasamos el id como "codigo"

        if (documentoId != null) {
            cargarDetalle(documentoId);
            
            findViewById(R.id.btnEliminarDetalle).setOnClickListener(v -> eliminarProducto(documentoId));
            
            findViewById(R.id.btnEditarDetalle).setOnClickListener(v -> {
                if (productoActual != null) {
                    Intent intent = new Intent(this, inventario_lista_completa.class);
                    intent.putExtra("id", documentoId);
                    intent.putExtra("modo_edicion", true);
                    startActivity(intent);
                }
            });
        }

        findViewById(R.id.btnVolverDetalle).setOnClickListener(v -> finish());
    }

    private void eliminarProducto(String id) {
        db.collection("inventario").document(id).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void cargarDetalle(String id) {
        db.collection("inventario").document(id).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        productoActual = documentSnapshot.toObject(Producto.class);
                        if (productoActual != null) {
                            productoActual.setId(documentSnapshot.getId());
                            txtNombre.setText("Nombre: " + productoActual.getNombre());
                            txtStock.setText("Stock: " + productoActual.getStock() + " unidades");
                            txtPrecio.setText("Precio Venta: $" + productoActual.getPrecioVenta());
                            txtCategoria.setText("Categoría: " + productoActual.getCategoria());
                            txtCodigo.setText("Código: " + productoActual.getCodigo());
                        }
                    } else {
                        Toast.makeText(this, "El producto no existe", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar detalle", Toast.LENGTH_SHORT).show());
    }
}
