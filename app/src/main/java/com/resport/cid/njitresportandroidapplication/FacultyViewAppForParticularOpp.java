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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FacultyViewAppForParticularOpp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    ArrayList<String> apps = new ArrayList<String>();
    //EditText facultyListOfApplicantsForParticularOpp ;
    ListView facultyListApp;
    ArrayList<FacultyListApplicant> facultyListApps;
    String information ;
    JSONObject allApplicants;
    Integer appid ;
    Integer status ;
    Long timestamp ;
    String ucid ;
    String name ;
    Double gpa ;
    Boolean honors ;
    Integer classes ;
    String className;
    Integer major ;
    String majorName;
    JSONArray applicants;
    ArrayList<String> student_colleges = new ArrayList<String>();
    ArrayList<String> student_majors = new ArrayList<String>();
    ArrayList<String> student_class = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_app_for_particular_opp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getIds();
        facultyListApps = new ArrayList<>();
        facultyListApp = (ListView) findViewById(R.id.facultyListApp);
        Intent intent = getIntent();
        information = intent.getStringExtra("FacultyOpp");
        System.out.println("---------------------------------------------"+information);

        try {
            applicants = new JSONArray(information);
                for (int i = 0; i < applicants.length(); i++) {
                    allApplicants = applicants.getJSONObject(i);
                    appid = allApplicants.getInt("appid");
                    status = allApplicants.getInt("status");
                    timestamp = allApplicants.getLong("timestamp");
                    ucid = allApplicants.getString("ucid");
                    name = allApplicants.getString("name");
                    System.out.println("---------------------------------------------" + name);
                    gpa = allApplicants.getDouble("gpa");
                    honors = allApplicants.getBoolean("honors");
                    classes = allApplicants.getInt("class");
                    className = student_class.get(classes-1);
                    major = allApplicants.getInt("major");
                    majorName = student_majors.get(major-1);
                    facultyListApps.add(new FacultyListApplicant(name, majorName, gpa, className, ucid, appid, status));
                }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        FacultyListApplicantAdapter customAdapter = new FacultyListApplicantAdapter(this, R.layout.layout_faculty_list_app, facultyListApps);
        facultyListApp.setAdapter(customAdapter);

        facultyListApp.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                FacultyListApplicant oppItem = (FacultyListApplicant) facultyListApp.getAdapter().getItem(i);

                //Name: Example Opportunity \n\nCollege: NCE \n\nNumber of Students: 10 \n\nDescription: This is an example research opportunity just to demonstrate the idea.\n\nFaculty: Prof X\n\nFaculty UCID: profx
                startActivity(new Intent(FacultyViewAppForParticularOpp.this, facultyViewApplicant.class)
                        .putExtra("ucid",oppItem.getUcid())
                        .putExtra("appid", oppItem.getAppid().toString())
                        .putExtra("status", oppItem.getStatus().toString())
                        .putExtra("Applicant", "Name: " + oppItem.getName() + "\n\nEmail: " + oppItem.getUcid()+"@njit.edu"
                                + " \n\nMajor: " + oppItem.getMajor() + " \n\nGPA: " + oppItem.getGpa()
                                + " \n\nClass: " + oppItem.getClass1() ));
            }
        });


    }
    public void getIds()
    {
        String result = "";
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/info")
                    .header("Authorization", "Bearer " + temp)
                    .build();
            Response response = client.newCall(request).execute();
            result = response.body().string();

            JSONObject responseJSON = null;
            try {
                responseJSON = new JSONObject(result);
                String data;
                if(responseJSON.length()==4)
                {
                    data = responseJSON.getString("data");
                    JSONObject dataJSON = new JSONObject(data);

                    JSONArray collegeInfo = dataJSON.getJSONArray("colleges");
                    for(int i=0;i<collegeInfo.length();i++) {
                        student_colleges.add(collegeInfo.getJSONObject(i).getString("college"));
                    }

                    JSONArray majorInfo = dataJSON.getJSONArray("majors");
                    for(int i=0;i<majorInfo.length();i++) {
                        student_majors.add(majorInfo.getJSONObject(i).getString("major"));
                    }
                    JSONArray classInfo = dataJSON.getJSONArray("class");
                    for(int i=0;i<classInfo.length();i++) {
                        student_class.add(classInfo.getJSONObject(i).getString("class"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            result = "Error getting Ids";
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
            Intent intent = new Intent(FacultyViewAppForParticularOpp.this, FacultyProfile.class);
            startActivity(intent);
        } else if (id == R.id.fac_create) {
            Intent intent = new Intent(FacultyViewAppForParticularOpp.this, CreateOpportunity.class);
            startActivity(intent);
        } else if (id == R.id.fac_applicants) {
            Intent intent = new Intent(FacultyViewAppForParticularOpp.this, FacultyViewListOfOpp.class);
            startActivity(intent);

        } else if (id == R.id.fac_contact) {
            Intent intent = new Intent(FacultyViewAppForParticularOpp.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(FacultyViewAppForParticularOpp.this, MainActivity.class);
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
