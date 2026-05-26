package com.example.agenciadeviajes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvNombre, tvNif, tvEmail;
    private Button btnLogout;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private static final String TAG = "PERFIL_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvNombre = findViewById(R.id.perfil_nombre);
        tvNif = findViewById(R.id.perfil_nif);
        tvEmail = findViewById(R.id.perfil_email);
        btnLogout = findViewById(R.id.btn_cerrar_sesion);

        if (mAuth.getCurrentUser() != null) {
            cargarDatosUsuario();
        } else {
            Toast.makeText(this, "Sesión no válida", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            // Limpiar el historial
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void cargarDatosUsuario() {
        String uid = mAuth.getCurrentUser().getUid();

        db.collection("clientes").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String nombre = documentSnapshot.getString("nombre");
                        String nif = documentSnapshot.getString("nif");
                        String email = documentSnapshot.getString("email");

                        tvNombre.setText(nombre != null ? nombre : "No disponible");
                        tvNif.setText(nif != null ? nif : "No disponible");
                        tvEmail.setText(email != null ? email : "No disponible");
                    } else {
                        Log.d(TAG, "El documento no existe");
                        tvNombre.setText("Usuario no registrado en DB");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error al obtener datos: ", e);
                    Toast.makeText(PerfilActivity.this, "Error de conexión: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    tvNombre.setText("Error al cargar datos");
                });
    }
}