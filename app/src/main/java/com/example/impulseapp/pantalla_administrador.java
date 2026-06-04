package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class pantalla_administrador extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_administrador);

        mAuth = FirebaseAuth.getInstance();

        MaterialButton btnInventario = findViewById(R.id.btnInventario);
        MaterialButton btnVentas = findViewById(R.id.btnVentas);
        MaterialButton btnCompras = findViewById(R.id.btnCompras);
        MaterialButton btnGastos = findViewById(R.id.btnGastos);
        MaterialButton btnResumenNegocio = findViewById(R.id.btnResumenNegocio);
        MaterialButton btnAlertasStock = findViewById(R.id.btnAlertasStock);
        MaterialButton btnAsignarTareas = findViewById(R.id.btnAsignarTareas);
        MaterialButton btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnInventario.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, buscar_pruductos.class);
            startActivity(intent);
        });

        btnVentas.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, historial_ventas.class);
            startActivity(intent);
        });

        btnCompras.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, registro_compras.class);
            startActivity(intent);
        });

        btnGastos.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, control_gastos.class);
            startActivity(intent);
        });

        btnResumenNegocio.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, resumen_negocio.class);
            startActivity(intent);
        });

        btnAlertasStock.setOnClickListener(v -> {
            // Podrías reutilizar buscar_productos con un filtro de stock bajo
            Intent intent = new Intent(pantalla_administrador.this, buscar_pruductos.class);
            startActivity(intent);
        });

        btnAsignarTareas.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, asignar_tareas.class);
            startActivity(intent);
        });

        btnCerrarSesion.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(pantalla_administrador.this, pantalla_inicio_de_sesion.class);
            startActivity(intent);
            finish();
        });
    }
}