package com.example.david.diariodemisviajes.interfaz.actividades;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.david.diariodemisviajes.Constantes;
import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoDetalle;

public class ActividadDetalle extends AppCompatActivity {

    private String extraInfoViaje = "";
    final String FRAGMENTO_DETALLE = FragmentoDetalle.class.getSimpleName();

    public static void lanzarActividad(Activity activity, String idViaje) {
        Intent intent = getIntent(activity, idViaje);
        activity.startActivityForResult(intent, Constantes.ID_DETALLE);
    }

    public static Intent getIntent(Context context, String idViaje) {
        Intent intent = new Intent(context, ActividadDetalle.class);
        intent.putExtra(Constantes.ID_VIAJE, idViaje);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            // Dehabilitar titulo de la actividad
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            // Setear ícono "X" como Up button
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_close);
        }

        if (getIntent().getStringExtra(Constantes.ID_VIAJE) != null)
            extraInfoViaje = getIntent().getStringExtra(Constantes.ID_VIAJE);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, FragmentoDetalle.crearInstancia(extraInfoViaje), FRAGMENTO_DETALLE)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constantes.ID_ACTUALIZACION) {
            if (resultCode == RESULT_OK) {
                FragmentoDetalle fragment = (FragmentoDetalle) getSupportFragmentManager().
                        findFragmentByTag(FRAGMENTO_DETALLE);
                fragment.adaptador();

                setResult(RESULT_OK); // Propagar el código para actualizar
            } else if (resultCode == Constantes.EXITO) {
                setResult(Constantes.EXITO);
                finish();
            } else {
                setResult(RESULT_CANCELED);
            }
        }
    }

}
