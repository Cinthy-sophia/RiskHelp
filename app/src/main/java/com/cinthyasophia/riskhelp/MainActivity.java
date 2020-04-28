package com.cinthyasophia.riskhelp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.cinthyasophia.riskhelp.fragments.FragmentInicio;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarFragmentPrincipal();

    }
    /**
     * Inicia el fragment principal
     *
     */
    public void iniciarFragmentPrincipal(){
        Bundle b = new Bundle();
        Fragment fPrincipal = new FragmentInicio();
        fPrincipal.setArguments(b);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal,fPrincipal).commit();
    }

}
