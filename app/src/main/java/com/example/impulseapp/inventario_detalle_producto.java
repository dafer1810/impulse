package com.example.impulseapp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class inventario_detalle_producto extends AppCompatActivity {

    private TextView txtNombre, txtStock, txtPrecio, txtCategoria, txtCodigo;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario_detalle_producto);

        db = FirebaseFirestore.getInstance();

        // En el layout previo no había IDs para todos estos campos, los usaré como referencia
        // o asumiré que existen/deben crearse en el XML.
        // Voy a revisar activity_inventario_detalle_producto.xml primero.
        
        txtNombre = findViewById(R.id.txtDetalleNombre); // Necesito actualizar XML
        txtStock = findViewById(R.id.txtDetalleStock);
        txtPrecio = findViewById(R.id.txtDetallePrecio);
        txtCategoria = findViewById(R.id.txtDetalleCategoria);
        txtCodigo = findViewById(R.id.txtDetalleCodigo);

        String codigo = getIntent().getStringExtra("codigo");

        if (codigo != null) {
            cargarDetalle(codigo);
            findViewById(R.id.btnEliminarDetalle).setOnClickListener(v -> eliminarProducto(codigo));
        }

        findViewById(R.id.btnVolverDetalle).setOnClickListener(v -> finish());
    }

    private void eliminarProducto(String codigo) {
        db.collection("inventario").document(codigo).delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show());
    }

    private void cargarDetalle(String codigo) {
        db.collection("inventario").document(codigo).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Producto p = documentSnapshot.toObject(Producto.class);
                        if (p != null) {
                            txtNombre.setText("Nombre: " + p.getNombre());
                            txtStock.setText("Stock: " + p.getStock() + " unidades");
                            txtPrecio.setText("Precio: $" + p.getPrecioVenta());
                            txtCategoria.setText("Categoría: " + p.getCategoria());
                            txtCodigo.setText("Código: " + p.getCodigo());
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar detalle", Toast.LENGTH_SHORT).show());
    }
}