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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class pantalla_inicio_de_sesion extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantalla_inicio_de_sesion);

        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Referencias a los componentes según el XML
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

            iniciarSesion(email, password);
        });

        // Ajuste EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void iniciarSesion(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            verificarRol(user.getUid());
                        }
                    } else {
                        Toast.makeText(pantalla_inicio_de_sesion.this, "Error de autenticación: " + 
                                (task.getException() != null ? task.getException().getMessage() : "Desconocido"),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void verificarRol(String uid) {
        // Intentar buscar en administradores
        db.collection("administradores").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Es Administrador
                            irAPantalla(pantalla_administrador.class);
                        } else {
                            // No es admin, buscar en empleados
                            verificarEmpleado(uid);
                        }
                    } else {
                        Toast.makeText(this, "Error al consultar roles", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verificarEmpleado(String uid) {
        db.collection("empleados").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Es Empleado (Vendedor, Cajero, etc.)
                            irAPantalla(panel_empleado.class);
                        } else {
                            Toast.makeText(this, "Usuario sin rol asignado en la base de datos", Toast.LENGTH_LONG).show();
                            mAuth.signOut();
                        }
                    } else {
                        Toast.makeText(this, "Error al consultar empleados", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void irAPantalla(Class<?> destino) {
        Intent intent = new Intent(pantalla_inicio_de_sesion.this, destino);
        startActivity(intent);
        finish();
    }
}