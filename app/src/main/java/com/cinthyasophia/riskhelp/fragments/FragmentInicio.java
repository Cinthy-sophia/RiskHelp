package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentInicio extends Fragment {
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    private Button bEmergencia;
    private Button bRegistro;
    private Button bIniciarSesion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            // El usuario ya se ha autenticado.
            Toast.makeText(getContext(),
                    "Bienvenido " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

            Log.i("NOMBRE",FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getDisplayName());

            iniciarFragmentInicio();
        }*/

        Toast.makeText(getContext(),
                "Bienvenido ",
                Toast.LENGTH_LONG)
                .show();

        return inflater.inflate(R.layout.fragment_inicial,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bEmergencia = getView().findViewById(R.id.bEmergencia);
        bRegistro = getView().findViewById(R.id.bRegistro);
        bIniciarSesion = getView().findViewById(R.id.bIniciarSesion);

        Snackbar snack = Snackbar.make(getView(), R.string.emergency_message, Snackbar.LENGTH_INDEFINITE);
        snack.setAction("OK", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Respond to the click, such as by undoing the modification that caused
                // this message to be displayed
                Toast.makeText(getContext(),"You got it.",Toast.LENGTH_SHORT).show();
            }
        });
        snack.show();

        bIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword("k","k").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //iniciarFragmentPrincipal
                    }
                });
                /*if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    // Iniciamos Activity para Login/Registro

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .build(),
                            SIGN_IN_REQUEST_CODE
                    );


                } else {
                    // El usuario ya se ha autenticado.
                    Toast.makeText(getContext(),
                            "Bienvenido " + FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getDisplayName(),
                            Toast.LENGTH_LONG)
                            .show();

                    Log.i("NOMBRE",FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName());

                    iniciarFragmentPrincipal();
                }*/

            }
        });

        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fSignUp = new FragmentSignUp();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal,fSignUp).addToBackStack(null).commit();

            }
        });
    }
}
