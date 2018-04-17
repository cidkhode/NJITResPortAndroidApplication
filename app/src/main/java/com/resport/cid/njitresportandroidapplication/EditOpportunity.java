package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class EditOpportunity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    OkHttpClient client = new OkHttpClient();
    ListView edit_opp_list;
    ArrayList<EditOpp> editOpps;

    JSONObject responseJSON ;
    String data;
    JSONObject dataJSON ;
    JSONArray opportunities ;
    JSONArray applications;
    JSONObject arrayItems ;
    JSONObject info ;
    String id ;
    String oppName ;
    String description ;
    String position;
    Integer maxStudents;
    Integer hours;
    Double minGPA;
    Integer clg;
    Integer category;

    int expirationDateInt;
    String expirationDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_opportunity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        edit_opp_list = (ListView) findViewById(R.id.edit_opp_list);
        editOpps = new ArrayList<>();
        getOpp();

        if(editOpps.size() == 0) {
            Toast.makeText(EditOpportunity.this, "You have not created any opportunities yet!", Toast.LENGTH_LONG).show();
        } else {
            EditOppAdapter customAdapter = new EditOppAdapter(this, R.layout.layout_edit_opp_list, editOpps);
            edit_opp_list.setAdapter(customAdapter);
        }
        edit_opp_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                EditOpp appItem = (EditOpp) edit_opp_list.getAdapter().getItem(i);
                startActivity(new Intent(EditOpportunity.this, EditSpecificOpp.class)
                        .putExtra("Name", appItem.getName())
                        .putExtra("Id", appItem.getId())
                        .putExtra("Description", appItem.getDescription())
                        .putExtra("Position", appItem.getPosition())
                        .putExtra("maxStudents", appItem.getNum().toString())
                        .putExtra("hours", appItem.getHours().toString())
                        .putExtra("minGPA", Double.toString(appItem.getMinGPA()))
                        .putExtra("clg", appItem.getCollege())
                        .putExtra("category", appItem.getCategory())
                        .putExtra("expirationInt", appItem.getExpiryDate().toString())
                        .putExtra("Expiration", appItem.getExpiration()));
            }
        });

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

    public void getOpp(){
        String result = "";
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/opportunities")
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
                opportunities = dataJSON.getJSONArray("opportunities");
                for(int i=0;i<opportunities.length();i++)
                {
                    info = opportunities.getJSONObject(i);
                    id = info.getString("id");
                    oppName = info.getString("name");
                    description = info.getString("description");
                    position = info.getString("position");
                    maxStudents = info.getInt("maxStudents");
                    hours = info.getInt("hoursWeekly");
                    minGPA = info.getDouble("minGPA");
                    clg = info.getInt("college");
                    category = info.getInt("category");
                    expirationDateInt = Integer.parseInt(info.getString("deadline"));
                    expirationDate = new SimpleDateFormat("MM/dd/yyyy")
                            .format(new Date(expirationDateInt * 1000L));

                    editOpps.add(new EditOpp(id, oppName, description, position, maxStudents, hours, minGPA, clg, category, expirationDate, expirationDateInt));
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace(); }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.fac_profile) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditOpportunity.this, FacultyProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_create) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditOpportunity.this, CreateOpportunity.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_applicants) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditOpportunity.this, FacultyViewListOfOpp.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        }else if (id == R.id.fac_edit_opp) {
        } else if (id == R.id.fac_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditOpportunity.this, ContactUs.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(EditOpportunity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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
