package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ver_tareas extends AppCompatActivity {

    private RecyclerView rvTareas;
    private TareaAdapter adapter;
    private List<Tarea> listaTareas;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private MaterialButton btnConsultarTareas, btnNuevaTarea, btnVolverTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_tareas);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        rvTareas = findViewById(R.id.rvTareas);
        btnConsultarTareas = findViewById(R.id.btnConsultarTareas);
        btnNuevaTarea = findViewById(R.id.btnNuevaTarea);
        btnVolverTareas = findViewById(R.id.btnVolverTareas);

        // Verificar rol para mostrar/ocultar botón de nueva tarea
        verificarRol();

        listaTareas = new ArrayList<>();
        adapter = new TareaAdapter(listaTareas, tarea -> {
            // Al hacer clic, abrimos para editar/ver detalle
            Intent intent = new Intent(this, asignar_tareas.class);
            intent.putExtra("tarea_id", tarea.getId());
            intent.putExtra("modo_edicion", true);
            startActivity(intent);
        });

        rvTareas.setLayoutManager(new LinearLayoutManager(this));
        rvTareas.setAdapter(adapter);

        btnConsultarTareas.setOnClickListener(v -> cargarTareas());
        
        btnNuevaTarea.setOnClickListener(v -> {
            Intent intent = new Intent(this, asignar_tareas.class);
            startActivity(intent);
        });

        btnVolverTareas.setOnClickListener(v -> finish());

        cargarTareas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareas();
    }

    private void verificarRol() {
        String uid = mAuth.getUid();
        if (uid == null) return;

        db.collection("administradores").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        btnNuevaTarea.setVisibility(android.view.View.VISIBLE);
                    } else {
                        btnNuevaTarea.setVisibility(android.view.View.GONE);
                    }
                })
                .addOnFailureListener(e -> btnNuevaTarea.setVisibility(android.view.View.GONE));
    }

    private void cargarTareas() {
        // Mostramos un mensaje de carga
        Toast.makeText(this, "Buscando tareas en la base de datos...", Toast.LENGTH_SHORT).show();
        
        db.collection("tareas")
                .orderBy("fecha", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaTareas.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Tarea tarea = document.toObject(Tarea.class);
                                tarea.setId(document.getId());
                                listaTareas.add(tarea);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                        
                        if (listaTareas.isEmpty()) {
                            Toast.makeText(this, "No hay tareas registradas", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Tareas actualizadas", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Error al conectar con Firebase", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}