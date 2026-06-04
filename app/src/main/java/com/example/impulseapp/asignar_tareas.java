package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class asignar_tareas extends AppCompatActivity {

    private Spinner spinnerEmpleados, spinnerEstadoTarea;
    private EditText edtTituloTarea;
    private TextView txtLabelEstado, txtHeaderAsignar;
    private MaterialButton btnGuardarTarea, btnVolverAsignar, btnEliminarTarea;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String tareaId;
    private boolean modoEdicion = false;
    private boolean esAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asignar_tareas);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        spinnerEmpleados = findViewById(R.id.spinnerEmpleados);
        spinnerEstadoTarea = findViewById(R.id.spinnerEstadoTarea);
        edtTituloTarea = findViewById(R.id.edtTituloTarea);
        txtLabelEstado = findViewById(R.id.txtLabelEstado);
        txtHeaderAsignar = findViewById(R.id.txtHeaderAsignar); // Necesito asegurarme que este ID existe en el XML
        btnGuardarTarea = findViewById(R.id.btnGuardarTarea);
        btnVolverAsignar = findViewById(R.id.btnVolverAsignar);
        btnEliminarTarea = findViewById(R.id.btnEliminarTarea);

        // Simulación de empleados (Esto debería venir de Firebase Auth o una colección 'usuarios')
        String[] empleados = {"Empleado 1", "Empleado 2", "General"};
        ArrayAdapter<String> adapterEmp = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, empleados);
        adapterEmp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEmpleados.setAdapter(adapterEmp);

        // Configurar Spinner de Estados
        String[] estados = {"Pendiente", "En progreso", "Completada"};
        ArrayAdapter<String> adapterEst = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estados);
        adapterEst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstadoTarea.setAdapter(adapterEst);

        verificarRol();

        // Verificar si es edición
        Intent intent = getIntent();
        if (intent.hasExtra("tarea_id")) {
            tareaId = intent.getStringExtra("tarea_id");
            modoEdicion = true;
            configurarModoEdicion();
        }

        btnGuardarTarea.setOnClickListener(v -> guardarTarea());
        btnVolverAsignar.setOnClickListener(v -> finish());
        btnEliminarTarea.setOnClickListener(v -> eliminarTarea());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void verificarRol() {
        String uid = mAuth.getUid();
        if (uid == null) return;

        db.collection("administradores").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        esAdmin = true;
                    } else {
                        esAdmin = false;
                        configurarVistaEmpleado();
                    }
                });
    }

    private void configurarVistaEmpleado() {
        // Un empleado no puede cambiar a quién se asigna ni el título
        spinnerEmpleados.setEnabled(false);
        edtTituloTarea.setEnabled(false);
        btnEliminarTarea.setVisibility(View.GONE);
        
        // El empleado solo puede ver el estado para cambiarlo
        txtLabelEstado.setVisibility(View.VISIBLE);
        spinnerEstadoTarea.setVisibility(View.VISIBLE);
        
        if (modoEdicion) {
            btnGuardarTarea.setText("Actualizar Estado");
            btnGuardarTarea.setVisibility(View.VISIBLE);
        } else {
            // Un empleado no debería poder crear tareas
            btnGuardarTarea.setVisibility(View.GONE);
            txtHeaderAsignar.setText("Detalle de Tarea");
        }
    }

    private void configurarModoEdicion() {
        if (esAdmin) {
            btnGuardarTarea.setText("Actualizar Tarea");
            btnEliminarTarea.setVisibility(View.VISIBLE);
        } else {
            btnGuardarTarea.setText("Actualizar Estado");
            btnEliminarTarea.setVisibility(View.GONE);
        }
        
        txtLabelEstado.setVisibility(View.VISIBLE);
        spinnerEstadoTarea.setVisibility(View.VISIBLE);
        
        if (txtHeaderAsignar != null) {
            txtHeaderAsignar.setText(esAdmin ? "Editar Tarea" : "Detalle de Tarea");
        }

        db.collection("tareas").document(tareaId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                Tarea tarea = documentSnapshot.toObject(Tarea.class);
                if (tarea != null) {
                    edtTituloTarea.setText(tarea.getTitulo());
                    
                    // Seleccionar empleado en el spinner
                    ArrayAdapter adapterEmp = (ArrayAdapter) spinnerEmpleados.getAdapter();
                    int posEmp = adapterEmp.getPosition(tarea.getAsignadoA());
                    if (posEmp >= 0) spinnerEmpleados.setSelection(posEmp);

                    // Seleccionar estado en el spinner
                    ArrayAdapter adapterEst = (ArrayAdapter) spinnerEstadoTarea.getAdapter();
                    int posEst = adapterEst.getPosition(tarea.getEstado());
                    if (posEst >= 0) spinnerEstadoTarea.setSelection(posEst);
                    
                    // Re-aplicar restricciones si es empleado, por si el async tardó
                    if (!esAdmin) {
                        configurarVistaEmpleado();
                    }
                }
            }
        });
    }

    private void guardarTarea() {
        String titulo = edtTituloTarea.getText().toString().trim();
        String asignadoA = spinnerEmpleados.getSelectedItem().toString();
        String estado = modoEdicion ? spinnerEstadoTarea.getSelectedItem().toString() : "Pendiente";
        String fecha = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        if (titulo.isEmpty()) {
            Toast.makeText(this, "Escribe una descripción para la tarea", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> tarea = new HashMap<>();
        tarea.put("titulo", titulo);
        tarea.put("descripcion", "Asignada a: " + asignadoA);
        tarea.put("estado", estado);
        tarea.put("asignadoA", asignadoA);
        
        if (!modoEdicion) {
            tarea.put("fecha", fecha);
            db.collection("tareas")
                    .add(tarea)
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(this, "Tarea asignada correctamente", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al asignar tarea", Toast.LENGTH_SHORT).show();
                    });
        } else {
            db.collection("tareas").document(tareaId)
                    .update(tarea)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void eliminarTarea() {
        db.collection("tareas").document(tareaId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
    }
}