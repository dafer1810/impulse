package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class pantalla_administrador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_administrador);

        Button btnLogout = findViewById(R.id.btn_logout_admin);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(pantalla_administrador.this, pantalla_inicio_de_sesion.class);
            startActivity(intent);
            finish();
        });
    }
}