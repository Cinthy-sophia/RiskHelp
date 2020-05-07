package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.GrupoVoluntario;
import com.cinthyasophia.riskhelp.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentMiPerfil extends Fragment {
    private ImageView ivFotoPerfil;
    private TextView tvNombre;
    private TextView tvApeODir;
    private TextView tvApellidoODireccion;
    private TextView tvCodigoPostal;
    private TextView tvTelefono;
    private TextView tvEmail;
    private FirebaseFirestore database;
    private String tipoUsuario;
    private String email;//Con el email busco el usuario o grupo voluntario y muestro los datos necesarios
    private Object usuario;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        tipoUsuario = getArguments().getString("tipoUsuario");
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);//todo colocar la imagen segun la uri indicada
        tvNombre = view.findViewById(R.id.tvNombre);
        tvApeODir = view.findViewById(R.id.tvApeODir);
        tvApellidoODireccion = view.findViewById(R.id.tvApellidoODireccion);
        tvCodigoPostal = view.findViewById(R.id.tvCodigoPostal);
        tvTelefono = view.findViewById(R.id.tvTelefono);
        tvEmail = view.findViewById(R.id.tvEmail);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Con el correo obtengo el usuario o grupo y lo guardo en el objeto, según el tipo de usuario recibido mostrara los datos
        if(tipoUsuario.equalsIgnoreCase("USUARIO")){
            tvApeODir.setText(R.string.last_name);
            usuario = buscarUsuarioPorEmail(email);
            if (usuario != null){
                tvNombre.setText(((Usuario) usuario).getNombre());
                tvApellidoODireccion.setText(((Usuario) usuario).getApellido());
                tvEmail.setText(((Usuario) usuario).getEmail());
                tvTelefono.setText(((Usuario) usuario).getTelefono());
                tvCodigoPostal.setText(((Usuario) usuario).getCodigoPostal());
            }

        }else if(tipoUsuario.equalsIgnoreCase("GRUPO_VOLUNTARIO")){
            tvApeODir.setText(R.string.address);
            usuario = buscarGrupoVoluntarioPorEmail();
            if (usuario != null){
                tvNombre.setText(((GrupoVoluntario) usuario).getNombre());
                tvApellidoODireccion.setText(((GrupoVoluntario) usuario).getDireccion());
                tvEmail.setText(((GrupoVoluntario) usuario).getEmail());
                tvTelefono.setText(((GrupoVoluntario) usuario).getTelefono());
                tvCodigoPostal.setText(((GrupoVoluntario) usuario).getCodigoPostal());
            }
        }

    }
    public Usuario buscarUsuarioPorEmail(String email){
        final Usuario[] u = new Usuario[1];
        CollectionReference coleccion = database.collection("usuarios");
        Query query = coleccion.whereEqualTo("email",email);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        u[0] = document.toObject(Usuario.class);
                    }
                }
            }
        });
        return u[0];
    }
    public GrupoVoluntario buscarGrupoVoluntarioPorEmail(){
        final GrupoVoluntario[] u = new GrupoVoluntario[1];
        CollectionReference coleccion = database.collection("usuarios");
        Query query = coleccion.whereEqualTo("email",email);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        u[0] = document.toObject(GrupoVoluntario.class);
                    }
                }
            }
        });
        return u[0];
    }
}
