package com.example.david.diariodemisviajes.interfaz.actividades;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;

import com.example.david.diariodemisviajes.Constantes;
import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoActualizar;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoConfirmar;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoDatePicker;

public class ActividadActualizar extends AppCompatActivity
        implements FragmentoDatePicker.OnDateSelectedListener,
        FragmentoConfirmar.ConfirmDialogListener {

    final String FRAGMENTO_ACTUALIZAR = FragmentoActualizar.class.getSimpleName();
    final String NOMBRE_DIALOGO = "dialogo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String extraInfoViaje = getIntent().getStringExtra(Constantes.ID_VIAJE);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_done);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, FragmentoActualizar.crearInstancia(extraInfoViaje), FRAGMENTO_ACTUALIZAR)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        FragmentoActualizar fragmentoActualizar = (FragmentoActualizar)
                getSupportFragmentManager().findFragmentByTag(FRAGMENTO_ACTUALIZAR);

        if (fragmentoActualizar != null) {
            fragmentoActualizar.actualizarFecha(year, month, day);
        }
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FragmentoActualizar fragmentoActualizar = (FragmentoActualizar)
                getSupportFragmentManager().findFragmentByTag(FRAGMENTO_ACTUALIZAR);

        if (fragmentoActualizar != null) {
            String estraInfoDialogo = dialog.getArguments().getString(NOMBRE_DIALOGO);
            String cadena = getResources().
                    getString(R.string.dialogo_eliminar);

            if (estraInfoDialogo.compareTo(cadena) == 0) {
                fragmentoActualizar.eliminarViaje(); // Eliminar el viaje
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        FragmentoActualizar fragmentoActualizar = (FragmentoActualizar)
                getSupportFragmentManager().findFragmentByTag(FRAGMENTO_ACTUALIZAR);
        if (fragmentoActualizar != null) {
            // No deseamos hacer nada
        }
    }

}
