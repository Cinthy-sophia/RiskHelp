package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentMiPerfil extends Fragment {
    private ImageView ivFotoPerfil;
    private TextView tvNombre;
    private TextView tvCodigoPostal;
    private TextView tvTelefono;
    private TextView tvEmail;
    private FirebaseFirestore database;
    private String email;
    private Usuario usuario;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        database = FirebaseFirestore.getInstance();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);
        tvNombre = view.findViewById(R.id.tvNombre);
        tvCodigoPostal = view.findViewById(R.id.tvCodigoPostal);
        tvTelefono = view.findViewById(R.id.tvTelefono);
        tvEmail = view.findViewById(R.id.tvEmail);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getContext(),"Cargando...",Toast.LENGTH_SHORT).show();

        //Con el email selecciono el usuario correcto, y luego cargo los TextView con los datos
        CollectionReference coleccion = database.collection("usuarios");
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document: task.getResult()) {
                        usuario = document.toObject(Usuario.class);
                        if(usuario.getEmail().equalsIgnoreCase(email)){
                            tvNombre.setText(usuario.getNombre());
                            tvEmail.setText(usuario.getEmail());
                            tvTelefono.setText(String.valueOf(usuario.getTelefono()));
                            tvCodigoPostal.setText(String.valueOf(usuario.getCodigoPostal()));
                            ivFotoPerfil.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
                        }
                    }
                }
            }
        });


    }

}
