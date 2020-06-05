package com.cinthyasophia.riskhelp.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.Util.Lib;
import com.cinthyasophia.riskhelp.dialogos.DialogoGrupoVoluntario;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.GregorianCalendar;

public class FragmentNuevaAlerta extends Fragment {
    private TextInputEditText tfNombre;
    private TextInputEditText tfDescripcion;
    private TextInputEditText tfDireccion;
    private TextInputEditText tfCodigoPostal;
    private TextInputEditText tfTelefono;
    private MaterialCheckBox cBAnonimo;
    private Button bContinuar;
    private boolean anonimo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nueva_alerta, container, false);
        tfNombre = view.findViewById(R.id.tfNombre);
        tfDescripcion = view.findViewById(R.id.tfDescripcion);
        tfDireccion = view.findViewById(R.id.tfDireccion);
        tfCodigoPostal = view.findViewById(R.id.tfCodigoPostal);
        tfTelefono = view.findViewById(R.id.tfTelefono);
        cBAnonimo = view.findViewById(R.id.cBAnonimo);
        bContinuar = view.findViewById(R.id.bContinuar);


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        cBAnonimo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    tfNombre.setEnabled(false);
                    tfNombre.setText("");
                    anonimo = true;
                }else{
                    tfNombre.setEnabled(true);
                    anonimo = false;
                }
            }
        });

        bContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Si los TextFields estan vacíos se mostrará un Toast indicando el error.
                if ((!cBAnonimo.isChecked() && tfNombre.getText().toString().isEmpty()) || tfDireccion.getText().toString().isEmpty()
                        || tfDescripcion.getText().toString().isEmpty() || tfTelefono.getText().toString().isEmpty()){
                    Toast.makeText(getContext(),R.string.empty_field,Toast.LENGTH_LONG).show();

                }else{
                    //En caso contrario se creará la nueva alerta y se iniciará el DialogoGrupoVoluntario para que el usuario pueda
                    //seleccionar el grupo y se guarde la nueva alerta en la base de datos.
                    Alerta alerta = new Alerta(tfDescripcion.getText().toString(),tfDireccion.getText().toString(),anonimo,tfNombre.getText().toString(),tfTelefono.getText().toString(),null,Integer.parseInt(tfCodigoPostal.getText().toString()),new Date());
                    DialogoGrupoVoluntario dialogoGrupoVoluntario = new DialogoGrupoVoluntario();
                    Bundle fragment = new Bundle();
                    fragment.putSerializable("nuevaAlerta",alerta);
                    dialogoGrupoVoluntario.setArguments(fragment);
                    dialogoGrupoVoluntario.show(getActivity().getSupportFragmentManager(), "error_dialog");

                }
                tfNombre.setText("");
                tfDescripcion.setText("");
                tfDireccion.setText("");
                tfCodigoPostal.setText("");
                tfTelefono.setText("");
                cBAnonimo.setChecked(false);
                getActivity().getSupportFragmentManager().beginTransaction().remove(FragmentNuevaAlerta.this).commit();
            }
        });

    }

}
