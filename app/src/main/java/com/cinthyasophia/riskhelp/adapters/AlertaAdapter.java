package com.cinthyasophia.riskhelp.adapters;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.IAlertaListener;
import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

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
        alertaViewHolder.tvNombre.setText(alerta.getDenunciante());
        alertaViewHolder.tvDescripcion.setText(alerta.getDescripcion());
        alertaViewHolder.tvDireccion.setText(alerta.getDireccion());
        alertaViewHolder.tvTelefono.setText(alerta.getTelefono());
        alertaViewHolder.tvGrupoV.setText(alerta.getGrupo());
        alertaViewHolder.tvFechaHora.setText(alerta.getFechaHora());
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if ("GRUPO_VOLUNTARIO".equals(tipoUsuario)){
            //todo que lanze la notificación push de que hay una nueva alerta
        }

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
                    tvFechaHora.setTypeface(Typeface.DEFAULT);
                }

            }
        }
    }
}
