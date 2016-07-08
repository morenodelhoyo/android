package com.example.david.diariodemisviajes;

public class Viaje {

    private String idViaje;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String categoria;

    public Viaje(String descripcion, String idViaje, String titulo,
                 String fecha, String categoria) {
        this.descripcion = descripcion;
        this.idViaje = idViaje;
        this.titulo = titulo;
        this.fecha = fecha;
        this.categoria = categoria;
    }

    public String getTitulo() {
        return titulo;
    }
    public String getIdentificador() {
        return idViaje;
    }
    public String getFecha() {
        return fecha;
    }
    public String getDescripcion() {
        return descripcion;
    }
    public String getCategoria() {
        return categoria;
    }
}
