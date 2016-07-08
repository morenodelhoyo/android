package com.example.david.diariodemisviajes.interfaz;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.diariodemisviajes.R;
import com.example.david.diariodemisviajes.Viaje;
import com.example.david.diariodemisviajes.interfaz.actividades.ActividadDetalle;

import java.util.List;

public class Adaptador extends RecyclerView.Adapter<Adaptador.ViajeViewHolder>
        implements ItemClickListener {

    private List<Viaje> listaViajes;

    private Context context;

    public Adaptador(List<Viaje> items, Context context) {
        this.context = context;
        this.listaViajes = items;
    }

    @Override
    public ViajeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.viaje, parent, false);
        return new ViajeViewHolder(vista, this);
    }

    @Override
    public void onBindViewHolder(ViajeViewHolder holder, int position) {
        holder.titulo.setText(listaViajes.get(position).getTitulo());
        holder.fecha.setText(listaViajes.get(position).getFecha());
        holder.categoria.setText(listaViajes.get(position).getCategoria());
    }

    @Override
    public int getItemCount() {
        return listaViajes.size();
    }

    @Override
    public void onItemClick(View view, int position) {
        ActividadDetalle.lanzarActividad((Activity) context, listaViajes.get(position).getIdentificador());
    }

    public static class ViajeViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        public TextView titulo;
        public TextView fecha;
        public TextView categoria;
        public ItemClickListener listener;

        public ViajeViewHolder(View v, ItemClickListener listener) {
            super(v);
            titulo = (TextView) v.findViewById(R.id.titulo);
            fecha = (TextView) v.findViewById(R.id.fecha);
            categoria = (TextView) v.findViewById(R.id.categoria);
            this.listener = listener;
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClick(v, getAdapterPosition());
        }
    }
}

interface ItemClickListener {
    void onItemClick(View view, int position);
}
