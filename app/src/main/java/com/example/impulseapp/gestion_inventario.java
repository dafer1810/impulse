package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class gestion_inventario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_inventario);

        // Opción: Ver Inventario
        findViewById(R.id.btnVerInventario).setOnClickListener(v -> {
            Intent intent = new Intent(this, ver_inventario.class);
            startActivity(intent);
        });

        // Opción: Añadir al Inventario
        findViewById(R.id.btnAnadirInventario).setOnClickListener(v -> {
            Intent intent = new Intent(this, inventario_lista_completa.class);
            startActivity(intent);
        });

        // Opción: Eliminar Inventario (Placeholder o funcionalidad específica)
        findViewById(R.id.btnEliminarInventario).setOnClickListener(v -> {
            // Funcionalidad por implementar
        });

        // Opción: Actualizar Inventario (Placeholder o funcionalidad específica)
        findViewById(R.id.btnActualizarInventario).setOnClickListener(v -> {
            // Funcionalidad por implementar
        });

        // Botón Volver
        findViewById(R.id.btnVolver).setOnClickListener(v -> {
            finish();
        });
    }
}