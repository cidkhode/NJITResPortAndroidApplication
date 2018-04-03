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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateOpportunity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    OkHttpClient client = new OkHttpClient();
    String oppName = "";
    String jobTitle = "";
    String numStu="";
    int maxNum;
    int hour;
    String details="";
    long college;
    long category;
    double gpa;
    ArrayList<String> opportunity_colleges = new ArrayList<String>();
    ArrayList<String> opportunity_categories = new ArrayList<String>();
    ArrayAdapter<String> adapterColleges ;
    ArrayAdapter<String> adapterCategories;
    Button submitButton;

    static EditText createOpportunityName;
    static Spinner createOpportunityOppCollege;
    static EditText createOpportunityJobTitle;
    static EditText createOpportunityNumberOfStudents;
    static EditText createOpportunityExpectedHoursPerWeek;
    static EditText createOpportunityDescription;
    static Spinner createOpportunityCategory;
    static EditText createOpportunityMinGPA;


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

        submitButton = (Button) findViewById(R.id.createOpportunitySubmitButton);
        createOpportunityName = (EditText) findViewById(R.id.createOpportunityName);
        createOpportunityOppCollege = (Spinner) findViewById(R.id.createOpportunityOppCollege);
        createOpportunityJobTitle = (EditText) findViewById(R.id.createOpportunityJobTitle);
        createOpportunityNumberOfStudents = (EditText) findViewById(R.id.createOpportunityNumberOfStudents);
        createOpportunityExpectedHoursPerWeek = (EditText) findViewById(R.id.createOpportunityExpectedHoursPerWeek);
        createOpportunityCategory = (Spinner) findViewById(R.id.createOpportunityCategory);
        createOpportunityDescription = (EditText) findViewById(R.id.createOpportunityDescription);
        createOpportunityMinGPA = (EditText) findViewById(R.id.createOpportunityMinGPA);

        createOpportunityName.setSelection(createOpportunityName.getText().length());
        createOpportunityJobTitle.setSelection(createOpportunityJobTitle.getText().length());
        createOpportunityNumberOfStudents.setSelection(createOpportunityNumberOfStudents.getText().length());
        createOpportunityExpectedHoursPerWeek.setSelection(createOpportunityExpectedHoursPerWeek.getText().length());
        createOpportunityDescription.setSelection(createOpportunityDescription.getText().length());
        createOpportunityMinGPA.setSelection(createOpportunityMinGPA.getText().length());

        getIds();
        adapterColleges = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, opportunity_colleges);
        adapterColleges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterCategories = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, opportunity_categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        createOpportunityOppCollege.setAdapter(adapterColleges);
        createOpportunityCategory.setAdapter(adapterCategories);

    }
    public void createOpp(View view){

        oppName = createOpportunityName.getText().toString();
        jobTitle = createOpportunityJobTitle.getText().toString();
        maxNum = Integer.parseInt(createOpportunityNumberOfStudents.getText().toString());
        String maxNum1 = String.valueOf(maxNum);
        hour = Integer.parseInt(createOpportunityExpectedHoursPerWeek.getText().toString());
        String hours1 = String.valueOf(hour);
        details=createOpportunityDescription.getText().toString();
        college = createOpportunityOppCollege.getSelectedItemId()+1;
        String college1 = String.valueOf(college);
        category = createOpportunityCategory.getSelectedItemId()+1;
        String category1 = String.valueOf(category);
        gpa = Double.parseDouble(createOpportunityMinGPA.getText().toString());
        String gpa1 = String.valueOf(gpa);

        String result = saveOpp(oppName,jobTitle,maxNum1,hours1,details,college1,category1,gpa1);
        try {
            JSONObject responseJSON = new JSONObject(result);
            String msg = responseJSON.getString("msg");
            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        createOpportunityName.setText("");
        createOpportunityOppCollege.setAdapter(adapterColleges);
        createOpportunityJobTitle.setText("");
        createOpportunityNumberOfStudents.setText("");
        createOpportunityExpectedHoursPerWeek.setText("");
        createOpportunityCategory.setAdapter(adapterCategories);
        createOpportunityDescription.setText("");
        createOpportunityMinGPA.setText("");

    }

    public String saveOpp(String name, String title, String maxStudents, String hoursWeekly, String desc, String colleges, String categories, String minGPA)
    {
        String temp = readToken();
        RequestBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("description", desc)
                .add("position", title)
                .add("maxStudents", maxStudents)
                .add("hoursWeekly", hoursWeekly)
                .add("college", colleges)
                .add("category", categories)
                .add("minGPA", minGPA)
                .build();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/opportunity")
                    .header("Authorization", "Bearer " + temp)
                    .post(formBody)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            return (response.body().string());
        } catch (IOException exception) {
            exception.printStackTrace();
            return "Error";
        }
    }
    public void getIds()
    {
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/info")
                    .header("Authorization", "Bearer " + temp)
                    .build();
            Response response = client.newCall(request).execute();
            parseVariableInformation(response.body().string());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void parseVariableInformation(String response)
    {
        System.out.println("------------------------response----------------------------------"+response);

        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(response);
            String data;
            if(responseJSON.length()==4)
            {
                data = responseJSON.getString("data");
                JSONObject dataJSON = new JSONObject(data);
                JSONArray collegeInfo = dataJSON.getJSONArray("colleges");
                for(int i=0;i<collegeInfo.length();i++) {
                    opportunity_colleges.add(collegeInfo.getJSONObject(i).getString("college"));
                }

                JSONArray categoriesInfo = dataJSON.getJSONArray("categories");
                for(int i=0;i<categoriesInfo.length();i++) {
                    opportunity_categories.add(categoriesInfo.getJSONObject(i).getString("category"));
                }

            }
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
