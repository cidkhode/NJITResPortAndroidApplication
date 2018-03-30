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
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentOpportunityView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    TextView oppDetails;
    Button showInterest;
    String oppId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_opportunity_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        showInterest = (Button) findViewById(R.id.showInterest);
        oppDetails = (TextView) findViewById(R.id.studentOpportunityViewTextView);
        Intent intent = getIntent();
        String name = intent.getStringExtra("Opportunity");
        oppId = intent.getStringExtra("oppId");
        oppDetails.setText(name);

    }

    public void showInterest(View view)
    {
        String temp = readToken();
        RequestBody formBody = new FormBody.Builder()
                .add("id", oppId)
                .build();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/apply")
                    .header("Authorization", "Bearer " + temp)
                    .post(formBody)
                    .build();
            Toast.makeText(StudentOpportunityView.this,"Applied! We hope you get it!", Toast.LENGTH_LONG).show();
            Response response = null;
            response = client.newCall(request).execute();
            System.out.println("Showed interest! Good luck");
        } catch (IOException exception) {

        }
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
        getMenuInflater().inflate(R.menu.student_opportunity_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.stu_profile) {
            Intent intent = new Intent(StudentOpportunityView.this, StudentProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
            startActivity(intent);
        } else if (id == R.id.stu_browse) {
            Intent intent = new Intent(StudentOpportunityView.this, StudentOpportunitiesList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
            startActivity(intent);

        } else if (id == R.id.stu_status) {
            Toast.makeText(StudentOpportunityView.this,"Statuses Page is not ready yet.", Toast.LENGTH_LONG).show();
            //Intent intent = new Intent(StudentOpportunitiesList.this, StudentOpportunitiesList.class);
            //startActivity(intent);
        } else if (id == R.id.stu_contact) {
            Intent intent = new Intent(StudentOpportunityView.this, ContactUsStudent.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
            startActivity(intent);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentOpportunityView.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public String readToken()
    {
        File path = getApplicationContext().getFilesDir();
        File tokenFile = new File(path, "token.txt");

        int size = (int) tokenFile.length();
        byte[] data = new byte[size];
        FileInputStream reader = null;
        try {
            reader = new FileInputStream(tokenFile);
            reader.read(data);
            reader.close();
            String token = new String(data);
            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR!";
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
