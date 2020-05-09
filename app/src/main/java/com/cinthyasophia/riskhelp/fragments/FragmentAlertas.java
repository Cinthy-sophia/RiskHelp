package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.AlertaAdapter;
import com.cinthyasophia.riskhelp.IAlertaListener;
import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.CollationElementIterator;
import java.util.ArrayList;

public class FragmentAlertas extends Fragment {
    private ArrayList<Alerta> alertas;
    private RecyclerView rvAlertas;
    private AlertaAdapter adapter;
    private FirebaseFirestore database;
    private CollectionReference coleccion;
    private IAlertaListener listener;
    private String tipoUsuario;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alertas, container, false);
        alertas = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        rvAlertas = view.findViewById(R.id.rvAlertas);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        coleccion = database.collection("alertas");
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    Alerta a;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("LAS ALERTAS", document.getId() + " => " + document.getData());

                        a=document.toObject(Alerta.class);
                        alertas.add(a);
                    }
                }
            }
        });
        Bundle b = getArguments();
        if (b!=null){
            tipoUsuario = b.getString("tipoUsuario");
            if(tipoUsuario.equalsIgnoreCase("GRUPO_VOLUNTARIO")){
                for (Alerta alerta: alertas) {
                    if (!alerta.getGrupo().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                        alertas.remove(alerta);
                    }
                    if(b.containsKey("ALERTAS_NO_TOMADAS")){
                        if(alerta.isTomada()){
                           alertas.remove(alerta);
                        }
                    }else if(b.containsKey("ALERTAS_TOMADAS")) {
                        if(!alerta.isTomada()){
                            alertas.remove(alerta);
                        }
                    }
                }
            }else if (tipoUsuario.equalsIgnoreCase("USUARIO")){
                for (Alerta alerta: alertas) {
                    if (!alerta.getDenunciante().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                        alertas.remove(alerta);
                    }
                    if(b.containsKey("ALERTAS_NO_TOMADAS")){
                        if(alerta.isTomada()){
                            alertas.remove(alerta);
                        }
                    }else if(b.containsKey("ALERTAS_TOMADAS")) {
                        if(!alerta.isTomada()){
                            alertas.remove(alerta);
                        }
                    }
                }

            }else if(b.containsKey("CERRAR_SESION")){


            }
        }
        adapter = new AlertaAdapter(tipoUsuario,alertas,listener);
        rvAlertas.setAdapter(adapter);
        rvAlertas.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));

    }

    public void setListener(IAlertaListener listener){
        this.listener=listener;
    }

}
