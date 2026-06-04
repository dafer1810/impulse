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
    private MaterialButton btnConsultarTareas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ver_tareas);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        rvTareas = findViewById(R.id.rvTareas);
        btnConsultarTareas = findViewById(R.id.btnConsultarTareas);

        listaTareas = new ArrayList<>();
        adapter = new TareaAdapter(listaTareas);

        rvTareas.setLayoutManager(new LinearLayoutManager(this));
        rvTareas.setAdapter(adapter);

        // Configurar el botón para realizar la búsqueda manual
        btnConsultarTareas.setOnClickListener(v -> {
            cargarTareas();
        });

        // Cargar automáticamente al entrar
        cargarTareas();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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