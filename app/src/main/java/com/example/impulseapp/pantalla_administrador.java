package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class pantalla_administrador extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_administrador);

        mAuth = FirebaseAuth.getInstance();
        //Comentario de prueba
        // Referencia al contenedor del botón en el menú inferior
        LinearLayout btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(v -> {
            // 1. Cerrar sesión en Firebase
            mAuth.signOut();

            // 2. Regresar a la pantalla de inicio de sesión
            Intent intent = new Intent(pantalla_administrador.this, pantalla_inicio_de_sesion.class);
            startActivity(intent);
            finish();
        });
    }
}