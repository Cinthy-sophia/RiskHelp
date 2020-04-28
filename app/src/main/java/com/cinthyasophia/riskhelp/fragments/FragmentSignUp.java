package com.cinthyasophia.riskhelp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.DialogoVoluntario;
import com.cinthyasophia.riskhelp.R;
import com.google.android.material.textfield.TextInputEditText;

public class FragmentSignUp extends Fragment {
    private TextInputEditText tfName;
    private TextInputEditText tfPassword;
    private Button bSiguiente;
    private String correo;
    private String password;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        /**bIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                correo = String.valueOf(tfCorreoE.getText());
                password = String.valueOf(tfPassword.getText());
                //Aqui compruebo que los datos ingresados son correctos.
                //Si son correctos ingresa a su correspondiente perfil (Sea una persona civil, o un grupo voluntario)


            }
        });**/
        return inflater.inflate(R.layout.fragment_sign_up,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bSiguiente = getView().findViewById(R.id.bSiguiente);
        bSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogoVoluntario dialogoVoluntario = new DialogoVoluntario();
                dialogoVoluntario.show(getActivity().getSupportFragmentManager(), "error_dialog_mapview");
            }
        });
    }
}
