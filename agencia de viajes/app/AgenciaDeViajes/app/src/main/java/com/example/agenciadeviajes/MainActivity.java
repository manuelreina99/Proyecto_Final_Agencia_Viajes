package com.example.agenciadeviajes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout; // IMPORTANTE: Cambiado de CardView a LinearLayout
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView tvWelcome;
    private ImageButton btnLogout;

    private LinearLayout btnVuelo, btnHotel, btnReservas, btnPerfil, btnMapa, btnOfertas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        tvWelcome = findViewById(R.id.tv_welcome);
        btnLogout = findViewById(R.id.btn_logout);

        btnVuelo = findViewById(R.id.card_vuelo);
        btnHotel = findViewById(R.id.card_hotel);
        btnReservas = findViewById(R.id.card_reservas);
        btnPerfil = findViewById(R.id.card_perfil);
        btnMapa = findViewById(R.id.card_mapa);
        btnOfertas = findViewById(R.id.cardOfertas);

        if (currentUser != null) {
            String email = currentUser.getEmail();
            String nombre = (email != null && email.contains("@")) ? email.split("@")[0] : "Aventurero";
            tvWelcome.setText("Hola, " + nombre);
        }

        setupListeners();
    }

    private void setupListeners() {
        btnOfertas.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, VuelosDisponiblesActivity.class));
        });

        btnReservas.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MisReservasActivity.class));
        });

        btnMapa.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, VuelosDirecto.class));
        });

        btnPerfil.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, PerfilActivity.class));
        });

        btnVuelo.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReservaVueloActivity.class));
        });

        btnHotel.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ReservaHotelActivity.class));
        });


        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}