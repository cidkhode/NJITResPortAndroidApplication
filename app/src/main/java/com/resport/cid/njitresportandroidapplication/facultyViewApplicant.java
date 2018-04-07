package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

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

public class facultyViewApplicant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    String info;
    TextView facultyViewParticularApplicant;
    Integer appid;
    String ucid;
    String decision;
    Button facultyDecline;
    Button facultyAccept;
    Integer status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_applicant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        facultyViewParticularApplicant = (TextView) findViewById(R.id.facultyViewParticularApplicant);
        facultyAccept = (Button) findViewById(R.id.facultyAccept);
        facultyDecline = (Button) findViewById(R.id.facultyDecline);

        Intent intent = getIntent();
        info = intent.getStringExtra("Applicant");
        ucid = intent.getStringExtra("ucid");
        appid = Integer.parseInt(intent.getStringExtra("appid"));
        status = Integer.parseInt(intent.getStringExtra("status"));

        facultyViewParticularApplicant.setText(info);

        if(status == 1){
            facultyAccept.setEnabled(false);
            facultyDecline.setEnabled(false);
            facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_disabled);
            facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_disabled_noteditable);
        }
        if(status == -1){
            facultyDecline.setEnabled(false);
            facultyAccept.setEnabled(false);
            facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_disabled_noteditable);
            facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_disabled);
            //facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_diabled);
        }

    }

    public void contactStudent(View view){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ucid+"@njit.edu"});
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(facultyViewApplicant.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void accept(View view)
    {   choice("accept");
        facultyDecline.setEnabled(false);
        facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_disabled);
        facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_disabled_noteditable);
    }

    public void decline(View view)
    {   choice("reject");
        facultyAccept.setEnabled(false);
        facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_disabled_noteditable);
        facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_disabled);
    }

    public void choice(String decision)
    {
        String temp = readToken();
        RequestBody formBody = null;
        formBody = new FormBody.Builder()
                .add("appid", appid.toString())
                .add("choice", decision)
                .build();

        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/applications")
                    .header("Authorization", "Bearer " + temp)
                    .post(formBody)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            JSONObject responseJSON = new JSONObject(response.body().string());
            String msg = responseJSON.getString("msg");
            Toast.makeText(facultyViewApplicant.this, msg, Toast.LENGTH_LONG).show();


        } catch (IOException exception) {
            exception.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fac_profile) {
            Intent intent = new Intent(facultyViewApplicant.this, FacultyProfile.class);
            startActivity(intent);
        } else if (id == R.id.fac_create) {
            Intent intent = new Intent(facultyViewApplicant.this, CreateOpportunity.class);
            startActivity(intent);
        } else if (id == R.id.fac_applicants) {
            Intent intent = new Intent(facultyViewApplicant.this, FacultyViewListOfOpp.class);
            startActivity(intent);

        } else if (id == R.id.fac_contact) {
            Intent intent = new Intent(facultyViewApplicant.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(facultyViewApplicant.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public String readToken() {
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
