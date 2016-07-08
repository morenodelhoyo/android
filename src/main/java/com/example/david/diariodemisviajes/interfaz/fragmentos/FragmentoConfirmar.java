package com.example.david.diariodemisviajes.interfaz.fragmentos;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import com.example.david.diariodemisviajes.R;

public class FragmentoConfirmar extends DialogFragment {

    ConfirmDialogListener miListener;

    public interface ConfirmDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    private static final String NOMBRE_DIALOGO = "dialogo";

    public static DialogFragment crearInstancia(String extra) {
        DialogFragment fragment = new FragmentoConfirmar();
        Bundle bundle = new Bundle();
        bundle.putString(NOMBRE_DIALOGO, extra);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage(getArguments().getString(NOMBRE_DIALOGO))
                .setPositiveButton(R.string.dialogo_aceptar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        miListener.onDialogPositiveClick(FragmentoConfirmar.this);
                    }
                })
                .setNegativeButton(R.string.dialogo_cancelar, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        miListener.onDialogNegativeClick(FragmentoConfirmar.this);
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            miListener = (ConfirmDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " Debes implementar la interfaz ConfirmDialogListener");
        }
    }
}
