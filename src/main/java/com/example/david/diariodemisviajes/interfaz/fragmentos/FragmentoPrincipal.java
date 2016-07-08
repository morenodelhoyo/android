package com.example.david.diariodemisviajes.interfaz.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.david.diariodemisviajes.Constantes;
import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.Viaje;
import com.example.david.diariodemisviajes.ViajeSingleton;
import com.example.david.diariodemisviajes.interfaz.Adaptador;
import com.example.david.diariodemisviajes.interfaz.actividades.ActividadInsertar;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FragmentoPrincipal extends Fragment {

    private Adaptador miAdaptador;
    private RecyclerView miLista;
    private RecyclerView.LayoutManager miLayoutManager;
    private FloatingActionButton miBotonFlotante;
    private Gson miGson = new Gson();

    public FragmentoPrincipal() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.principal, container, false);

        // Obtenemos la referencia a la lista:
        miLista = (RecyclerView) vista.findViewById(R.id.miRecycler);
        miLista.setHasFixedSize(true);

        // Necesitamos un administrador para el LinearLayout:
        miLayoutManager = new LinearLayoutManager(getActivity());
        miLista.setLayoutManager(miLayoutManager);

        // Llamamos al método adaptador para cargar los datos:
        adaptador();

        // Obtenemos la referencia al botón flotante:
        miBotonFlotante = (FloatingActionButton) vista.findViewById(R.id.botonFb);

        // Dejamos a la escucha:
        miBotonFlotante.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Iniciar actividad de inserción
                        getActivity().startActivityForResult(
                                new Intent(getActivity(), ActividadInsertar.class), Constantes.CODIGO_PETICION);

                    }
                }
        );
        return vista;
    }




    public void adaptador() {
        // Realizamos la petición GET como ya vimos en las guías anteriores:
        ViajeSingleton.
                getInstance(getActivity()).
                addToRequestQueue(
                        new JsonObjectRequest(
                                Request.Method.GET,
                                Constantes.GET,
                                null,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        // Usamos nuestro método procesarRespuesta
                                        procesarRespuesta(response);
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.e("Diario", error.toString());
                                    }
                                }
                        )
                );
    }

    private void procesarRespuesta(JSONObject response) {
        String mensaje = getResources().getString(R.string.error_acceso_viajes);
        String estado;
        JSONArray viajesJson;
        Viaje[] viajes;
        try {
            estado = response.getString("estado");
            switch (estado) {
                case "1": // hemos tenido éxito (recuerda los estados que usmos en PHP)
                    viajesJson = response.getJSONArray("viajes");
                    // Parseamos con el objeto Gson
                    viajes = miGson.fromJson(viajesJson.toString(), Viaje[].class);
                    // Inicializamos el adaptador
                    miAdaptador = new Adaptador(Arrays.asList(viajes), getActivity());
                    // Asignamos el adaptador a la lista
                    miLista.setAdapter(miAdaptador);
                    break;
                case "2": // Algo ha fallado ;(
                    Toast.makeText(getActivity(), mensaje, Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (JSONException e) {
            //Log o lanzar la excepción
        }
    }
}
