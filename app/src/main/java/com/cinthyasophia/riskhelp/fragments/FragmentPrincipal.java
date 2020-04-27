package com.cinthyasophia.riskhelp;

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

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentPrincipal extends Fragment {
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    private Button bEmergencia;
    private Button bRegistrarse;
    private Button bIniciarSesion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        bEmergencia = container.findViewById(R.id.bEmergencia);
        bRegistrarse = container.findViewById(R.id.bRegistrarse);
        bIniciarSesion = container.findViewById(R.id.bIniciarSesion);

        bIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FirebaseAuth.getInstance().getCurrentUser() == null) {
                    // Iniciamos Activity para Login/Registro
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .build(),
                            SIGN_IN_REQUEST_CODE
                    );


                } else {
                    // El usuario ya se ha autenticado.
                    Toast.makeText(container.getContext(),
                            "Bienvenido " + FirebaseAuth.getInstance()
                                    .getCurrentUser()
                                    .getDisplayName(),
                            Toast.LENGTH_LONG)
                            .show();

                    Log.i("NOMBRE",FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName());

                    //iniciarFragmentPrincipal();
                }

            }
        });

        bRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fSignUp = new FragmentSignUp();

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal,fSignUp).commit();


            }
        });

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
