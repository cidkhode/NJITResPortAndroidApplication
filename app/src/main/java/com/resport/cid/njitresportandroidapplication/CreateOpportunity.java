package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CreateOpportunity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_opportunity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fac_profile) {
            Intent intent = new Intent(CreateOpportunity.this, FacultyProfile.class);
            startActivity(intent);
        } else if (id == R.id.fac_create) {

        } else if (id == R.id.fac_applicants) {

        } else if (id == R.id.fac_contact) {
            Intent intent = new Intent(CreateOpportunity.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(CreateOpportunity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void clearToken() {
        File path = getApplicationContext().getFilesDir();
        File tokenFile = new File(path, "token.txt");
        PrintWriter clearer = null;
        try {
            clearer = new PrintWriter(tokenFile);
            clearer.write("");
            clearer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
