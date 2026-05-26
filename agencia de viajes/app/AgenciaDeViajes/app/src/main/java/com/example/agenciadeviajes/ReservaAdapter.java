package com.example.agenciadeviajes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReservaAdapter extends RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder> {

    private List<Map<String, Object>> listaReservas;

    public ReservaAdapter(List<Map<String, Object>> listaReservas) {
        this.listaReservas = listaReservas;
    }

    @Override
    public ReservaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reserva, parent, false);
        return new ReservaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReservaViewHolder holder, int position) {
        Map<String, Object> reserva = listaReservas.get(position);


        if (reserva.get("vueloInfo") != null) {
            holder.tvRuta.setText(reserva.get("vueloInfo").toString());
        } else {
            String origen = reserva.get("origen") != null ? reserva.get("origen").toString() : "N/A";
            String destino = reserva.get("destino") != null ? reserva.get("destino").toString() : "N/A";
            holder.tvRuta.setText(origen + " ➔ " + destino);
        }


        if (reserva.get("fecha") instanceof Timestamp) {
            Timestamp ts = (Timestamp) reserva.get("fecha");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvFecha.setText("Fecha de salida: " + sdf.format(ts.toDate()));
        } else {
            holder.tvFecha.setText("Fecha no disponible");
        }


        String nombreTrabajador = reserva.get("nombreTrabajador") != null ? reserva.get("nombreTrabajador").toString() : null;
        if (nombreTrabajador != null && !nombreTrabajador.isEmpty()) {
            holder.tvTrabajador.setText("Asignado por: " + nombreTrabajador);
        } else {

            holder.tvTrabajador.setText("Asignado por: Oficina Central");
        }
    }

    @Override
    public int getItemCount() {
        return listaReservas.size();
    }

    public static class ReservaViewHolder extends RecyclerView.ViewHolder {
        TextView tvRuta, tvFecha, tvTrabajador;

        public ReservaViewHolder(View itemView) {
            super(itemView);
            tvRuta = itemView.findViewById(R.id.tv_ruta);
            tvFecha = itemView.findViewById(R.id.tv_fecha);
            tvTrabajador = itemView.findViewById(R.id.tv_trabajador);
        }
    }
}