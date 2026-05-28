package com.example.impulseapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class pantalla_inicio_de_sesion extends AppCompatActivity {

    // Credenciales de prueba
    private final String ADMIN_CORREO = "admin@impulse.com";
    private final String ADMIN_CLAVE = "admin123";
    
    private final String EMPLEADO_CORREO = "empleado@impulse.com";
    private final String EMPLEADO_CLAVE = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_inicio_de_sesion);

        // Referencias a los componentes según el XML actual
        TextInputEditText inputEmail = findViewById(R.id.editTextEmail);
        TextInputEditText inputPassword = findViewById(R.id.editTextPassword);
        MaterialButton btnLogin = findViewById(R.id.button);

        btnLogin.setOnClickListener(v -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lógica para Administrador
            if (email.equals(ADMIN_CORREO) && password.equals(ADMIN_CLAVE)) {
                Intent intent = new Intent(pantalla_inicio_de_sesion.this, pantalla_administrador.class);
                startActivity(intent);
                finish();
            } 
            // Lógica para Empleado
            else if (email.equals(EMPLEADO_CORREO) && password.equals(EMPLEADO_CLAVE)) {
                Intent intent = new Intent(pantalla_inicio_de_sesion.this, panel_empleado.class);
                startActivity(intent);
                finish();
            } 
            else {
                Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        // Ajuste EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}