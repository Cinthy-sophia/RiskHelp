package com.cinthyasophia.riskhelp.dialogos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.cinthyasophia.riskhelp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DialogoCambioPassword extends DialogFragment {
    private static final int PASSWORD_MIN_SIZE = 6;
    TextInputEditText tfPassword;
    Button bCambiar;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialogo_cambio_password, null);
        builder.setView(layout);
        tfPassword = layout.findViewById(R.id.tfPassword);
        bCambiar = layout.findViewById(R.id.bCambiar);


        bCambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tfPassword.getText().toString().isEmpty() || tfPassword.getText().length() < PASSWORD_MIN_SIZE ){
                    Toast.makeText(getContext(),R.string.password_error,Toast.LENGTH_LONG).show();

                }else{
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    user.updatePassword(tfPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("INFO", "User password updated.");
                                        dismiss();
                                    }else{
                                        Log.d("INFO", "Error.");
                                    }
                                }
                            });
                    }
                }

        });

        return builder.create();
    }
}
