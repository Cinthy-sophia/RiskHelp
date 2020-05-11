package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cinthyasophia.riskhelp.adapters.AlertaAdapter;
import com.cinthyasophia.riskhelp.IAlertaListener;
import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

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
        Query query = coleccion.orderBy("grupo");
        
        Bundle b = getArguments();
        if (b!=null){
            tipoUsuario = b.getString("tipoUsuario");
            String nombreUsuario = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

            if(tipoUsuario.equalsIgnoreCase("GRUPO_VOLUNTARIO")){

                if(b.containsKey("ALERTAS_NO_TOMADAS")){
                    query = coleccion.whereEqualTo("tomada",false).whereEqualTo("grupo",nombreUsuario);
                }else if(b.containsKey("ALERTAS_TOMADAS")) {
                    query = coleccion.whereEqualTo("tomada",true).whereEqualTo("grupo",nombreUsuario);
                }else{
                    query = coleccion.whereEqualTo("grupo",nombreUsuario);

                }
            }else if (tipoUsuario.equalsIgnoreCase("USUARIO")){

                if(b.containsKey("ALERTAS_NO_TOMADAS")){
                    query = coleccion.whereEqualTo("tomada",false).whereEqualTo("denunciante",nombreUsuario);
                }else if(b.containsKey("ALERTAS_TOMADAS")) {
                    query = coleccion.whereEqualTo("tomada",true).whereEqualTo("denunciante",nombreUsuario);
                }else{
                    query = coleccion.whereEqualTo("denunciante",nombreUsuario);

                }

            }
        }
        //Ordenamos las alertas en funcion de la hora en la que se reciben.
        query.orderBy("fechaHora", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Alerta> options = new FirestoreRecyclerOptions.Builder<Alerta>()
                .setQuery(query,Alerta.class)
                .build();

        adapter = new AlertaAdapter(options,tipoUsuario,listener);
        rvAlertas.setAdapter(adapter);
        rvAlertas.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));

        Log.d("ALERTAG", "Start listeninng");
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (adapter != null){
            Log.d("ALERTAG", "Stop listeninng");
            adapter.stopListening();


        }
    }

    public void setListener(IAlertaListener listener){
        this.listener=listener;
    }

}
