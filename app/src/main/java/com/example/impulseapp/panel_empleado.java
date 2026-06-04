package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class panel_empleado extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panel_empleado);

        mAuth = FirebaseAuth.getInstance();

        // Referencias a los botones
        MaterialButton btnFacturarVentas = findViewById(R.id.btnFacturarVentas);
        MaterialButton btnBuscarProducto = findViewById(R.id.btnBuscarProducto);
        MaterialButton btnVerTareas = findViewById(R.id.btnVerTareas);
        MaterialButton btnHistorialVentas = findViewById(R.id.btnHistorialVentas);
        MaterialButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        // Configuración de clics
        btnFacturarVentas.setOnClickListener(v -> {
            Intent intent = new Intent(panel_empleado.this, facturar_ventas.class);
            startActivity(intent);
        });

        btnBuscarProducto.setOnClickListener(v -> {
            Intent intent = new Intent(panel_empleado.this, buscar_pruductos.class);
            startActivity(intent);
        });

        btnVerTareas.setOnClickListener(v -> {
            Intent intent = new Intent(panel_empleado.this, ver_tareas.class);
            startActivity(intent);
        });

        btnHistorialVentas.setOnClickListener(v -> {
            Intent intent = new Intent(panel_empleado.this, historial_ventas.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            // 1. Cerrar sesión en Firebase
            mAuth.signOut();

            // 2. Regresar a la pantalla de inicio de sesión
            Intent intent = new Intent(panel_empleado.this, pantalla_inicio_de_sesion.class);
            startActivity(intent);
            
            // Finalizar esta actividad
            finish();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}