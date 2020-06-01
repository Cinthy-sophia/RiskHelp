package com.cinthyasophia.riskhelp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cinthyasophia.riskhelp.R;
import com.cinthyasophia.riskhelp.dialogos.DialogoCambioPassword;
import com.cinthyasophia.riskhelp.dialogos.DialogoGrupoVoluntario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class FragmentAjustes extends Fragment {
    private static final int PICK_IMAGE = 100;
    Button bCambiarContra;
    Button bCambiarFoto;
    ImageView ivFotoActual;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ajustes,container, false);
        bCambiarContra = view.findViewById(R.id.bCambiarContra);
        bCambiarFoto = view.findViewById(R.id.bCambiarFoto);
        ivFotoActual = view.findViewById(R.id.ivFotoActual);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ivFotoActual.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        bCambiarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        bCambiarContra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Dialogo para cambiar la contraseña
                DialogoCambioPassword dialogoCambioPassword = new DialogoCambioPassword();
                dialogoCambioPassword.show(getActivity().getSupportFragmentManager(), "error_dialog_cambio_password");

            }
        });

    }

    /**
     * Lanza un intent para abrir la galería, y que el usuario seleccione una foto
     */
    private void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        gallery.setType("image/");
        startActivityForResult(gallery, PICK_IMAGE);
    }

    /**
     * Según el resultCode recibido, guarda la imagen recibida por medio del intent,
     * y guarda la ruta de la imagen en el perfil del usuario actual.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Uri imageUri;
        if(resultCode == Activity.RESULT_OK ){
            imageUri = data.getData();
            ivFotoActual.setImageURI(imageUri);
            Bitmap imagen = ((BitmapDrawable) ivFotoActual.getDrawable()).getBitmap();

            //Recibe la ruta una vez guardada la imagen
            String ruta = guardarImagen(getContext(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), imagen);

            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(Uri.parse(ruta))
                    .build();

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("INFO", "FOTO GUARDADA.");
                                Log.d("INFO", user.getPhotoUrl().toString());
                                Toast.makeText(getContext(),"Foto cambiada con éxito!", Toast.LENGTH_SHORT);
                            }
                        }
                    });

        }
    }

    /**
     * Recibe los datos necesarios para guardar la imagen en la memoria interna del dispositivo,
     * y regresa la ruta en la que ha sido creada
     * @param context
     * @param nombre
     * @param imagen
     * @return
     */
    private String guardarImagen (Context context, String nombre, Bitmap imagen){
        //Creamos un contextwrapper con la memoria interna de la aplicación
        ContextWrapper cw = new ContextWrapper(context);
        //Obtenemos el directorio a partir del contextwrapper, y creamos una carpeta llamada FotosPerfil privada
        File dirImages = cw.getDir("FotosPerfil", Context.MODE_PRIVATE);
        //Creamos la ruta de nuestra imagen, usando la del directorio anterior, y el nombre de nuestra imagen y su extensión .png
        File myPath = new File(dirImages, nombre + ".png");
        FileOutputStream fos = null;
        try{
            fos = new FileOutputStream(myPath);
            //Elegimos un formato de compresión, un int que indique la calidad de la imagen, y el objeto FileOutputStream creado
            imagen.compress(Bitmap.CompressFormat.JPEG, 10, fos);
            //Limpiamos el buffer
            fos.flush();
        }catch (FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
        //Devolvemos la ruta de nuestra imagen ya guardada
        return myPath.getAbsolutePath();
    }

}
