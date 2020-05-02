package com.cinthyasophia.riskhelp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.PrincipalActivity;
import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.dialogos.DialogoTipoUsuario;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentInicio extends Fragment {
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    private Button bEmergencia;
    private Button bRegistro;
    private Button bIniciarSesion;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio,container,false);

        bEmergencia = view.findViewById(R.id.bEmergencia);
        bRegistro = view.findViewById(R.id.bRegistro);
        bIniciarSesion = view.findViewById(R.id.bIniciarSesion);
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {//todo cambiar el signo del null a != para que acceda al fragment principal directamente
            // El usuario que ya se ha autenticado, ingresa directamente a la ventana principal de la aplicacion
            Toast.makeText(getContext(),
                    "Bienvenido " + FirebaseAuth.getInstance()
                            .getCurrentUser()
                            .getDisplayName(),
                    Toast.LENGTH_LONG)
                    .show();

           /* Log.i("NOMBRE",FirebaseAuth.getInstance()
                    .getCurrentUser()
                    .getDisplayName());*/

            iniciarFragmentPrincipal(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),FirebaseAuth.getInstance().getCurrentUser().getEmail());

        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Snackbar snack = Snackbar.make(getView(), R.string.emergency_message, Snackbar.LENGTH_INDEFINITE);
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

        bEmergencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo iniciar fragment emergencia
            }
        });

        bIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fLogIn= new FragmentLogIn();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inicio,fLogIn).addToBackStack(null).commit();

            }
        });

        bRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoTipoUsuario dialogoTipoUsuario = new DialogoTipoUsuario();
                dialogoTipoUsuario.show(getActivity().getSupportFragmentManager(), "error_dialog_mapview");
            }
        });
    }

    /**
     * Recibe el nombre y el email del usuario para enviarlos al PrincipalActivity, y mostrarlos
     * @param nombreU
     * @param emailU
     */
    public void iniciarFragmentPrincipal(String nombreU,String emailU){
        Intent i = new Intent(getContext(), PrincipalActivity.class);
        i.putExtra("nombreUsuario",nombreU);
        i.putExtra("emailUsuario",emailU);
        startActivity(i);
    }
}
