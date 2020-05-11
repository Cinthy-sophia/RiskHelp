package com.cinthyasophia.riskhelp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.dialogos.DialogoGrupoVoluntario;
import com.cinthyasophia.riskhelp.modelos.GrupoVoluntario;
import com.cinthyasophia.riskhelp.modelos.Usuario;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class GrupoAdapter extends FirestoreRecyclerAdapter<Usuario, GrupoAdapter.GrupoViewHolder> implements View.OnClickListener{
    private View.OnClickListener listener;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public GrupoAdapter(@NonNull FirestoreRecyclerOptions<Usuario> options) {
        super(options);

    }

    @NonNull
    @Override
    public GrupoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.grupo_item,parent,false);
        item.setOnClickListener(this);
        return new GrupoViewHolder(item);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    @Override
    protected void onBindViewHolder(@NonNull GrupoViewHolder grupoViewHolder, int i, @NonNull Usuario grupoVoluntario) {
        grupoViewHolder.tvGrupoVoluntario.setText(grupoVoluntario.getNombre());
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    static class GrupoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvGrupoVoluntario;
        public GrupoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGrupoVoluntario = itemView.findViewById(R.id.tvGrupoVoluntario);
        }
    }
}
