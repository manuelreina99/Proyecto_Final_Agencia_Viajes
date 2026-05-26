package com.example.agenciadeviajes;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AgregarVueloActivity extends AppCompatActivity {

    private EditText origenEt, destinoEt, aerolineaEt, fechaEt, precioEt, asientosEt;
    private Button guardarBtn;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_vuelo);

        // Inicializar la instancia de Firestore
        db = FirebaseFirestore.getInstance();

        // Vincular componentes de la interfaz
        origenEt = findViewById(R.id.et_vuelo_origen);
        destinoEt = findViewById(R.id.et_vuelo_destino);
        aerolineaEt = findViewById(R.id.et_vuelo_aerolinea);
        fechaEt = findViewById(R.id.et_vuelo_fecha);
        precioEt = findViewById(R.id.et_vuelo_precio);
        asientosEt = findViewById(R.id.et_vuelo_asientos);
        guardarBtn = findViewById(R.id.btn_guardar_vuelo);

        guardarBtn.setOnClickListener(v -> subirVueloAFirestore());
    }

    private void subirVueloAFirestore() {
        String origen = origenEt.getText().toString().trim();
        String destino = destinoEt.getText().toString().trim();
        String aerolinea = aerolineaEt.getText().toString().trim();
        String fecha = fechaEt.getText().toString().trim();
        String precioStr = precioEt.getText().toString().trim();
        String asientosStr = asientosEt.getText().toString().trim();

        // Validar que no haya campos vacíos
        if (TextUtils.isEmpty(origen) || TextUtils.isEmpty(destino) ||
                TextUtils.isEmpty(aerolinea) || TextUtils.isEmpty(fecha) ||
                TextUtils.isEmpty(precioStr) || TextUtils.isEmpty(asientosStr)) {

            Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Conversión segura de tipos de datos numéricos
        double precio;
        int asientos;
        try {
            precio = Double.parseDouble(precioStr);
            asientos = Integer.parseInt(asientosStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El precio o los asientos no tienen un formato válido", Toast.LENGTH_SHORT).show();
            return;
        }

        // Estructurar la información del vuelo en un mapa
        Map<String, Object> vuelo = new HashMap<>();
        vuelo.put("origen", origen);
        vuelo.put("destino", destino);
        vuelo.put("aerolinea", aerolinea);
        vuelo.put("fecha", fecha);
        vuelo.put("precio", precio);
        vuelo.put("asientosDisponibles", asientos);

        //Subir a la colección vuelos_disponibles con un ID autogenerado por Firebase
        db.collection("vuelos_disponibles")
                .add(vuelo)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(AgregarVueloActivity.this, "Oferta de vuelo añadida con éxito", Toast.LENGTH_SHORT).show();
                    limpiarCampos();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AgregarVueloActivity.this, "Error al publicar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void limpiarCampos() {
        origenEt.setText("");
        destinoEt.setText("");
        aerolineaEt.setText("");
        fechaEt.setText("");
        precioEt.setText("");
        asientosEt.setText("");
    }
}