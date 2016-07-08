package com.example.david.diariodemisviajes.interfaz.actividades;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoConfirmar;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoDatePicker;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoInsertar;

public class ActividadInsertar extends AppCompatActivity
        implements FragmentoDatePicker.OnDateSelectedListener,
        FragmentoConfirmar.ConfirmDialogListener {

    final String FRAGMENTO_INSERTAR = FragmentoInsertar.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_done);
        // Si no existe una instancia, la creamos:
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new FragmentoInsertar(), FRAGMENTO_INSERTAR)
                    .commit();
        }

    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        FragmentoInsertar fragmentoInsertar = (FragmentoInsertar)
                getSupportFragmentManager().findFragmentByTag(FRAGMENTO_INSERTAR);

        if (fragmentoInsertar != null) {
            finish(); // Finalizar actividad descartando cambios
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        FragmentoInsertar fragmentoInsertar = (FragmentoInsertar)
                getSupportFragmentManager().findFragmentByTag(FRAGMENTO_INSERTAR);

        if (fragmentoInsertar != null) {
            // No se quieren descartar los cambios
        }
    }

    @Override
    public void onDateSelected(int year, int month, int day) {
        FragmentoInsertar fragmentoInsertar = (FragmentoInsertar)
                getSupportFragmentManager().findFragmentByTag(FRAGMENTO_INSERTAR);

        if (fragmentoInsertar != null) {
            fragmentoInsertar.actualizarFecha(year, month, day);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



}
