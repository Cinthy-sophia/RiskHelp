package com.cinthyasophia.riskhelp;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.cinthyasophia.riskhelp.fragments.FragmentAlertas;
import com.cinthyasophia.riskhelp.fragments.FragmentAlertasRecibidas;
import com.cinthyasophia.riskhelp.fragments.FragmentMiPerfil;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PrincipalActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private TextView tvNombreUsuario;
    private TextView tvEmailUsuario;
    private Fragment fragment;
    DrawerLayout drawer;
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

        tvNombreUsuario.setText(getIntent().getStringExtra("nombreUsuario"));
        tvEmailUsuario.setText(getIntent().getStringExtra("emailUsuario"));
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
        fragment = new FragmentAlertas();


        if (id == R.id.nav_alertas) {
            b.putString("ALERTAS", "Mi texto");
            fragment.setArguments(b);
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();

        } else if (id == R.id.nav_alertas_recibidas) {
            b.putString("ALERTAS RECIBIDAS", "Mi texto");
            fragment = new FragmentAlertasRecibidas();
            fragment.setArguments(b);
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();
        } else if (id == R.id.nav_my_profile) {
            b.putString("MI PERFIL", "Mi texto");
            fragment.setArguments(b);
            fragment = new FragmentMiPerfil();
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_principal, fragment).commit();
        //fragment.setListener(this);
        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{

            super.onBackPressed();
        }
    }
}
