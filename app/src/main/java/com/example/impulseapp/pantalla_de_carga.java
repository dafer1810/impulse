package com.example.impulseapp;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class pantalla_de_carga extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_de_carga);

        ProgressBar progressBar = findViewById(R.id.progressBar);

        // Animación de la barra: de 0 a 100 en 1000ms (1 segundo)
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
        progressAnimator.setDuration(1000);
        progressAnimator.start();

        // Handler para cambiar de pantalla después de 2 segundos
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(pantalla_de_carga.this, pantalla_inicio_de_sesion.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}