package com.example.agenciadeviajes;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VuelosDirecto extends AppCompatActivity {

    private MapView map;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable updateRunnable;
    private static final int INTERVALO = 8000; // 8 segundos
    private static final String BASE_URL = "https://opensky-network.org/api/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Configuration.getInstance().setUserAgentValue(getPackageName());


        setContentView(R.layout.activity_vuelos_directo);


        map = findViewById(R.id.map);
        if (map != null) {
            map.setMultiTouchControls(true);
            map.getController().setZoom(6.5);
            map.getController().setCenter(new GeoPoint(40.0, -3.5)); // Centrado en España aprox.
        }


        FloatingActionButton btnRefresh = findViewById(R.id.btnRefresh);
        if (btnRefresh != null) {
            btnRefresh.setOnClickListener(v -> cargarVuelos());
        }


        updateRunnable = new Runnable() {
            @Override
            public void run() {
                cargarVuelos();
                handler.postDelayed(this, INTERVALO);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (map != null) map.onResume();
        handler.post(updateRunnable); // Iniciar bucle
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (map != null) map.onPause();
        handler.removeCallbacks(updateRunnable); // Detener bucle para ahorrar batería
    }

    private void cargarVuelos() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OpenSkyService service = retrofit.create(OpenSkyService.class);

        // Coordenadas de ejemplo (Lamin, Lomin, Lamax, Lomax)
        Call<OpenSkyResponse> call = service.getAreaStates(34.0, -10.0, 44.0, 5.0);

        call.enqueue(new Callback<OpenSkyResponse>() {
            @Override
            public void onResponse(Call<OpenSkyResponse> call, Response<OpenSkyResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().states != null) {
                    actualizarMapa(response.body().states);
                }
            }

            @Override
            public void onFailure(Call<OpenSkyResponse> call, Throwable t) {
                Log.e("API", "Error: " + t.getMessage());
                Toast.makeText(VuelosDirecto.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarMapa(List<List<Object>> states) {
        map.getOverlays().clear(); // Limpiar aviones viejos

        for (List<Object> s : states) {
            try {
                // Verificar que latitud (pos 6) y longitud (pos 5) no sean nulas
                if (s.get(5) != null && s.get(6) != null) {
                    double lon = ((Number) s.get(5)).doubleValue();
                    double lat = ((Number) s.get(6)).doubleValue();

                    // Extraer información para el bocadillo
                    String callsign = s.get(1) != null ? s.get(1).toString().trim() : "N/A";
                    String pais = s.get(2) != null ? s.get(2).toString() : "Desconocido";
                    double altitud = s.get(7) != null ? ((Number) s.get(7)).doubleValue() : 0.0;
                    double velocidad = s.get(9) != null ? ((Number) s.get(9)).doubleValue() * 3.6 : 0.0; // m/s a km/h

                    // Rumbo (Heading) en la posición 10
                    float rumbo = s.get(10) != null ? ((Number) s.get(10)).floatValue() : 0.0f;

                    // Crear Marcador
                    Marker m = new Marker(map);
                    m.setPosition(new GeoPoint(lat, lon));

                    // Configurar Icono de Avión
                    m.setIcon(getResources().getDrawable(R.drawable.ic_avion, getTheme()));
                    m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER);

                    // Rotar el avión hacia donde vuela
                    m.setRotation(rumbo);

                    // Configurar el bocadillo (InfoWindow)
                    m.setTitle("Vuelo: " + callsign);
                    String detalle = "País: " + pais + "\n" +
                            "Altitud: " + String.format("%.0f", altitud) + " m\n" +
                            "Velocidad: " + String.format("%.0f", velocidad) + " km/h";
                    m.setSnippet(detalle);

                    map.getOverlays().add(m);
                }
            } catch (Exception e) {
                Log.e("API", "Error procesando un avión individual");
            }
        }
        map.invalidate(); // Refrescar el mapa para mostrar los nuevos marcadores
    }
}