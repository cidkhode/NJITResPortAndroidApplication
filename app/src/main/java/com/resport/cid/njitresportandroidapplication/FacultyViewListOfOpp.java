package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FacultyViewListOfOpp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    ListView facultyListView;
    ArrayList<FacultyOpp> facultyOpps;

    JSONObject responseJSON ;
    String data;
    JSONObject dataJSON ;
    Integer opportunities ;
    JSONArray applications;
    JSONObject arrayItems ;
    JSONObject info ;
    String id ;
    String oppName ;
    String description ;
    String position;
    JSONArray applicants;
    String name;
    int expirationDateInt;
    String expirationDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_list_of_opp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        facultyListView = (ListView) findViewById(R.id.facultyListView);
        facultyOpps = new ArrayList<>();
        getInfo();

        FacultyOppAdapter customAdapter = new FacultyOppAdapter(this, R.layout.layout_faculty_opp, facultyOpps);
        facultyListView.setAdapter(customAdapter);

        facultyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                FacultyOpp appItem = (FacultyOpp) facultyListView.getAdapter().getItem(i);

                //Name: Example Opportunity \n\nCollege: NCE \n\nNumber of Students: 10 \n\nDescription: This is an example research opportunity just to demonstrate the idea.\n\nFaculty: Prof X\n\nFaculty UCID: profx
                startActivity(new Intent(FacultyViewListOfOpp.this, FacultyViewAppForParticularOpp.class)
                        .putExtra("FacultyOpp", appItem.getApplicants().toString()));
            }
        });

    }

    public void getInfo(){
        String result = "";
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/applications")
                    .header("Authorization", "Bearer " + temp)
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();
            parseInformation(result);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parseInformation(String result)  {
        try
        {
            responseJSON = new JSONObject(result);
            {
                data = responseJSON.getString("data");
                dataJSON = new JSONObject(data);
                opportunities = dataJSON.getInt("opportunities");
                applications = dataJSON.getJSONArray("applications");
                for(int i=0;i<applications.length();i++)
                {
                    arrayItems = applications.getJSONObject(i);
                    info = arrayItems.getJSONObject("info");
                    id = info.getString("id");
                    oppName = info.getString("name");
                    description = info.getString("description");
                    position = info.getString("position");
                    expirationDateInt = Integer.parseInt(info.getString("deadline"));
                    expirationDate = new SimpleDateFormat("MM/dd/yyyy")
                            .format(new Date(expirationDateInt * 1000L));
                    applicants = arrayItems.getJSONArray("applicants");

                    facultyOpps.add(new FacultyOpp(id, oppName, description, position, expirationDate, applicants));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace(); }
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
            Intent intent = new Intent(FacultyViewListOfOpp.this, FacultyProfile.class);
            startActivity(intent);
        } else if (id == R.id.fac_create) {
            Intent intent = new Intent(FacultyViewListOfOpp.this, CreateOpportunity.class);
            startActivity(intent);
        } else if (id == R.id.fac_applicants) {

        } else if (id == R.id.fac_contact) {
            Intent intent = new Intent(FacultyViewListOfOpp.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(FacultyViewListOfOpp.this, MainActivity.class);
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
