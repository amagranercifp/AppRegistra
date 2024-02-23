package com.example.appregistra.adapters;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appregistra.R;
import com.example.appregistra.datos.Tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TareasRecyclerViewAdapter extends RecyclerView.Adapter<TareasRecyclerViewAdapter.ViewHolder>{

    public ArrayList<Tarea> practicaList;

    public TareasRecyclerViewAdapter(ArrayList<Tarea> practicaList){
        this.practicaList = practicaList;
    }

    @Override
    public TareasRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tarea_cardview_element, parent, false);

        return new TareasRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount(){return practicaList.size();}


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position){
        holder.bindData(practicaList.get(position));
    }

    //public void setItems(ArrayList<Practica> items){practicaList = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tvIdTarea, tvTecnico, tvMomentoInicial, tvMomentoFinal;
        CardView cardView;
        private TareasRecyclerViewAdapter adapter;

        public ViewHolder(View itemView){
            super(itemView);

            tvIdTarea = itemView.findViewById(R.id.tvIdTarea);
            tvTecnico = itemView.findViewById(R.id.tvTecnico);
            tvMomentoInicial = itemView.findViewById(R.id.tvMomentoInicial);
            tvMomentoFinal = itemView.findViewById(R.id.tvMomentoFinal);

        }

        void bindData(final Tarea card){

            tvIdTarea.setText(card.getIdTarea());
            tvTecnico.setText(card.getNombreTecnico());
            tvMomentoInicial.setText(card.getMomentoInicial());
            tvMomentoFinal.setText(card.getMomentoFinal());

        }
    }

}