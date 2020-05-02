package com.cinthyasophia.riskhelp.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.fragments.FragmentSignUp;

public class DialogoTipoUsuario extends DialogFragment{
    DialogInterface.OnClickListener listenerUsuarioRegular;
    DialogInterface.OnClickListener listenerGrupoVoluntario;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_tipo_usuario, null);
        builder.setView(layout);
        listenerUsuarioRegular = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"El usuario no es voluntario",Toast.LENGTH_LONG).show();
                iniciarFragmentSignUp("USUARIO");
            }
        };
        listenerGrupoVoluntario = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"El usuario es voluntario",Toast.LENGTH_LONG).show();
                iniciarFragmentSignUp("GRUPO_VOLUNTARIO");
            }
        };
        builder.setPositiveButton(R.string.regular_user, listenerUsuarioRegular);
        builder.setNegativeButton( R.string.volunteer_group, listenerGrupoVoluntario);

        return builder.create();
    }
    public void iniciarFragmentSignUp(String tipoUsuario){
        Bundle b = new Bundle();
        Fragment fSignUp = new FragmentSignUp();
        b.putString("tipoUsuario",tipoUsuario);
        fSignUp.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_inicio,fSignUp).addToBackStack(null).commit();
    }

}
