package com.example.irene.geoatencion;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.irene.geoatencion.Model.NotificationFirebase;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //static Toolbar toolbar;
    View mView;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            Log.d("firebase", "DATOS RECIBIDOS MAIN ACTIVITY");
            Log.d("firebase", "Latitud: " + getIntent().getExtras().getString("networkLatitude"));
            Log.d("firebase", "Logintud: " + getIntent().getExtras().getString("networkLongitude"));
            Log.d("firebase", "Unidad: " + getIntent().getExtras().getString("networkCode"));
            Log.d("firebase", "Status: " + getIntent().getExtras().getString("status"));
            NotificationFirebase notification;
            notification = new NotificationFirebase(getIntent().getExtras().getString("networkLatitude"),
                    getIntent().getExtras().getString("networkLongitude"),
                    getIntent().getExtras().getString("networkAddress"),
                    getIntent().getExtras().getString("networkCode"),
                    getIntent().getExtras().getString("status"));
            MapsFragment.AgregarMarcadorPush(notification);
        }

        SharedPreferences settings = getSharedPreferences("perfil", MODE_PRIVATE);
        if (settings.getString("id", null)==null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Cerrar sesión
        if (id == R.id.action_settings) {
            SharedPreferences settings = getSharedPreferences("perfil", MODE_PRIVATE);
            settings.edit().clear().commit();
            Intent intent = new Intent (this, LoginActivity.class);
            startActivityForResult(intent, 0);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Fragment fragment = new ProfileFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        } else if (id == R.id.nav_history) {
            Fragment fragment = new HistoryFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        }else if (id == R.id.nav_map) {
            Fragment fragment = new MapsFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        } else if (id == R.id.nav_alarm) {
            Fragment fragment = new AlarmFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();

        } else if (id == R.id.nav_calification) {
            Fragment fragment = new RatingFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment, fragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
