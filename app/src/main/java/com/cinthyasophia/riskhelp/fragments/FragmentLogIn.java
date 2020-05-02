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
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class FragmentLogIn extends Fragment {
    Button bEntrar;
    TextInputEditText tfEmail;
    TextInputEditText tfPassword;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_in,container,false);
        bEntrar = view.findViewById(R.id.bEntrar);
        tfEmail = view.findViewById(R.id.tfEmail);
        tfPassword = view.findViewById(R.id.tfPassword);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(tfEmail.getText().toString(),tfPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                iniciarFragmentPrincipal();
                            }
                        })
                        .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Snackbar snack = Snackbar.make(getView(), "No es correcto mija, intenta de nuevo.", Snackbar.LENGTH_INDEFINITE);
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
        });

    }
    public void iniciarFragmentPrincipal(){
        Bundle b = new Bundle();
        Intent i = new Intent(getContext(), PrincipalActivity.class);
        startActivity(i);
    }
}
