package com.upbmovil.proyectofinal.datos;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.upbmovil.proyectofinal.R;
import com.upbmovil.proyectofinal.modelo.Estudiante;
import com.upbmovil.proyectofinal.modelo.Vacantes;

import java.util.ArrayList;


public class AdapterVacantes extends RecyclerView.Adapter<AdapterVacantes.ViewHolder> {
    private ArrayList<Vacantes> mDatos;

    //el
    public AdapterVacantes(ArrayList<Vacantes> myDataSet) {
        mDatos = myDataSet;
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombre,detalle;

        public ViewHolder(View view) {
            super(view);
            nombre = view.findViewById(R.id.item_nombre);
            detalle = view.findViewById(R.id.item_detalle);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(mDatos.get(position).getNombre());
        holder.detalle.setText(mDatos.get(position).getDetalle());
    }



    @Override
    public int getItemCount() {
        return mDatos.size();
    }
}
