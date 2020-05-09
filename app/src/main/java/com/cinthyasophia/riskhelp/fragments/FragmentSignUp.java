package com.cinthyasophia.riskhelp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FragmentSignUp extends Fragment {
    private final int PASSWORD_MIN_SIZE = 6;
    private TextView tvMessage;
    private TextView tvApellidoODireccion;
    private TextInputEditText tfNombre;
    private TextInputEditText tfApellidoODireccion;
    private TextInputEditText tfTelefono;
    private TextInputEditText tfCodigoPostal;
    private TextInputEditText tfEmail;
    private TextInputEditText tfPassword;
    private Button bSiguiente;
    private String tipoUsuario;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        tipoUsuario= getArguments().getString("tipoUsuario");

        //Text Views
        //Dependiendo del tipo de usuario(Usuario regular/grupo voluntario), tendrán un contenido u otro
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
                if (tfNombre.getText().toString().isEmpty() || tfApellidoODireccion.getText().toString().isEmpty()
                || tfCodigoPostal.getText().toString().isEmpty() || tfTelefono.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),R.string.empty_field,Toast.LENGTH_LONG).show();

                }else if(tfEmail.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),R.string.email_error,Toast.LENGTH_LONG).show();

                }else if(!tfEmail.getText().toString().contains("@")) {
                    Toast.makeText(getContext(), R.string.email_error, Toast.LENGTH_LONG).show();

                }else if (tfPassword.getText().toString().isEmpty() || tfPassword.getText().length() < PASSWORD_MIN_SIZE ){
                    Toast.makeText(getContext(),R.string.password_error,Toast.LENGTH_LONG).show();

                }else{

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(tfEmail.getText().toString(),tfPassword.getText().toString())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Snackbar snack = Snackbar.make(getView(), R.string.sign_up_failure, Snackbar.LENGTH_INDEFINITE);
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
                            })
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    FirebaseUser user = authResult.getUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(tfNombre.getText().toString())
                                            //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("INFO", "User profile updated.");
                                                    }
                                                }
                                            });
                                    iniciarActivityPrincipal();
                                }
                            });



                }
                tfEmail.setText("");
                tfNombre.setText("");
                tfApellidoODireccion.setText("");
                tfTelefono.setText("");
                tfCodigoPostal.setText("");
                tfPassword.setText("");
            }


        });

    }
    public void iniciarActivityPrincipal(){
        Bundle b = new Bundle();
        Intent i = new Intent(getContext(),PrincipalActivity.class);
        startActivity(i);
        getActivity().finish();
    }

}
