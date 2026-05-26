package com.example.agenciadeviajes;

public class Vuelo {
    private String origen;
    private String destino;
    private String fecha;
    private String precio;


    public Vuelo() {}

    public Vuelo(String origen, String destino, String fecha, String precio) {
        this.origen = origen;
        this.destino = destino;
        this.fecha = fecha;
        this.precio = precio;
    }


    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public String getFecha() { return fecha; }
    public String getPrecio() { return precio; }
}