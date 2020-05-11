package com.cinthyasophia.riskhelp.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.adapters.GrupoAdapter;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.cinthyasophia.riskhelp.modelos.Usuario;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DialogoGrupoVoluntario extends DialogFragment {
    private GrupoAdapter adapter;
    private ArrayList<Usuario> gruposVoluntarios;
    private FirebaseFirestore database;
    private RecyclerView rvGrupos;
    private Alerta nuevaAlerta;
    private FirestoreRecyclerOptions<Usuario> options;
    private CollectionReference coleccion;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_grupo_voluntario, null);
        builder.setView(layout);

        nuevaAlerta = (Alerta) getArguments().getSerializable("nuevaAlerta");//Recibe la nueva alerta creada en el fragment anterior.
        database = FirebaseFirestore.getInstance();
        gruposVoluntarios = new ArrayList<>();
        rvGrupos = layout.findViewById(R.id.rvGrupos);
        coleccion = database.collection("usuarios");

        obtenerGrupos();
        if (gruposVoluntarios.size()!=0){
            Toast.makeText(getActivity(),"Lo sentimos pero no hay grupos disponibles en tu zona, te pedimos que llames a los numeros de emergencia: 112,012",Toast.LENGTH_LONG).show();
            dismiss();
        }

        Query query = coleccion.whereEqualTo("codigo_postal",nuevaAlerta.getCodigo_postal()).whereEqualTo("voluntario",true);
        options = new FirestoreRecyclerOptions.Builder<Usuario>()
                .setQuery(query,Usuario.class)
                .build();
        adapter = new GrupoAdapter(options);
        rvGrupos.setAdapter(adapter);
        rvGrupos.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        rvGrupos.addItemDecoration(dividerItemDecoration);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crearNuevaAlerta(nuevaAlerta, gruposVoluntarios.get(rvGrupos.getChildAdapterPosition(v)));
                dismiss();
            }

        });

        adapter.startListening();
        builder.setTitle(R.string.select_volunteer_group+":");

        return builder.create();

    }

    public void obtenerGrupos(){
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Usuario grupo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("LOS DATOS", document.getId() + " => " + document.getData());
                        grupo = document.toObject(Usuario.class);
                        if (grupo.isVoluntario()){
                            gruposVoluntarios.add(grupo);
                        }
                    }
                } else {
                    Log.d("LOS DATOS", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    /**
     * Guarda la nueva alerta creada en la base de datos.
     * @param alerta
     * @param itemAtPosition
     */
    private void crearNuevaAlerta(Alerta alerta, Usuario itemAtPosition){
        alerta.setGrupo(itemAtPosition.getNombre());
        CollectionReference coleccion = database.collection("alertas");
        coleccion.add(alerta)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Si ha sido correcto muestra un mensaje de exito

                        Toast.makeText(getActivity(),R.string.new_alert_correct,Toast.LENGTH_LONG).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si algo ha fallado
                        Toast.makeText(getActivity(),R.string.new_alert_error,Toast.LENGTH_LONG).show();
                    }
                });
    }

}
