package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class ver_inventario extends AppCompatActivity {

    private RecyclerView recyclerView;
    private InventarioAdapter adapter;
    private List<Producto> productoList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_inventario);

        db = FirebaseFirestore.getInstance();
        recyclerView = findViewById(R.id.rvInventario);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productoList = new ArrayList<>();
        adapter = new InventarioAdapter(productoList, producto -> {
            Intent intent = new Intent(this, inventario_detalle_producto.class);
            intent.putExtra("codigo", producto.getCodigo());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnVolverVer).setOnClickListener(v -> finish());

        obtenerProductos();
    }

    private void obtenerProductos() {
        db.collection("inventario").get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productoList.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Producto producto = document.toObject(Producto.class);
                        productoList.add(producto);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show());
    }
}