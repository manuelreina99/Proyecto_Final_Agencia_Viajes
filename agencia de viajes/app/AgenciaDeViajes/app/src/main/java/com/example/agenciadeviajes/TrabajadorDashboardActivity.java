package com.example.agenciadeviajes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrabajadorDashboardActivity extends AppCompatActivity {

    private Spinner clientesSpinner, vuelosSpinner;
    private Button asignarBtn;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Listas para Vuelos Disponibles
    private List<String> listaVuelosTexto = new ArrayList<>();
    private List<String> listaVuelosIds = new ArrayList<>();
    private ArrayAdapter<String> vuelosAdapter;

    // Listas para Peticiones Activas de Clientes
    private List<String> listaClientesTexto = new ArrayList<>();
    private List<String> listaClientesReservaIds = new ArrayList<>(); // Almacena el ID del documento en "reservas"
    private List<Map<String, Object>> listaDatosPeticiones = new ArrayList<>();
    private ArrayAdapter<String> clientesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trabajador_dashboard);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        clientesSpinner = findViewById(R.id.spinner_trabajador_clientes);
        vuelosSpinner = findViewById(R.id.spinner_trabajador_vuelos);
        asignarBtn = findViewById(R.id.btn_asignar_vuelo);


        vuelosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaVuelosTexto);
        vuelosSpinner.setAdapter(vuelosAdapter);

        clientesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaClientesTexto);
        clientesSpinner.setAdapter(clientesAdapter);


        cargarVuelosDisponibles();
        cargarPeticionesClientes();


        asignarBtn.setOnClickListener(v -> procesarAsignacion());

        findViewById(R.id.btn_logout_trabajador).setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(TrabajadorDashboardActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void cargarVuelosDisponibles() {
        db.collection("vuelos_disponibles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                listaVuelosTexto.clear();
                listaVuelosIds.clear();
                for (DocumentSnapshot doc : task.getResult()) {
                    String origen = doc.getString("origen");
                    String destino = doc.getString("destino");
                    Double precio = doc.getDouble("precio");

                    if (origen != null && destino != null) {
                        listaVuelosTexto.add(origen + " ➔ " + destino + " (" + (precio != null ? precio : 0) + "€)");
                        listaVuelosIds.add(doc.getId());
                    }
                }
                vuelosAdapter.notifyDataSetChanged();
            }
        });
    }

    private void cargarPeticionesClientes() {

        db.collection("reservas")
                .whereEqualTo("estado", "Petición")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        listaClientesTexto.clear();
                        listaClientesReservaIds.clear();
                        listaDatosPeticiones.clear();

                        for (DocumentSnapshot doc : task.getResult()) {
                            String email = doc.getString("emailCliente");
                            String origen = doc.getString("origen");
                            String destino = doc.getString("destino");

                            if (email != null) {

                                String infoMostrar = email + " (" + origen + " ➔ " + destino + ")";
                                listaClientesTexto.add(infoMostrar);
                                listaClientesReservaIds.add(doc.getId());
                                listaDatosPeticiones.add(doc.getData());
                            }
                        }
                        clientesAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void procesarAsignacion() {
        if (listaClientesReservaIds.isEmpty() || clientesSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "No hay peticiones de clientes pendientes", Toast.LENGTH_SHORT).show();
            return;
        }
        if (listaVuelosIds.isEmpty() || vuelosSpinner.getSelectedItem() == null) {
            Toast.makeText(this, "No hay vuelos disponibles para asignar", Toast.LENGTH_SHORT).show();
            return;
        }


        int posCliente = clientesSpinner.getSelectedItemPosition();
        String idReservaPendiente = listaClientesReservaIds.get(posCliente);


        int posVuelo = vuelosSpinner.getSelectedItemPosition();
        String idVueloSeleccionado = listaVuelosIds.get(posVuelo);
        String textoVueloSeleccionado = listaVuelosTexto.get(posVuelo);


        String nombreTemporal = "Empleado Anonimo";
        if (mAuth.getCurrentUser() != null) {
            if (mAuth.getCurrentUser().getDisplayName() != null && !mAuth.getCurrentUser().getDisplayName().isEmpty()) {
                nombreTemporal = mAuth.getCurrentUser().getDisplayName();
            } else if (mAuth.getCurrentUser().getEmail() != null) {

                String email = mAuth.getCurrentUser().getEmail();
                nombreTemporal = email.split("@")[0];
            }
        }


        final String nombreTrabajadorFinal = nombreTemporal;


        Map<String, Object> actualizacion = new HashMap<>();
        actualizacion.put("idVuelo", idVueloSeleccionado);
        actualizacion.put("vueloInfo", textoVueloSeleccionado);
        actualizacion.put("estado", "Asignado");
        actualizacion.put("nombreTrabajador", nombreTrabajadorFinal);


        db.collection("reservas").document(idReservaPendiente)
                .update(actualizacion)
                .addOnSuccessListener(aVoid -> {

                    Toast.makeText(TrabajadorDashboardActivity.this, "Vuelo asignado correctamente por " + nombreTrabajadorFinal, Toast.LENGTH_SHORT).show();

                    cargarPeticionesClientes();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(TrabajadorDashboardActivity.this, "Error en la base de datos: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}