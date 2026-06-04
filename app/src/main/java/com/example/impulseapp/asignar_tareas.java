package com.example.impulseapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class asignar_tareas extends AppCompatActivity {

    private Spinner spinnerEmpleados;
    private EditText edtTituloTarea;
    private MaterialButton btnGuardarTarea, btnVolverAsignar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asignar_tareas);

        db = FirebaseFirestore.getInstance();

        spinnerEmpleados = findViewById(R.id.spinnerEmpleados);
        edtTituloTarea = findViewById(R.id.edtTituloTarea);
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        btnVolverAsignar = findViewById(R.id.btnVolverAsignar);

        // Simulación de empleados (Esto debería venir de Firebase Auth o una colección 'usuarios')
        String[] empleados = {"Empleado 1", "Empleado 2", "General"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empleados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmpleados.setAdapter(adapter);

        btnGuardarTarea.setOnClickListener(v -> guardarTarea());
        btnVolverAsignar.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void guardarTarea() {
        String titulo = edtTituloTarea.getText().toString().trim();
        String asignadoA = spinnerEmpleados.getSelectedItem().toString();
        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        if (titulo.isEmpty()) {
            Toast.makeText(this, "Escribe una descripción para la tarea", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> tarea = new HashMap<>();
        tarea.put("titulo", titulo);
        tarea.put("descripcion", "Asignada a: " + asignadoA);
        tarea.put("estado", "Pendiente");
        tarea.put("fecha", fecha);
        tarea.put("asignadoA", asignadoA);

        db.collection("tareas")
                .add(tarea)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this, "Tarea asignada correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al asignar tarea", Toast.LENGTH_SHORT).show();
                });
    }
}