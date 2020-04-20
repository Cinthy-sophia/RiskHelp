package com.cinthyasophia.riskhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private static final int SIGN_IN_REQUEST_CODE = 1001;
    private Button bEmergencia;
    private Button bRegistrarse;
    private Button bIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        bEmergencia = findViewById(R.id.bEmergencia);
        bRegistrarse = findViewById(R.id.bRegistrarse);
        bIniciarSesion = findViewById(R.id.bIniciarSesion);

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
                    Toast.makeText(MainActivity.this,
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


            }
        });



    }
}
