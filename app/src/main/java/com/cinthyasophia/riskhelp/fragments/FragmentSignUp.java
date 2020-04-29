package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentSignUp extends Fragment {
    private TextView tvMessage;
    private TextView tvApellidoODireccion;
    private TextInputEditText tfNombre;
    private TextInputEditText tfApellidoODireccion;
    private TextInputEditText tfTelefono;
    private TextInputEditText tfCodigoPostal;
    private TextInputEditText tfEmail;
    private TextInputEditText tfPassword;
    private Button bSiguiente;
    String tipoUsuario;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        tipoUsuario= getArguments().getString("tipoUsuario");
        //Text Views
        tvMessage = view.findViewById(R.id.tvMessage);
        tvApellidoODireccion = view.findViewById(R.id.tvApellidoODireccion);
        //Text Fields
        tfNombre = view.findViewById(R.id.tfNombre);
        tfApellidoODireccion = view.findViewById(R.id.tfApellidoODireccion);
        tfTelefono = view.findViewById(R.id.tfTelefono);
        tfCodigoPostal = view.findViewById(R.id.tfCodigoPostal);
        tfEmail = view.findViewById(R.id.tfEmail);
        tfPassword = view.findViewById(R.id.tfPassword);
        //Button
        bSiguiente = view.findViewById(R.id.bSiguiente);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Segun el tipo de usuario que desee ingresar se adaptaran los datos del fragment
        switch (tipoUsuario){
            case "USUARIO":
                tvMessage.setText(R.string.sign_up_user);
                tvApellidoODireccion.setText(R.string.last_name);
                break;
            case "GRUPO_VOLUNTARIO":
                tvMessage.setText(R.string.sign_up_volunteer);
                tvApellidoODireccion.setText(R.string.address);
                break;
            default:

                break;
        }

        bSiguiente.setOnClickListener(new View.OnClickListener() {
        @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(tfEmail.getText().toString(),tfPassword.getText().toString())
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                iniciarFragmentPrincipal();
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(getContext(),"HA SIDO CORRECTO.",Toast.LENGTH_LONG);
                            }
                        });


            }
        });

    }
    public void iniciarFragmentPrincipal(){
        Bundle b = new Bundle();
        Fragment fPrincipal = new FragmentPrincipal();
        fPrincipal.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal,fPrincipal).commit();
    }
}
