package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class pantalla_inicio_de_sesion extends AppCompatActivity {

    // Variables de prueba (Credenciales temporales)
    private final String CORREO_VALIDACION = "test@impulse.com";
    private final String CLAVE_VALIDACION = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_inicio_de_sesion);

        // Referencias a los componentes del XML
        TextInputEditText inputEmail = findViewById(R.id.editTextEmail);
        TextInputEditText inputPassword = findViewById(R.id.editTextPassword);
        Button btnLogin = findViewById(R.id.button);

        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            // Validamos contra nuestras variables de prueba
            if (email.equals(CORREO_VALIDACION) && password.equals(CLAVE_VALIDACION)) {
                // Si es correcto, vamos al Panel de Empleado
                Intent intent = new Intent(pantalla_inicio_de_sesion.this, panel_empleado.class);
                startActivity(intent);
                finish(); // Cerramos el login para que no se pueda volver atrás
            } else {
                // Si es incorrecto, mostramos un aviso
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        // Ajuste para que el diseño respete las barras del sistema (EdgeToEdge)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}