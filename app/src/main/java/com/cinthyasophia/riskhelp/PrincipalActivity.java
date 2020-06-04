package com.cinthyasophia.riskhelp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cinthyasophia.riskhelp.fragments.FragmentAjustes;
import com.cinthyasophia.riskhelp.fragments.FragmentAlertas;
import com.cinthyasophia.riskhelp.fragments.FragmentMiPerfil;
import com.cinthyasophia.riskhelp.fragments.FragmentNuevaAlerta;
import com.cinthyasophia.riskhelp.modelos.Alerta;
import com.cinthyasophia.riskhelp.modelos.Usuario;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAlertaListener{

    private TextView tvNombreUsuario;
    private TextView tvEmailUsuario;
    private ImageView ivFotoUsuario;
    private FragmentAlertas fragment;
    private FirebaseFirestore database;
    private String tipoUsuario;
    private DrawerLayout drawer;
    private CollectionReference coleccion;
    private FloatingActionButton fab;
    private static final String CHANNEL_ID = "idcanal";
    private NotificationCompat.Builder mBuilder;
    private NotificationManager notificationManager;
    private Toolbar toolbar;
    private static final int ID_ALERTA_NOTIFICACION =0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            Intent i = new Intent(PrincipalActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }else{

            toolbar = findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();


            NavigationView navigationView = findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            tvNombreUsuario = header.findViewById(R.id.tvNombreUsuario);
            tvEmailUsuario = header.findViewById(R.id.tvEmailUsuario);
            ivFotoUsuario = header.findViewById(R.id.ivFotoUsuario);
            fab = findViewById(R.id.fab);
            fab.hide();
            notificationManager = getSystemService(NotificationManager.class);

            try {
                //Paramos el hilo principal por cierta cantidad de tiempo para que la aplicacion
                //tenga tiempo de recibir y cargar los datos correctamente
                Toast.makeText(this,"Cargando...",Toast.LENGTH_SHORT).show();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                tvNombreUsuario.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                tvEmailUsuario.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                if (FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()==null){
                    //Si el usuario registrado no tiene una foto indicada, se le designa una predeterminada
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setPhotoUri(Uri.parse("drawable/fireman_profile.png"))
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
                }
                ivFotoUsuario.setImageURI(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());

            }

            database = FirebaseFirestore.getInstance();
            coleccion = database.collection("usuarios");
            obtenerTipoUsuario();


            //Esta query se utiliza para que el listener, en caso de que se añada una nueva alerta, lanze la alerta necesaria.
            Query nuevaAlertaQuery = database.collection("alertas");
            nuevaAlertaQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    Alerta alerta;
                    if (e != null) { //si hay una excepción
                        Log.d("CHANGE ALERTA", "Listen failed.", e);
                        return;
                    }

                    if (queryDocumentSnapshots != null) { //si no hay excepción, y hay datos que analizar
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            alerta=document.toObject(Alerta.class);
                            if (isUsuarioCorrecto(alerta)){
                                crearCanalNotificacion();
                                crearNotificacionBarra();
                                setActividad();
                                notificationManager.notify(ID_ALERTA_NOTIFICACION,mBuilder.build());
                            }
                            Log.d("CHANGE ALERTA", "Current data: " +alerta.getDescripcion());
                        }

                    } else {
                        Log.d("CHANGE ALERTA", "Current data: null");
                    }
                }
            });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iniciarFragmentNuevaAlerta();
                }
            });

            navigationView.setNavigationItemSelectedListener(this);

        }



    }

    /**
     * Se buscará el usuario actual entre los usuarios registrados, si este está marcado como voluntario, el tipo de usuario será
     * Grupo Voluntario, sino, sera Usuario.
     */
    public void obtenerTipoUsuario(){
        //coleccion = database.collection("usuarios");
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Usuario user;
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("LOS USUARIOS", document.getId() + " => " + document.getData());
                        Log.d("USER ACTUAL", FirebaseAuth.getInstance().getCurrentUser().getEmail());

                        user =document.toObject(Usuario.class);
                        if (user.getEmail().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            if (user.isVoluntario()){
                                tipoUsuario = "GRUPO_VOLUNTARIO";
                            }else{
                                tipoUsuario = "USUARIO";
                                fab.show();
                            }

                        }


                    }

                }
            }

        });

    }

    /**
     * Inicia el FragmentNuevaAlerta para que el usuario pueda mandar una alerta.
     */
    public void iniciarFragmentNuevaAlerta(){
        FragmentNuevaAlerta fragmentNuevaAlerta = new FragmentNuevaAlerta();
        fab.hide();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragmentNuevaAlerta).addToBackStack(null).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.miAjustes:
                FragmentAjustes fragmentAjustes = new FragmentAjustes();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragmentAjustes).addToBackStack(null).commit();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Bundle b = new Bundle();
        b.putString("tipoUsuario",tipoUsuario);
        fragment = new FragmentAlertas();
        //Según que item del Navigation View se indique, se iniciará un fragment u otro
        if (id == R.id.nav_alertas) {
            b.putString("ALERTAS", "Mi texto");
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();
            toolbar.setTitle(R.string.menu_alerts);
        } else if (id == R.id.nav_alertas_tomadas) {
            b.putString("ALERTAS_TOMADAS", "Mi texto");
            toolbar.setTitle(R.string.menu_taken_alerts);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();
        } else if (id == R.id.nav_alertas_no_tomadas) {
            b.putString("ALERTAS_NO_TOMADAS", "Mi texto");
            fragment.setArguments(b);
            toolbar.setTitle(R.string.menu_untaken_alerts);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();

        }else if (id == R.id.nav_mi_perfil) {
            b.putString("MI PERFIL", "Mi texto");
            FragmentMiPerfil fragmentMiPerfil = new FragmentMiPerfil();
            fragmentMiPerfil.setArguments(b);
            toolbar.setTitle("");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragmentMiPerfil).addToBackStack(null).commit();

        } else if (id == R.id.nav_log_out){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent i = new Intent(PrincipalActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        }else{
            toolbar.setTitle("");
        }
        fragment.setListener(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }



    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else if (getFragmentManager().getBackStackEntryCount() > 0){
            getFragmentManager().popBackStackImmediate();
        }else{
            super.onBackPressed();
            if(tipoUsuario!=null && tipoUsuario.equalsIgnoreCase("USUARIO")){
                fab.show();
            }

        }
    }

    /**
     * Abre Google Maps con la direccion recibida.
     * @param direccion
     */
    public void abrirMapa(String direccion) {
        String map = "http://maps.google.com/maps?q=" + direccion;
        // Donde direccion es la variable que contiene el string del textview
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
        startActivity(i);

        /*String uri = String.format(Locale.ENGLISH, "google.navigation:q=%1$s", direccion);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);*/
    }

    /**
     * En caso de que se haga click en la alerta.
     * @param alerta
     * @param direccion
     */
    @Override
    public void onAlertaClicked(final Alerta alerta, String direccion) {
        Log.d("ALERTA", "Se ha clickeado la alerta: "+alerta.getDescripcion());
        coleccion = database.collection("alertas");
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("LOS DATOS", document.getId() + " => " + document.getData());
                        Log.d("ALERTA", document.getId() + " => " + alerta.getId());

                        if (alerta.getId().equalsIgnoreCase(document.getId())){
                            //Si el id del documento corresponde con el de la alerta seleccionada
                            //se actualiza el campo, para que aparezca como "tomada"
                            coleccion.document(document.getId()).update("tomada", true);
                        }
                    }
                } else {
                    Log.d("LOS DATOS", "Error getting documents: ", task.getException());
                }

            }
        });
        //Luego de actualizar la alerta se abre Google maps para mostrar la dirección indicada en la alerta
        abrirMapa(direccion);
    }

    /**
     * En caso de que la version del SDK sea menor o igual a la O, entonces
     * se creará un canal para enviar la notificación.
     **/
    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence nombre = "Mi canal";
            String descripcion = "Mi canal de notificación ";
            int importancia = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel (CHANNEL_ID, nombre,
                    importancia);
            channel.setDescription(descripcion);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Crea la notificación con todos sus elementos.
     */
    private void crearNotificacionBarra() {
        mBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        mBuilder.setSmallIcon(R.drawable.ic_reports);
        mBuilder.setContentTitle("¡NUEVA ALERTA!");
        mBuilder.setContentText("Tienes una nueva alerta ¡Tiempo de ayudar!");
        mBuilder.setTicker("¡Atencion!");
        mBuilder.setVibrate(new long[] {100, 250, 100, 500});
        mBuilder.setAutoCancel(true);
    }

    /**
     * Prepara la actividad y el intent para crear la notificación.
     */
    private void setActividad() {
        Intent intent = new Intent (getApplicationContext(), PrincipalActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent);
    }

    /**
     * Recibe la alerta nueva y segun los usuarios registrados en la base de datos
     * comprobará si es voluntario y es el grupo mencionado en la alerta en cuestion e indicará si es correcto o no.
     * @param nuevaAlerta
     * @return
     */
    public boolean isUsuarioCorrecto(final Alerta nuevaAlerta){
        final boolean[] correct = new boolean[1];
        coleccion.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Usuario u;
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        u = document.toObject(Usuario.class);

                        if (FirebaseAuth.getInstance() != null && u.isVoluntario()&& u.getNombre().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                                && u.getEmail().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                                && u.getNombre().equalsIgnoreCase(nuevaAlerta.getGrupo())){
                            correct[0] = true;
                        }else{
                            correct[0] = false;
                        }


                    }
                }
            }
        });
        return correct[0];
    }
}
