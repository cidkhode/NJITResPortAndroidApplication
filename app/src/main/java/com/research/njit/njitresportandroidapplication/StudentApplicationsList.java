package com.research.njit.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentApplicationsList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    ArrayList<Application> applied_opps;
    ListView studentsListView;
    ArrayList<String> student_applied_colleges = new ArrayList<String>();
    ArrayList<String> student_applied_categories = new ArrayList<String>();
    ArrayList<String> opportunity_majors = new ArrayList<String>();
    ArrayAdapter<String> adapterApplicationColleges;
    JSONArray applications;
    JSONObject app=null;
    JSONObject info =null;
    String id = "";
    String name = "";
    String description = "";
    String position = "";
    String category = "" ;
    String categoryName="";
    String facultyName = "";
    String email = "";
    String timestamp = "";
    String college = "";
    String facUCID = "";
    String status = "";
    String collegeName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applications_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        studentsListView = (ListView) findViewById(R.id.studentsListView);
        adapterApplicationColleges = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, student_applied_colleges);
        applied_opps = new ArrayList<>();
        String responseData = loadAppliedOpportunities();

        if(!responseData.equals("Error"))
        {
            JSONObject responseJSON = null;
            try {
                getIds();
                responseJSON = new JSONObject(responseData);
                String data;
                data = responseJSON.getString("data");
                JSONObject dataJSON = new JSONObject(data);
                applications = dataJSON.getJSONArray("applications");
                for (int i = 0; i < applications.length(); i++) {
                    app = applications.getJSONObject(i);
                    id = app.getString("id");
                    name = app.getString("name");
                    description = app.getString("description");
                    position = app.getString("position");
                    category = app.getString("category");
                    categoryName = student_applied_categories.get(Integer.parseInt(category) -1);
                    facultyName = app.getString("facultyName");
                    email = app.getString("email");
                    timestamp = app.getString("timestamp");
                    status = app.getString("status");
                    college = app.getString("college");
                    facUCID = app.getString("facultyUCID");
                    collegeName = student_applied_colleges.get(Integer.parseInt(college)-1);

                    applied_opps.add(new Application(id, name, description, position, facultyName, email, Long.parseLong(timestamp), Integer.parseInt(status), collegeName, facUCID, categoryName));
                }

            } catch (JSONException e) {
                e.printStackTrace();}
        }

        if(applied_opps.size() == 0) {
            Toast.makeText(StudentApplicationsList.this, "You have not showed interest to any opportunity yet!", Toast.LENGTH_LONG).show();
        } else {
            ApplicationAdapter customAdapter = new ApplicationAdapter(this, R.layout.layout_applications, applied_opps);
            studentsListView.setAdapter(customAdapter);

            studentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Application appItem = (Application) studentsListView.getAdapter().getItem(i);

                    //Name: Example Opportunity \n\nCollege: NCE \n\nNumber of Students: 10 \n\nDescription: This is an example research opportunity just to demonstrate the idea.\n\nFaculty: Prof X\n\nFaculty UCID: profx
                    startActivity(new Intent(StudentApplicationsList.this, StudentApplicationView.class)
                            .putExtra("appId", id)
                            .putExtra("status", appItem.getStatus())
                            .putExtra("Name", appItem.getName())
                            .putExtra("College", appItem.getCollege())
                            .putExtra("Description", appItem.getDescription())
                            .putExtra("FacultyName", appItem.getFacultyName())
                            .putExtra("FacultyUCID", appItem.getUCID()));
                }
            });
        }

    }

    public String loadAppliedOpportunities()
    {
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/applications")
                    .header("Authorization", "Bearer " + temp)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException exception) {
            return "Error";
        }
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
                        student_applied_colleges.add(collegeInfo.getJSONObject(i).getString("college"));
                    }

                    JSONArray categoriesInfo = dataJSON.getJSONArray("categories");
                    for(int i=0;i<categoriesInfo.length();i++) {
                        student_applied_categories.add(categoriesInfo.getJSONObject(i).getString("category"));
                    }
                    JSONArray majorsInfo = dataJSON.getJSONArray("degrees");
                    for(int i=0;i<majorsInfo.length();i++) {
                        opportunity_majors.add(majorsInfo.getJSONObject(i).getString("degree"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            result = "Error getting Ids";
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.stu_profile) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentApplicationsList.this, StudentProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 230);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_browse) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentApplicationsList.this, StudentOpportunitiesList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 230);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_status) {

        } else if (id == R.id.stu_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentApplicationsList.this, ContactUsStudent.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 230);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentApplicationsList.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}