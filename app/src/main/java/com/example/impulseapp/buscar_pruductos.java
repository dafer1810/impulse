package com.example.impulseapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class buscar_pruductos extends AppCompatActivity {

    private TextInputEditText edtBuscar;
    private MaterialButton btnEjecutarBusqueda;
    private RecyclerView rvResultados;
    private ProductoAdapter adapter;
    private List<Producto> listaProductos;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_buscar_pruductos);

        db = FirebaseFirestore.getInstance();
        edtBuscar = findViewById(R.id.edtBuscar);
        btnEjecutarBusqueda = findViewById(R.id.btnEjecutarBusqueda);
        rvResultados = findViewById(R.id.rvResultados);

        listaProductos = new ArrayList<>();
        adapter = new ProductoAdapter(listaProductos);

        rvResultados.setLayoutManager(new LinearLayoutManager(this));
        rvResultados.setAdapter(adapter);

        // Acción del botón BUSCAR
        btnEjecutarBusqueda.setOnClickListener(v -> {
            String query = edtBuscar.getText().toString().trim();
            buscarEnFirebase(query);
        });

        // Cargar todos al inicio para que el panel no esté vacío
        buscarEnFirebase("");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void buscarEnFirebase(String query) {
        Toast.makeText(this, "Consultando inventario...", Toast.LENGTH_SHORT).show();
        
        // Según tu captura, la colección se llama "inventario"
        db.collection("inventario")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaProductos.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Producto producto = document.toObject(Producto.class);
                                String q = query.toLowerCase();
                                
                                String nombre = producto.getNombre() != null ? producto.getNombre().toLowerCase() : "";
                                String codigo = producto.getCodigo() != null ? producto.getCodigo().toLowerCase() : "";
                                String categoria = producto.getCategoria() != null ? producto.getCategoria().toLowerCase() : "";

                                if (q.isEmpty() || nombre.contains(q) || codigo.contains(q) || categoria.contains(q)) {
                                    listaProductos.add(producto);
                                }
                            } catch (Exception e) {
                                Log.e("FIREBASE_ERROR", "Error al convertir producto: " + e.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        
                        if (listaProductos.isEmpty()) {
                            Toast.makeText(this, "No se encontraron productos", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al conectar con Firestore", Toast.LENGTH_SHORT).show();
                        Log.e("FIREBASE_ERROR", "Error: ", task.getException());
                    }
                });
    }
}