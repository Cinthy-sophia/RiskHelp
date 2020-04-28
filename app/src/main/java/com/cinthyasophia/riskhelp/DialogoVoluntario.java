package com.cinthyasophia.riskhelp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.fragments.FragmentPrincipal;

public class DialogoVoluntario  extends DialogFragment{
    ToggleButton toggleButton;
    DialogInterface.OnClickListener listenerSi;
    DialogInterface.OnClickListener listenerNo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_voluntario, null);
        builder.setView(layout);
        listenerSi = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"El usuario es voluntario",Toast.LENGTH_LONG).show();
                iniciarFragmentPrincipal();
            }
        };
        listenerNo = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(),"El usuario no es voluntario",Toast.LENGTH_LONG).show();
                iniciarFragmentPrincipal();
            }
        };
        builder.setPositiveButton(R.string.yes,listenerSi);
        builder.setNegativeButton(R.string.no,listenerNo);

        return builder.create();
    }
    public void iniciarFragmentPrincipal(){
        Bundle b = new Bundle();
        Fragment fPrincipal = new FragmentPrincipal();
        fPrincipal.setArguments(b);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal,fPrincipal).commit();
    }
}
