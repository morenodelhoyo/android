package com.example.david.diariodemisviajes.interfaz.fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentoActualizar extends Fragment {

    private TextInputLayout tilTitulo;
    private TextInputLayout tilDescripcion;

    private EditText titulo;
    private EditText descripcion;
    private TextView fecha;
    private Spinner categoria;
    private String idViaje;
    private Viaje viaje;
    private Gson gson = new Gson();

    // Constructor
    public FragmentoActualizar(){

    }

    public static Fragment crearInstancia(String extra) {
        FragmentoActualizar detailFragment = new FragmentoActualizar();
        Bundle bundle = new Bundle();
        bundle.putString(Constantes.ID_VIAJE, extra);
        detailFragment.setArguments(bundle);
        return detailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflando layout del fragmento
        View miVista = inflater.inflate(R.layout.formulario, container, false);

        // Obtención de instancias controles

        tilTitulo = (TextInputLayout) miVista.findViewById(R.id.til_titulo);
        tilDescripcion = (TextInputLayout) miVista.findViewById(R.id.til_descripcion);

        titulo = (EditText) miVista.findViewById(R.id.titulo);
        descripcion = (EditText) miVista.findViewById(R.id.descripcion);
        fecha = (TextView) miVista.findViewById(R.id.fecha);
        categoria = (Spinner) miVista.findViewById(R.id.categoria);

        fecha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment picker = new FragmentoDatePicker();
                        picker.show(getFragmentManager(), "datePicker");

                    }
                }
        );

        // Obtener valor extra
        idViaje = getArguments().getString(Constantes.ID_VIAJE);

        if (idViaje != null) {
            cargarDatos();
        }

        return miVista;
    }

    private void cargarDatos() {
        // Añadiendo idMeta como parámetro a la URL
        String newURL = Constantes.GET_ID + "?idViaje=" + idViaje;

        // Consultar el detalle de la meta
        ViajeSingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        null,
                        new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesa la respuesta GET_BY_ID
                                respuestaCargarDatos(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Diario", "Error Volley: "+error.getMessage());
                            }
                        }
                )
        );
    }

    private void respuestaCargarDatos(JSONObject response) {

        JSONObject jsonViaje;
        String estado;
        String mensaje;

        try {
            estado = response.getString("estado");
            mensaje = getResources().getString(R.string.error_detalle);

            switch (estado) {
                case "1":
                    jsonViaje = response.getJSONObject("viaje");
                    // Guardar instancia
                    viaje = gson.fromJson(jsonViaje.toString(), Viaje.class);
                    // Setear valores de la meta
                    cargarFormulario(viaje);
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Ha fallado, informamos a la actividad y finalizamos
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void cargarFormulario(Viaje viaje) {
        // Seteando valores de la respuesta
        titulo.setText(viaje.getTitulo());
        descripcion.setText(viaje.getDescripcion());
        fecha.setText(viaje.getFecha());
        // Obteniendo acceso a los array de strings para categorias y prioridades
        String[] categorias = getResources().getStringArray(R.array.categorias_viaje);
        // Obteniendo la posición del spinner categorias
        int posicion_categoria = 0;
        for (int i = 0; i < categorias.length; i++) {
            if (categorias[i].compareTo(viaje.getCategoria()) == 0) {
                posicion_categoria = i;
                break;
            }
        }
        // Setear selección del Spinner de categorías
        categoria.setSelection(posicion_categoria);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:// CONFIRMAR
                switch (camposVacios()){
                    case 0:
                        actualizarViaje();
                        break;
                    case 1:
                        // Mostrar error sobre el campo vacío.
                        tilTitulo.setError(getResources().getString(R.string.error_title_vacio));
                        break;
                    case 2:
                        // Eliminar el primer error ya que el campo está completo
                        tilTitulo.setErrorEnabled(false);
                        // Mostrar error sobre el campo vacío.
                        tilDescripcion.setError(getResources().getString(R.string.error_description_vacio));
                        break;
                }
                return true;
            case R.id.eliminar:// ELIMINAR
                mostrarDialogo(R.string.dialogo_eliminar);
                break;

            case R.id.cancelar:// DESCARTAR
                mostrarDialogo(R.string.dialogo_descartar);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void actualizarViaje() {

        // Obtener valores actuales de los controles
        final String tituloViaje = titulo.getText().toString();
        final String descripcionViaje = descripcion.getText().toString();
        final String fechaViaje = fecha.getText().toString();
        final String categoriaViaje = categoria.getSelectedItem().toString();

        HashMap<String, String> miMapa = new HashMap<String, String>();// Mapeo previo

        miMapa.put("idViaje", idViaje);
        miMapa.put("titulo", tituloViaje);
        miMapa.put("descripcion", descripcionViaje);
        miMapa.put("fecha", fechaViaje);
        miMapa.put("categoria", categoriaViaje);
        miMapa.put("imagen", ""); // no tenemos imagen :(

        // Crear nuevo objeto Json basado en el mapa
        JSONObject jsonViajes = new JSONObject(miMapa);

        // Depurando objeto Json...
        Log.d("Diario", jsonViajes.toString());

        // Actualizar datos en el servidor
        ViajeSingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.UPDATE,
                        jsonViajes,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                respustaActualizarViaje(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Diario", "Error en Volley: "+error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    private void respustaActualizarViaje(JSONObject response) {
        String estado;
        String mensaje;
        try {
            // Obtener estado
            estado = response.getString("estado");
            // Obtener mensaje
            mensaje = getResources().getString(R.string.error_actualizar);;
            if(estado.compareTo("1") == 0){
                mensaje = getResources().getString(R.string.ok_actualizar);;
            }
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    getActivity().setResult(Activity.RESULT_OK);
                    // Terminar actividad
                    getActivity().finish();
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void eliminarViaje() {

        HashMap<String, String> miMapa = new HashMap<>();// MAPEO

        miMapa.put("idViaje", idViaje);// Identificador

        JSONObject jsonViaje = new JSONObject(miMapa);// Objeto Json

        // Eliminar datos en el servidor
        ViajeSingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.DELETE,
                        jsonViaje,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta
                                respuestaEliminarViaje(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Diario", "Error en Volley: "+error.getMessage());
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );
    }

    private void respuestaEliminarViaje(JSONObject response) {

        String mensaje = getResources().getString(R.string.error_eliminar);;
        try {
            // Obtener estado
            String estado = response.getString("estado");
            // Obtener mensaje
            if(estado.compareTo("1") == 0){
                mensaje = getResources().getString(R.string.ok_eliminar);;
            }
            switch (estado) {
                case "1":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito
                    getActivity().setResult(Constantes.EXITO);
                    // Terminar actividad
                    getActivity().finish();
                    break;

                case "2":
                    // Mostrar mensaje
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de falla
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    // Terminar actividad
                    getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void actualizarFecha(int ano, int mes, int dia) {
        // Setear en el textview la fecha
        fecha.setText(ano + "-" + (mes + 1) + "-" + dia);
    }

    private void mostrarDialogo(int id) {
        DialogFragment dialogo = FragmentoConfirmar.crearInstancia(
                        getResources().getString(id));
        dialogo.show(getFragmentManager(), "ConfirmDialog");
    }

    public int camposVacios() {
        String tituloViaje = titulo.getText().toString();
        String descripcionViaje = descripcion.getText().toString();
        if(tituloViaje.isEmpty()){
            return 1;
        }
        if(descripcionViaje.isEmpty()){
            return 2;
        }
        return 0;
    }
}
