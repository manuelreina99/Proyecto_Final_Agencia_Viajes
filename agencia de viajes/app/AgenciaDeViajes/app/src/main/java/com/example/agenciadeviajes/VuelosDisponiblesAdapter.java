package com.example.agenciadeviajes;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Map;

public class VuelosDisponiblesAdapter extends RecyclerView.Adapter<VuelosDisponiblesAdapter.ViewHolder> {

    private List<Map<String, Object>> listaVuelos;
    private OnReservaClickListener listener;

    public interface OnReservaClickListener {
        void onReservaClick(Map<String, Object> vuelo);
    }

    public VuelosDisponiblesAdapter(List<Map<String, Object>> listaVuelos, OnReservaClickListener listener) {
        this.listaVuelos = listaVuelos;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vuelo_disponible, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Map<String, Object> vuelo = listaVuelos.get(position);
        String origen = vuelo.get("origen").toString();
        String destino = vuelo.get("destino").toString();
        holder.tvRuta.setText(origen + " ✈️ " + destino);
        holder.tvPrecio.setText("Precio: " + vuelo.get("precio").toString() + "€");

        holder.btnReservar.setOnClickListener(v -> listener.onReservaClick(vuelo));
    }

    @Override
    public int getItemCount() { return listaVuelos.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRuta, tvPrecio;
        Button btnReservar;
        public ViewHolder(View itemView) {
            super(itemView);
            tvRuta = itemView.findViewById(R.id.tv_oferta_ruta);
            tvPrecio = itemView.findViewById(R.id.tv_oferta_precio);
            btnReservar = itemView.findViewById(R.id.btn_reservar_ahora);
        }
    }
}