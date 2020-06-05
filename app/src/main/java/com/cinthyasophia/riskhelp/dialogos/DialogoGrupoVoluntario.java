package com.cinthyasophia.riskhelp.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.cinthyasophia.riskhelp.PrincipalActivity;
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

        //Se obtiene en el query los usuarios marcados como voluntarios y que tengan el mismo codigo postal que el de la nueva alerta
        Query query = coleccion.whereEqualTo("codigoPostal", nuevaAlerta.getCodigo_postal()).whereEqualTo("voluntario",true);


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
        builder.setTitle(R.string.select_volunteer_group);

        return builder.create();

    }

    //En caso de que se cierre el dialogo muestra el toast indicado
    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Toast.makeText(getActivity(),R.string.back_button,Toast.LENGTH_LONG).show();
        super.onDismiss(dialog);
    }

    /**
     * Se obtienen todos los usuarios voluntarios y se guardan en un ArrayList como grupos voluntarios.
     */
    public void obtenerGrupos(){
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Usuario grupo;
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        grupo = document.toObject(Usuario.class);
                        if (grupo.isVoluntario()){
                            gruposVoluntarios.add(grupo);
                            Log.d("LOS DATOS", document.getId() + " => " + document.getData());
                        }
                    }
                    if (gruposVoluntarios.size()==0){
                        Toast.makeText(getActivity(),R.string.group_error,Toast.LENGTH_LONG).show();
                        dismiss();

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
    private void crearNuevaAlerta(final Alerta alerta, Usuario itemAtPosition){
        alerta.setGrupo(itemAtPosition.getNombre());
        final CollectionReference coleccion = database.collection("alertas");
        coleccion.add(alerta)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Si ha sido correcto, actualiza la alerta para que se guarde con su id, y muestra un mensaje de exito
                        coleccion.document(documentReference.getId()).update("id", documentReference.getId());

                        //Toast.makeText(getActivity(),R.string.new_alert_correct,Toast.LENGTH_LONG).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si algo ha fallado
                        //Toast.makeText(getActivity(),R.string.new_alert_error,Toast.LENGTH_LONG).show();
                    }
                });
    }

}
