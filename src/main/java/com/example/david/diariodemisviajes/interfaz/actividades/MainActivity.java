package com.example.david.diariodemisviajes.interfaz.actividades;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.david.diariodemisviajes.Constantes;
import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoConfirmar;
import com.example.david.diariodemisviajes.interfaz.fragmentos.FragmentoPrincipal;

public class MainActivity extends AppCompatActivity {

    final String FRAGMENTO_PRINCIPAL = FragmentoPrincipal.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_main, new FragmentoPrincipal(),FRAGMENTO_PRINCIPAL)
                    .commit();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constantes.ID_DETALLE || requestCode == Constantes.CODIGO_PETICION) {
            if (resultCode == RESULT_OK || resultCode == Constantes.EXITO) {
                FragmentoPrincipal fragment = (FragmentoPrincipal) getSupportFragmentManager().
                        findFragmentByTag(FRAGMENTO_PRINCIPAL);
                fragment.adaptador();
            }
        }
    }

}
