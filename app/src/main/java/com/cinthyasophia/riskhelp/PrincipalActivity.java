package com.cinthyasophia.riskhelp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.cinthyasophia.riskhelp.fragments.FragmentAlertas;
import com.cinthyasophia.riskhelp.fragments.FragmentMiPerfil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IAlertaListener{

    private TextView tvNombreUsuario;
    private TextView tvEmailUsuario;
    private ImageView ivFotoUsuario;
    private FragmentAlertas fragment;
    private String tipoUsuario;
    private DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tvNombreUsuario = header.findViewById(R.id.tvNombreUsuario);
        tvEmailUsuario = header.findViewById(R.id.tvEmailUsuario);
        ivFotoUsuario = header.findViewById(R.id.ivFotoUsuario);//todo cambio de la imagen en preferencias

        tvNombreUsuario.setText(getIntent().getStringExtra("nombreUsuario"));
        tvEmailUsuario.setText(getIntent().getStringExtra("emailUsuario"));
        tipoUsuario = getIntent().getExtras().getString("tipoUsuario");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Bundle b = new Bundle();
        b.putString("tipoUsuario",tipoUsuario);

        if (id == R.id.nav_alertas) {
            b.putString("ALERTAS", "Mi texto");
            fragment.setArguments(b);
            setTitle(R.string.menu_alerts);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();
        } else if (id == R.id.nav_alertas_tomadas) {
            b.putString("ALERTAS_TOMADAS", "Mi texto");
            setTitle(R.string.menu_taken_alerts);
            fragment.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();
        } else if (id == R.id.nav_alertas_no_tomadas) {
            b.putString("ALERTAS_NO_TOMADAS", "Mi texto");
            fragment.setArguments(b);
            setTitle(R.string.menu_untaken_alerts);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();

        }else if (id == R.id.nav_mi_perfil) {
            b.putString("MI PERFIL", "Mi texto");
            setTitle(R.string.menu_my_profile);
            FragmentMiPerfil fragmentMiPerfil = new FragmentMiPerfil();
            fragmentMiPerfil.setArguments(b);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragmentMiPerfil).commit();
        }
        fragment.setListener(this);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            finish();
        }
    }

    @Override
    public void onAlertaClicked(int adapterPosition, String direccion) {
        //todo accion con el listener, al hacer click en la alerta se cargará google maps
    }
}
