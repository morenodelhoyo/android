package com.example.david.diariodemisviajes;

public class Constantes {

    public interface Direcciones {
        String HTTP = "http://";
        String URL_VIAJES = "/viajes/obtenerviajes.php";
        String URL_VIAJE_ID = "/viajes/obtenerviajeporid.php";
        String URL_ACTUALIZAR = "/viajes/actualizar.php";
        String URL_ELIMINAR = "/viajes/eliminar.php";
        String URL_INSERTAR = "/viajes/crearviaje.php";
    }

    private static final String URL_BASE = "10.0.2.2"; // ip local

    public static final String GET = Direcciones.HTTP + URL_BASE  + Direcciones.URL_VIAJES;
    public static final String GET_ID = Direcciones.HTTP + URL_BASE + Direcciones.URL_VIAJE_ID;
    public static final String UPDATE = Direcciones.HTTP + URL_BASE  + Direcciones.URL_ACTUALIZAR;
    public static final String DELETE = Direcciones.HTTP + URL_BASE + Direcciones.URL_ELIMINAR;
    public static final String INSERT = Direcciones.HTTP + URL_BASE + Direcciones.URL_INSERTAR;

    public static final int ID_DETALLE = 1;

    public static final int ID_ACTUALIZACION = 2;

    public static final int CODIGO_PETICION = 3;

    public static final int EXITO = 100;

    public static final String ID_VIAJE = "identificador";
}
