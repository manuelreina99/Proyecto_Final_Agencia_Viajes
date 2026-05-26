package com.example.agenciadeviajes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class AdminDashboardActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        mAuth = FirebaseAuth.getInstance();

        LinearLayout cardAgregarVuelo = findViewById(R.id.card_agregar_vuelo);
        LinearLayout cardAltaTrabajadores = findViewById(R.id.card_alta_trabajadores);

        // Evento para abrir el formulario de añadir vuelos a "vuelos_disponibles"
        cardAgregarVuelo.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AgregarVueloActivity.class);
            startActivity(intent);
        });

        // Evento para abrir la pantalla de registro de nuevos trabajadores
        cardAltaTrabajadores.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AltaTrabajadorActivity.class);
            startActivity(intent);
        });

        // Evento para cerrar sesión
        findViewById(R.id.btn_logout_admin).setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(AdminDashboardActivity.this, "Sesión de administrador cerrada", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(AdminDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}