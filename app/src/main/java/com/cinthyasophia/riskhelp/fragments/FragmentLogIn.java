package com.cinthyasophia.riskhelp.fragments;

import android.content.Intent;
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

import com.cinthyasophia.riskhelp.MainActivity;
import com.cinthyasophia.riskhelp.PrincipalActivity;
import com.cinthyasophia.riskhelp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogIn extends Fragment {
    private final int PASSWORD_MIN_SIZE = 6;
    private String tipoUsuario;
    private TextView tvMessageLogIn;
    private Button bEntrar;
    private TextInputEditText tfEmail;
    private TextInputEditText tfPassword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in,container,false);
        bEntrar = view.findViewById(R.id.bEntrar);
        tfEmail = view.findViewById(R.id.tfEmail);
        tfPassword = view.findViewById(R.id.tfPassword);
        tipoUsuario = getArguments().getString("tipoUsuario");
        tvMessageLogIn = view.findViewById(R.id.tvMessage);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (tipoUsuario.equalsIgnoreCase("USUARIO")){
            tvMessageLogIn.setText(R.string.log_in_message_user);
        }else if(tipoUsuario.equalsIgnoreCase("GRUPO_VOLUNTARIO")){
            tvMessageLogIn.setText(R.string.log_in_message_volunteer);
        }
        bEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tfEmail.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),R.string.email_empty,Toast.LENGTH_LONG).show();
                }else if(!tfEmail.getText().toString().contains("@")){
                    Toast.makeText(getContext(),R.string.email_error,Toast.LENGTH_LONG).show();
                }else if (tfPassword.getText().toString().isEmpty() || tfPassword.getText().length() < PASSWORD_MIN_SIZE ){
                    Toast.makeText(getContext(),R.string.password_error,Toast.LENGTH_LONG).show();

                }else{
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(tfEmail.getText().toString(),tfPassword.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    iniciarActivityPrincipal();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar snack = Snackbar.make(getView(), R.string.log_in_failure, Snackbar.LENGTH_INDEFINITE);
                                    snack.setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // Respond to the click, such as by undoing the modification that caused
                                            // this message to be displayed
                                            //Toast.makeText(getContext(),"OK",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    snack.show();
                                }

                            });
                }
            }
        });

    }
    public void iniciarActivityPrincipal(){
        Intent i = new Intent(getContext(), PrincipalActivity.class);
        i.putExtra("tipoUsuario",tipoUsuario);
        startActivity(i);
        getActivity().finish();
    }
}
