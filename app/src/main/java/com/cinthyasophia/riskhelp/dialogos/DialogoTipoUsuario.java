package com.cinthyasophia.riskhelp.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.fragments.FragmentLogIn;
import com.cinthyasophia.riskhelp.fragments.FragmentSignUp;

public class DialogoTipoUsuario extends DialogFragment{
    private DialogInterface.OnClickListener listenerUsuarioRegular;
    private DialogInterface.OnClickListener listenerGrupoVoluntario;
    private TextView tvDialogo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final String fragment = getArguments().getString("FRAGMENT");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_tipo_usuario, null);
        builder.setView(layout);
        tvDialogo = layout.findViewById(R.id.tvDialogo);

        //Segun el tipo de fragment que se desee iniciar el Dialogo mostrará un mensaje u otro.
        switch (fragment){
            case "LOG_IN":
                tvDialogo.setText(R.string.dialog_log_in_message);
                break;
            case "SIGN_UP":
                tvDialogo.setText(R.string.dialog_sign_up_message);
                break;
            default:
                break;
        }

        //Listener en caso de que el usuario haga click en el botón de "Usuario Regular"
        listenerUsuarioRegular = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Si el usuario es un usuario regular segun si desea iniciar sesion o registrarse,
                // los metodos cargan el fragment para cada caso
                if(fragment.equalsIgnoreCase("LOG_IN")){
                    iniciarFragmentLogIn("USUARIO");

                }else if(fragment.equalsIgnoreCase("SIGN_UP")){
                    iniciarFragmentSignUp("USUARIO");

                }
            }
        };

        //Listener en caso de que el usuario haga click en el botón de "Grupo Voluntario"
        listenerGrupoVoluntario = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Si el usuario es un usuario regular segun si desea iniciar sesion o registrarse,
                // los metodos cargan el fragment para cada caso
                if(fragment.equalsIgnoreCase("LOG_IN")){
                    iniciarFragmentLogIn("GRUPO_VOLUNTARIO");

                }else if(fragment.equalsIgnoreCase("SIGN_UP")){
                    iniciarFragmentSignUp("GRUPO_VOLUNTARIO");
                }
            }
        };
        builder.setPositiveButton(R.string.regular_user, listenerUsuarioRegular);
        builder.setNegativeButton( R.string.volunteer_group, listenerGrupoVoluntario);

        return builder.create();
    }

    /**
     * Inicia el fragment para registrarse en la aplicación, y le envia como argumento el tipo de usuario
     * @param tipoUsuario
     */
    public void iniciarFragmentSignUp(String tipoUsuario){
        Bundle b = new Bundle();
        Fragment fSignUp = new FragmentSignUp();
        b.putString("tipoUsuario",tipoUsuario);
        fSignUp.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inicio,fSignUp).addToBackStack(null).commit();
    }

    /**
     * Inicia el fragment para iniciar sesion en la aplicación, y le envia como argumento el tipo de usuario
     * @param tipoUsuario
     */
    public void iniciarFragmentLogIn(String tipoUsuario){
        Bundle b = new Bundle();
        Fragment fLogIn = new FragmentLogIn();
        b.putString("tipoUsuario",tipoUsuario);
        fLogIn.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inicio,fLogIn).addToBackStack(null).commit();
    }

}
