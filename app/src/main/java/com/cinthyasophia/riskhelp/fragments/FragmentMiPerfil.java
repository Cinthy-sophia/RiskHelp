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
    private TextView tvCodigoPostal;
    private TextView tvTelefono;
    private TextView tvEmail;
    private FirebaseFirestore database;
    private String email;//Con el email busco el usuario o grupo voluntario y muestro los datos necesarios
    private Usuario usuario;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mi_perfil, container, false);
        database = FirebaseFirestore.getInstance();
        email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        ivFotoPerfil = view.findViewById(R.id.ivFotoPerfil);//todo colocar la imagen segun la uri indicada
        tvNombre = view.findViewById(R.id.tvNombre);
        tvCodigoPostal = view.findViewById(R.id.tvCodigoPostal);
        tvTelefono = view.findViewById(R.id.tvTelefono);
        tvEmail = view.findViewById(R.id.tvEmail);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Con el correo obtengo el usuario o grupo y lo guardo en el objeto, según el tipo de usuario recibido mostrara los datos
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
                        }
                    }
                }
            }
        });


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

}
