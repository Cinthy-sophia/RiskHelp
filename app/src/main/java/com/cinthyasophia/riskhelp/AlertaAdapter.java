package com.cinthyasophia.riskhelp;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.modelos.Alerta;

import java.util.ArrayList;

public class AlertaAdapter extends RecyclerView.Adapter<AlertaAdapter.AlertaViewHolder> {
    private String tipoUsuario;
    private ArrayList<Alerta> alertas;
    private IAlertaListener listener;


    public AlertaAdapter( String tipoUsuario, ArrayList<Alerta> alertas, IAlertaListener listener) {
        this.tipoUsuario = tipoUsuario;
        this.alertas = alertas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alerta_item,parent,false);
        return new AlertaViewHolder(view, tipoUsuario, listener);//listener
    }

    @Override
    public void onBindViewHolder(@NonNull AlertaViewHolder holder, int position) {
        Alerta alerta = alertas.get(position);
        holder.bindItem(alerta);
    }

    @Override
    public int getItemCount() {
        return alertas.size();
    }

    public static class AlertaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private String tipoUsuario;
        private IAlertaListener listener;
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvDireccion;
        private TextView tvTelefono;
        private TextView tvGrupoV;

        public AlertaViewHolder(@NonNull View itemView, String tipoUsuario, IAlertaListener listener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvDireccion = itemView.findViewById(R.id.tvDirección);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvGrupoV = itemView.findViewById(R.id.tvGrupoV);
            this.tipoUsuario = tipoUsuario;
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        public void bindItem(Alerta alerta) {
            tvNombre.setText(alerta.getDenunciante());
            tvDescripcion.setText(alerta.getDescripcion());
            tvDireccion.setText(alerta.getDireccion());
            tvTelefono.setText(alerta.getTelefono());
            tvGrupoV.setText(alerta.getGrupo());

        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                if ("GRUPO_VOLUNTARIO".equals(tipoUsuario)) {
                    listener.onAlertaClicked(getAdapterPosition(),tvDireccion.getText().toString());
                    tvNombre.setTypeface(Typeface.DEFAULT);
                    tvDescripcion.setTypeface(Typeface.DEFAULT);
                    tvDireccion.setTypeface(Typeface.DEFAULT);
                    tvTelefono.setTypeface(Typeface.DEFAULT);
                    tvGrupoV.setTypeface(Typeface.DEFAULT);
                }

            }
        }
    }
}
