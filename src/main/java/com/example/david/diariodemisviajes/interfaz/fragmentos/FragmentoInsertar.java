package com.example.david.diariodemisviajes.interfaz.fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.example.david.diariodemisviajes.ViajeSingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FragmentoInsertar extends Fragment {

    private EditText titulo;
    private EditText descripcion;
    private TextView fecha;
    private Spinner categoria;
    private TextInputLayout tilTitulo;
    private TextInputLayout tilDescripcion;

    // Constructor ------------
    public FragmentoInsertar(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Indicamos que el fragmento tendrá un menú con opciones
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
                        DialogFragment miDatePicker = new FragmentoDatePicker();
                        miDatePicker.show(getFragmentManager(), "datePicker");
                    }
                }
        );

        return miVista;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.removeItem(R.id.eliminar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:// confirmar los cambios
                switch (camposVacios()){
                    case 0:
                        almacenarViaje();
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

            case R.id.cancelar:// cancelar
                if (camposVacios() == 0)
                    mostrarDialogo();
                else
                    getActivity().finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void almacenarViaje() {

        // Obtener los valores actuales:
        final String tituloViaje = titulo.getText().toString();
        final String descripcionViaje = descripcion.getText().toString();
        final String fechaViaje = fecha.getText().toString();
        final String categoriaViaje = categoria.getSelectedItem().toString();

        //Creamos un mapa donde almacenar los valores que posteriormente pasamos a JSON
        HashMap<String, String> miMapa = new HashMap<String, String>();

        // Añadimos los datos:
        miMapa.put("titulo", tituloViaje);
        miMapa.put("descripcion", descripcionViaje);
        miMapa.put("fecha", fechaViaje);
        miMapa.put("categoria", categoriaViaje);
        miMapa.put("imagen", "");

        // Creamos un nuevo objeto Json basado en el mapa
        JSONObject jsonViajes = new JSONObject(miMapa);

        // Llamamos a insertar:
        ViajeSingleton.getInstance(getActivity()).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.POST,
                        Constantes.INSERT,
                        jsonViajes,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                respuesta(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("Diario", "Error: " + error.getMessage());
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

    private void respuesta(JSONObject response) {
        String mensaje = getResources().getString(R.string.error_crear_viaje);
        String estado;
        try {
            // Obtenemos el estado
            estado = response.getString("estado");
            // Obtenemos el mensaje
            if(estado.compareTo("1") == 0) {
                mensaje = getResources().getString(R.string.ok_crear);
            }
            switch (estado) {
                case "1": // Ha ido bien, acabamos la actividad con éxito
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    // Enviar código de éxito a la actividad
                    getActivity().setResult(Activity.RESULT_OK);
                    // Terminar la actividad mediante finish
                    getActivity().finish();
                    break;
                case "2":
                    // Ha ocurrido algun problema
                    Toast.makeText(
                            getActivity(),
                            mensaje,
                            Toast.LENGTH_LONG).show();
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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


    public void actualizarFecha(int ano, int mes, int dia) {
        fecha.setText(ano + "-" + (mes + 1) + "-" + dia);
    }

    public void mostrarDialogo() {
        DialogFragment dialogo = FragmentoConfirmar.
                crearInstancia(
                        getResources().
                                getString(R.string.dialogo_descartar));
        dialogo.show(getFragmentManager(), "Dialogo Confirmacion");
    }


}
