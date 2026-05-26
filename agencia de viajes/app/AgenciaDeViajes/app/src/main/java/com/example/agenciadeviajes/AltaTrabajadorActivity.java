package com.example.agenciadeviajes;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AltaTrabajadorActivity extends AppCompatActivity {

    private EditText nombreEt, emailEt, passwordEt;
    private Spinner rolSpinner;
    private Button registrarBtn;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alta_trabajador);


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        nombreEt = findViewById(R.id.et_alta_nombre);
        emailEt = findViewById(R.id.et_alta_email);
        passwordEt = findViewById(R.id.et_alta_password);
        rolSpinner = findViewById(R.id.spinner_alta_rol);
        registrarBtn = findViewById(R.id.btn_registrar_empleado);


        String[] roles = {"trabajador", "admin"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, roles);
        rolSpinner.setAdapter(adapter);

        registrarBtn.setOnClickListener(v -> registrarEmpleado());
    }

    private void registrarEmpleado() {
        String nombre = nombreEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String password = passwordEt.getText().toString().trim();
        String rol = rolSpinner.getSelectedItem().toString();


        if (TextUtils.isEmpty(nombre)) {
            nombreEt.setError("Introduce el nombre");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            emailEt.setError("Introduce el email");
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            passwordEt.setError("La contraseña debe tener mínimo 6 caracteres");
            return;
        }


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {

                        String uid = task.getResult().getUser().getUid();


                        guardarEmpleadoEnFirestore(uid, nombre, email, rol);
                    } else {
                        Toast.makeText(AltaTrabajadorActivity.this, "Error en Auth: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void guardarEmpleadoEnFirestore(String uid, String nombre, String email, String rol) {

        Map<String, Object> empleado = new HashMap<>();
        empleado.put("nombre", nombre);
        empleado.put("email", email);
        empleado.put("rol", rol);


        db.collection("Empleados").document(uid).set(empleado)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AltaTrabajadorActivity.this, "Empleado dado de alta correctamente", Toast.LENGTH_SHORT).show();

                    nombreEt.setText("");
                    emailEt.setText("");
                    passwordEt.setText("");
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AltaTrabajadorActivity.this, "Fallo al guardar en base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}