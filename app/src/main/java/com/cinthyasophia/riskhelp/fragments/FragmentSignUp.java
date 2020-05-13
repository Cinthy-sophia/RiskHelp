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
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.cinthyasophia.riskhelp.modelos.GrupoVoluntario;
import com.cinthyasophia.riskhelp.modelos.Usuario;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FragmentSignUp extends Fragment {
    private final int PASSWORD_MIN_SIZE = 6;
    private TextView tvMessage;
    private TextInputEditText tfNombre;
    private TextInputEditText tfTelefono;
    private TextInputEditText tfCodigoPostal;
    private TextInputEditText tfEmail;
    private TextInputEditText tfPassword;
    private boolean voluntario;
    private Button bSiguiente;
    private String tipoUsuario;
    private FirebaseFirestore database;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up,container,false);
        tipoUsuario= getArguments().getString("tipoUsuario");

        //Text Views
        //Dependiendo del tipo de usuario(Usuario regular/grupo voluntario), tendrán un contenido u otro
        tvMessage = view.findViewById(R.id.tvMessage);

        //Text Fields
        tfNombre = view.findViewById(R.id.tfNombre);
        tfTelefono = view.findViewById(R.id.tfTelefono);
        tfCodigoPostal = view.findViewById(R.id.tfCodigoPostal);
        tfEmail = view.findViewById(R.id.tfEmail);
        tfPassword = view.findViewById(R.id.tfPassword);
        //Button
        bSiguiente = view.findViewById(R.id.bSiguiente);

        database = FirebaseFirestore.getInstance();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //Segun el tipo de usuario que desee ingresar se adaptaran los datos del fragment
        switch (tipoUsuario){
            case "USUARIO":
                tvMessage.setText(R.string.sign_up_user);
                break;
            case "GRUPO_VOLUNTARIO":
                tvMessage.setText(R.string.sign_up_volunteer);
                break;
            default:

                break;
        }


        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tfNombre.getText().toString().isEmpty() || tfCodigoPostal.getText().toString().isEmpty() || tfTelefono.getText().toString().isEmpty()){
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
                                    //Si algo ha fallado
                                    Log.e("ERROR",e.getMessage());
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
                                    //Si ha ido bien, según el tipo de usuario que haya recibido, marcará
                                    // el nuevo usuario como voluntario o no
                                    switch (tipoUsuario){
                                        case "USUARIO":
                                            voluntario = false;
                                            break;
                                        case "GRUPO_VOLUNTARIO":
                                            voluntario = true;
                                            break;
                                        default:

                                            break;
                                    }
                                    Usuario nuevoUsuario = new Usuario(tfNombre.getText().toString(),tfEmail.getText().toString(),tfTelefono.getText().toString(),Integer.parseInt(tfCodigoPostal.getText().toString()),voluntario);

                                    //Actualizo el usuario registrado en la aplicación para que tenga el mismo nombre
                                    // que el usuario registrado en la base de datos
                                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(nuevoUsuario.getNombre())
                                            //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                            .build();

                                    user.updateProfile(profileUpdates)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("INFO", "User profile updated.");
                                                        Log.d("INFO", user.getDisplayName());
                                                    }
                                                }
                                            });

                                    tfEmail.setText("");
                                    tfNombre.setText("");
                                    tfTelefono.setText("");
                                    tfCodigoPostal.setText("");
                                    tfPassword.setText("");
                                    crearNuevoUsuario(nuevoUsuario);
                                    iniciarActivityPrincipal();
                                }
                            });

                }
            }


        });

    }

    /**
     * Guarda el nuevo usuario recibido en la base de datos.
     * @param usuario
     */
    private void crearNuevoUsuario(Usuario usuario){

        CollectionReference coleccion = database.collection("usuarios");
        coleccion.add(usuario)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        //Si ha sido correcto muestra un mensaje de exito

                        Toast.makeText(getContext(),"Nuevo usuario creado correctamente.",Toast.LENGTH_LONG).show();


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Si algo ha fallado
                        Toast.makeText(getActivity(),"El usuario no ha podido ser creado.",Toast.LENGTH_LONG).show();
                    }
                });
    }

    /**
     * Inicia la ActivityPrincipal.
     */
    public void iniciarActivityPrincipal(){
        Intent i = new Intent(getContext(),PrincipalActivity.class);
        startActivity(i);
        getActivity().finish();
    }

}
