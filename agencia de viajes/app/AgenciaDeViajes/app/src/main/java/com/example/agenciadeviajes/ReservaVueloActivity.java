package com.example.agenciadeviajes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReservaVueloActivity extends AppCompatActivity {

    private EditText origenEt, destinoEt, fechaEt;
    private Button btnConfirmar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    private Date fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_vuelo);


        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        origenEt = findViewById(R.id.et_origen);
        destinoEt = findViewById(R.id.et_destino);
        fechaEt = findViewById(R.id.et_fecha);
        btnConfirmar = findViewById(R.id.btn_confirmar_vuelo);


        fechaEt.setOnClickListener(v -> mostrarCalendario());


        btnConfirmar.setOnClickListener(v -> guardarReservaEnFirestore());
    }

    private void mostrarCalendario() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    c.set(year1, monthOfYear, dayOfMonth);
                    fechaSeleccionada = c.getTime();


                    String fechaMostrar = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    fechaEt.setText(fechaMostrar);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void guardarReservaEnFirestore() {
        String origen = origenEt.getText().toString().trim();
        String destino = destinoEt.getText().toString().trim();


        String uid = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;
        String email = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getEmail() : null;


        if (uid == null || email == null) {
            Toast.makeText(this, "Usuario no identificado en el sistema", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(origen)) {
            origenEt.setError("Introduce el origen");
            return;
        }

        if (TextUtils.isEmpty(destino)) {
            destinoEt.setError("Introduce el destino");
            return;
        }

        if (fechaSeleccionada == null) {
            Toast.makeText(this, "Selecciona una fecha para tu viaje", Toast.LENGTH_SHORT).show();
            return;
        }


        Map<String, Object> reserva = new HashMap<>();
        reserva.put("idCliente", uid);
        reserva.put("emailCliente", email);
        reserva.put("origen", origen);
        reserva.put("destino", destino);
        reserva.put("tipo", "vuelo");
        reserva.put("estado", "Petición");


        reserva.put("fecha", new Timestamp(fechaSeleccionada));
        reserva.put("creado_el", Timestamp.now());


        db.collection("reservas")
                .add(reserva)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ReservaVueloActivity.this, "Solicitud enviada! Pendiente de asignacion por el personal", Toast.LENGTH_LONG).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ReservaVueloActivity.this, "Error al procesar la solicitud: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}