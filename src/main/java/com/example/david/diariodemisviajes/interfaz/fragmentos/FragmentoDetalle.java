package com.example.david.diariodemisviajes.interfaz.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.david.diariodemisviajes.Constantes;
import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.Viaje;
import com.example.david.diariodemisviajes.ViajeSingleton;
import com.example.david.diariodemisviajes.interfaz.actividades.ActividadActualizar;
import com.example.david.diariodemisviajes.interfaz.actividades.ActividadInsertar;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FragmentoDetalle extends Fragment {

    private ImageView cabecera;
    private TextView titulo;
    private ImageButton botonEditar;
    private TextView descripcion;
    private TextView fecha;
    private TextView categoria;
    private String identificador;
    private Gson gson = new Gson();

    public FragmentoDetalle() {

    }

    public static FragmentoDetalle crearInstancia(String idMeta) {
        FragmentoDetalle fragmentoDetalle = new FragmentoDetalle();
        Bundle bundle = new Bundle();
        bundle.putString(Constantes.ID_VIAJE, idMeta);
        fragmentoDetalle.setArguments(bundle);
        return fragmentoDetalle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.detalle, container, false);

        // Obtención de views
        cabecera = (ImageView) vista.findViewById(R.id.cabecera);
        titulo = (TextView) vista.findViewById(R.id.titulo);
        descripcion = (TextView) vista.findViewById(R.id.descripcion);
        fecha = (TextView) vista.findViewById(R.id.fecha);
        botonEditar = (ImageButton) vista.findViewById(R.id.botonEditar);
        categoria = (TextView) vista.findViewById(R.id.categoria);

        // Obtener extra del intent de envío
        identificador = getArguments().getString(Constantes.ID_VIAJE);

        // Setear escucha para el fab
        botonEditar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Iniciamos la actividad de actualización del viaje
                        Intent i = new Intent(getActivity(), ActividadActualizar.class);
                        i.putExtra(Constantes.ID_VIAJE, identificador);
                        getActivity().startActivityForResult(i, Constantes.ID_ACTUALIZACION);
                    }
                }
        );
        // Cargar datos desde el web service
        adaptador();
        return vista;
    }

    public void adaptador() {
        // Añadimos el identificador a la URL (nos viene como extra en el Intent):
        String URL_GET_VIAJE = Constantes.GET_ID + "?idViaje=" + identificador;
        // Realizamos la petición Volley con la URL creada
        ViajeSingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        URL_GET_VIAJE,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesamos la respuesta obtenida:
                                procesarRespuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Posible error de Volley
                                Log.e("Diario", "Error en Volley: "+error.getMessage());
                            }
                        }
                )
        );
    }

    private void procesarRespuesta(JSONObject response) {
        String estado;
        String mensaje;
        JSONObject objeto;
        Viaje viaje;
        try {
            // Obtenemos el atributo "mensaje"
            estado = response.getString("estado");
            // incluimos el mensaje de error por si el estado no es 1
            mensaje = getResources().getString(R.string.error_detalle);
            if(estado.compareTo("2") == 0){
                mensaje = getResources().getString(R.string.error_registro);;
            }
            if(estado.compareTo("3") == 0){
                mensaje = getResources().getString(R.string.error_identificador);;
            }

            switch (estado) {
                case "1": // Hemos tenido éxito :)
                    objeto = response.getJSONObject("viaje");
                    viaje = gson.fromJson(objeto.toString(), Viaje.class);
                    // Asignamos el color del fondo a la cabecera
                    switch (viaje.getCategoria()) {
                        case "Placer":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.placer));
                            break;
                        case "Negocios":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.negocios));
                            break;
                        case "Salud":
                            cabecera.setBackgroundColor(getResources().getColor(R.color.salud));
                            break;
                    }
                    titulo.setText(viaje.getTitulo());
                    descripcion.setText(viaje.getDescripcion());
                    fecha.setText(viaje.getFecha());
                    categoria.setText(viaje.getCategoria());
                    break;
                case "2": // No hemos encontrado el viaje :(
                    Toast.makeText(getActivity(), mensaje,Toast.LENGTH_LONG).show();
                    break;
                case "3": // ¿Y el identificador? ;((
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
