package com.example.impulseapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class historial_ventas extends AppCompatActivity {

    private RecyclerView rvVentas;
    private VentaAdapter adapter;
    private List<Venta> listaVentas;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_historial_ventas);

        db = FirebaseFirestore.getInstance();
        rvVentas = findViewById(R.id.rvVentas);
        listaVentas = new ArrayList<>();
        adapter = new VentaAdapter(listaVentas);

        rvVentas.setLayoutManager(new LinearLayoutManager(this));
        rvVentas.setAdapter(adapter);

        cargarVentas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void cargarVentas() {
        db.collection("ventas")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaVentas.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Venta venta = document.toObject(Venta.class);
                            listaVentas.add(venta);
                        }
                        adapter.notifyDataSetChanged();
                        
                        if (listaVentas.isEmpty()) {
                            Toast.makeText(this, "No hay ventas registradas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar el historial", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}