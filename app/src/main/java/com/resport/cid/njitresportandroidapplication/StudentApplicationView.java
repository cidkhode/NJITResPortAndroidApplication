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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import okhttp3.OkHttpClient;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentApplicationView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    Button contactFaculty;
    TextView appDetails;
    TextView statusView;
    String appId;
    String statusInt;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_application_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        contactFaculty = (Button) findViewById(R.id.contactFaculty);
        appDetails = (TextView) findViewById(R.id.studentApplicationViewTextView);
        statusView = (TextView) findViewById(R.id.applicationStatus);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Application");
        statusInt = intent.getStringExtra("status");
        if(statusInt.equals("0"))
        {
            status = "Status:\nPending Review";
            statusView.setTextColor(getResources().getColor(R.color.pending));
        }
        else if(statusInt.equals("1"))
        {
            status = "Status: Accepted!\nPlease contact faculty for more information!";
            statusView.setTextColor(getResources().getColor(R.color.accepted));
        }
        else if(statusInt.equals("-1"))
        {
            status = "Status: Denied\nSorry, the opportunity has filled up.";
            statusView.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        statusView.setText(status);
        appId = intent.getStringExtra("appId");
        appDetails.setText(name);
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

        if (id == R.id.stu_profile) {
            Intent intent = new Intent(StudentApplicationView.this, StudentProfile.class);
            startActivity(intent);
        } else if (id == R.id.stu_browse) {
            Intent intent = new Intent(StudentApplicationView.this, StudentOpportunitiesList.class);
            startActivity(intent);
        } else if (id == R.id.stu_status) {
            Intent intent = new Intent(StudentApplicationView.this, StudentApplicationsList.class);
            startActivity(intent);
        } else if (id == R.id.stu_contact) {
            Intent intent = new Intent(StudentApplicationView.this, ContactUsStudent.class);
            startActivity(intent);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentApplicationView.this, MainActivity.class);
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

    public void contactFaculty(View view)
    {

    }
}
