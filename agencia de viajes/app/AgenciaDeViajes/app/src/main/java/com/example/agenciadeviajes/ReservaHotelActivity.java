package com.example.agenciadeviajes;

import android.app.DatePickerDialog;
import android.os.Bundle;
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

public class ReservaHotelActivity extends AppCompatActivity {

    private EditText hotelEt, ciudadEt, fechaEt;
    private Button btnConfirmar;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private Date fechaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva_hotel);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        hotelEt = findViewById(R.id.et_hotel_nombre);
        ciudadEt = findViewById(R.id.et_hotel_ciudad);
        fechaEt = findViewById(R.id.et_hotel_fecha);
        btnConfirmar = findViewById(R.id.btn_confirmar_hotel);

        fechaEt.setOnClickListener(v -> mostrarCalendario());
        btnConfirmar.setOnClickListener(v -> guardarHotelFirestore());
    }

    private void mostrarCalendario() {
        final Calendar c = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    c.set(year, month, dayOfMonth);
                    fechaSeleccionada = c.getTime();
                    fechaEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void guardarHotelFirestore() {
        String hotel = hotelEt.getText().toString().trim();
        String ciudad = ciudadEt.getText().toString().trim();
        String uid = mAuth.getCurrentUser().getUid();

        if (hotel.isEmpty() || ciudad.isEmpty() || fechaSeleccionada == null) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> reserva = new HashMap<>();
        reserva.put("id_usuario", uid);
        reserva.put("origen", "Hotel: " + hotel);
        reserva.put("destino", ciudad);
        reserva.put("tipo", "hotel");
        reserva.put("fecha", new Timestamp(fechaSeleccionada));

        db.collection("reservas").add(reserva)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Hotel reservado!", Toast.LENGTH_SHORT).show();
                    finish();
                });
    }
}