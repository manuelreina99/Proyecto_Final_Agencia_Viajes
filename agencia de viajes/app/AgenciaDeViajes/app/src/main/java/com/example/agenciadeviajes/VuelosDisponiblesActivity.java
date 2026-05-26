package com.example.agenciadeviajes;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VuelosDisponiblesActivity extends AppCompatActivity {

    private RecyclerView rv;
    private VuelosDisponiblesAdapter adapter;
    private List<Map<String, Object>> listaVuelos = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vuelos_disponibles);

        rv = findViewById(R.id.rv_vuelos_disponibles);
        rv.setLayoutManager(new LinearLayoutManager(this));

        adapter = new VuelosDisponiblesAdapter(listaVuelos, vuelo -> {

            crearReserva(vuelo);
        });
        rv.setAdapter(adapter);

        cargarVuelosAdmin();
    }

    private void cargarVuelosAdmin() {
        db.collection("vuelos_disponibles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    listaVuelos.add(doc.getData());
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void crearReserva(Map<String, Object> vuelo) {
        Map<String, Object> reserva = new HashMap<>();
        reserva.put("id_usuario", mAuth.getCurrentUser().getUid());
        reserva.put("origen", vuelo.get("origen"));
        reserva.put("destino", vuelo.get("destino"));
        reserva.put("fecha", new Timestamp(new Date()));

        db.collection("reservas").add(reserva).addOnSuccessListener(documentReference -> {
            Toast.makeText(this, "¡Vuelo Reservado con éxito!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}