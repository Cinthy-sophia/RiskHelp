package com.cinthyasophia.riskhelp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.IAlertaListener;
import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.Date;

public class AlertaAdapter extends FirestoreRecyclerAdapter<Alerta,AlertaAdapter.AlertaViewHolder> {
    private String tipoUsuario;
    private IAlertaListener listener;


    public AlertaAdapter(FirestoreRecyclerOptions<Alerta> options, String tipoUsuario, IAlertaListener listener) {
        super(options);
        this.tipoUsuario = tipoUsuario;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlertaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alerta_item,parent,false);

        return new AlertaViewHolder(view, tipoUsuario, listener);//listener
    }


    @Override
    protected void onBindViewHolder(@NonNull AlertaViewHolder alertaViewHolder, int i, @NonNull Alerta alerta) {
        alertaViewHolder.setAlerta(alerta);
        alertaViewHolder.tvNombre.setText(alerta.getDenunciante());
        alertaViewHolder.tvDescripcion.setText(alerta.getDescripcion());
        alertaViewHolder.tvDireccion.setText(alerta.getDireccion());
        alertaViewHolder.tvTelefono.setText(alerta.getTelefono());
        alertaViewHolder.tvGrupoV.setText(alerta.getGrupo());
        alertaViewHolder.tvFechaHora.setText(DateFormat.format("dd/MM/yyyy HH:mm", new Date(alerta.getFechaHora())));
        if (alerta.isTomada()){
            alertaViewHolder.tvNombre.setTypeface(Typeface.DEFAULT);
            alertaViewHolder.tvDescripcion.setTypeface(Typeface.DEFAULT);
            alertaViewHolder.tvDireccion.setTypeface(Typeface.DEFAULT);
            alertaViewHolder.tvTelefono.setTypeface(Typeface.DEFAULT);
            alertaViewHolder.tvGrupoV.setTypeface(Typeface.DEFAULT);
            alertaViewHolder.tvFechaHora.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onDataChanged() {
        super.onDataChanged();
        notifyDataSetChanged();


    }

    public static class AlertaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private String tipoUsuario;
        private IAlertaListener listener;
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvDireccion;
        private TextView tvTelefono;
        private TextView tvGrupoV;
        private TextView tvFechaHora;
        private Alerta alerta;

        public AlertaViewHolder(@NonNull View itemView, String tipoUsuario, IAlertaListener listener) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvDireccion = itemView.findViewById(R.id.tvDirección);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvGrupoV = itemView.findViewById(R.id.tvGrupoV);
            tvFechaHora = itemView.findViewById(R.id.tvHoraFecha);
            this.tipoUsuario = tipoUsuario;
            this.listener = listener;
            itemView.setOnClickListener(this);
        }
        protected void setAlerta(Alerta a){
            this.alerta = a;
        }

        @Override
        public void onClick(View v) {
            if(listener!=null){
                //Si el usuario actual está marcado como voluntario se ejecuta el metodo de click de la alerta
                if ("GRUPO_VOLUNTARIO".equals(tipoUsuario) && alerta!=null) {
                    listener.onAlertaClicked(alerta,tvDireccion.getText().toString());
                }

            }
        }
    }
}
