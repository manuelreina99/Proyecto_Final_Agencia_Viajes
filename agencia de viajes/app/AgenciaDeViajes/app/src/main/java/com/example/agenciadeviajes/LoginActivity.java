package com.example.agenciadeviajes;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEt, passEt;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        MusicaManager.reproducirMusica(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailEt = findViewById(R.id.email_login);
        passEt = findViewById(R.id.password_login);


        findViewById(R.id.btn_login).setOnClickListener(v -> login());


        findViewById(R.id.btn_go_register).setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void login() {
        String email = emailEt.getText().toString().trim();
        String pass = passEt.getText().toString().trim();


        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Introduce tu email");
            return;
        }

        if (TextUtils.isEmpty(pass)) {
            passEt.setError("Introduce tu contraseña");
            return;
        }


        mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                comprobarRolUsuario();
            } else {

                String errorMsg = "Fallo al iniciar sesión";

                if (task.getException() != null) {
                    Exception e = task.getException();

                    if (e instanceof FirebaseAuthInvalidUserException) {
                        errorMsg = "Este usuario no está registrado";
                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                        errorMsg = "Contraseña incorrecta";
                    } else {
                        errorMsg = "Error: " + e.getLocalizedMessage();
                    }
                }
                Toast.makeText(LoginActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void comprobarRolUsuario() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();


        db.collection("Empleados").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {

                            String rol = document.getString("rol");

                            if (rol != null) {
                                switch (rol) {
                                    case "admin":
                                        redireccionarA(AdminDashboardActivity.class);
                                        break;

                                    case "trabajador":
                                        redireccionarA(TrabajadorDashboardActivity.class);
                                        break;

                                    default:

                                        redireccionarA(MainActivity.class);
                                        break;
                                }
                            } else {

                                redireccionarA(MainActivity.class);
                            }
                        } else {

                            redireccionarA(MainActivity.class);
                        }
                    } else {

                        Toast.makeText(LoginActivity.this, "Error al verificar el rol del usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void redireccionarA(Class<?> destinoClass) {
        Intent intent = new Intent(LoginActivity.this, destinoClass);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}