package com.cinthyasophia.riskhelp.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.cinthyasophia.riskhelp.modelos.GrupoVoluntario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DialogoGrupoVoluntario extends DialogFragment {
    private ArrayAdapter adaptador;
    private ArrayList<String> items;
    private ArrayList<GrupoVoluntario> grupos;
    private FirebaseFirestore database;
    private ListView listGrupos;
    private Alerta nuevaAlerta;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_grupo_voluntario, null);
        builder.setView(layout);
        nuevaAlerta = (Alerta) getArguments().getSerializable("nuevaAlerta");
        database = FirebaseFirestore.getInstance();
        items = new ArrayList<>();
        grupos= obtenerGrupos(nuevaAlerta.getCodigoPostal());
        listGrupos = layout.findViewById(R.id.listGrupos);
        adaptador = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, items);
        cargarDatos();
        listGrupos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                crearNuevaAlerta(nuevaAlerta, (GrupoVoluntario) listGrupos.getItemAtPosition(position));

                //getDialog().dismiss();
            }
        });

        return builder.create();

    }

    private void cargarDatos() {
        adaptador.clear();
        for (GrupoVoluntario grupo : grupos){
            adaptador.insert(grupo.getNombre(),adaptador.getCount());
        }
        adaptador.notifyDataSetChanged();

    }

    /**
     * Obtiene los grupos que tengan el mismo codigo postal indicado por el usuario al crear una
     * nueva alerta.
     * @param codigoPostal
     * @return
     */
    private ArrayList<GrupoVoluntario> obtenerGrupos(int codigoPostal){
        final ArrayList<GrupoVoluntario> grupos = new ArrayList<>();
        CollectionReference coleccion = database.collection("grupos_voluntarios");
        coleccion.whereEqualTo("codigo_postal",codigoPostal).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                GrupoVoluntario g;
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("LOS DATOS", document.getId() + " => " + document.getData());
                        g=document.toObject(GrupoVoluntario.class);
                        grupos.add(g);
                    }
                }
            }
        });
        return grupos;
    }
    private void crearNuevaAlerta(Alerta alerta, GrupoVoluntario itemAtPosition){
        alerta.setGrupo(itemAtPosition.getNombre());
        CollectionReference coleccion = database.collection("alertas");
        coleccion.add(alerta)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Si ha sido correcto muestra un mensaje de exito
                        Snackbar snack = Snackbar.make(getView(), "La alerta ha sido creada con éxito.", Snackbar.LENGTH_INDEFINITE);
                        snack.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Respond to the click, such as by undoing the modification that caused
                                // this message to be displayed
                                Toast.makeText(getContext(),"OK",Toast.LENGTH_SHORT).show();
                            }
                        });
                        snack.show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si algo ha fallado
                        Snackbar snack = Snackbar.make(getView(), "La alerta NO ha sido creada con éxito. Intente de nuevo.", Snackbar.LENGTH_INDEFINITE);
                        snack.setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Respond to the click, such as by undoing the modification that caused
                                // this message to be displayed
                                Toast.makeText(getContext(),"OK",Toast.LENGTH_SHORT).show();
                            }
                        });
                        snack.show();
                    }
                });
    }
}
