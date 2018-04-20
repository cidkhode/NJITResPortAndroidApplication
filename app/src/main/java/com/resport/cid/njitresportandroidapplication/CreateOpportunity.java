package com.resport.cid.njitresportandroidapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class CreateOpportunity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    OkHttpClient client = new OkHttpClient();
    String oppName = "";
    String jobTitle = "";
    String maxNum1;
    String hours1;
    String details="";
    long college;
    String college1;
    String category1;
    String gpa1;
    Date expirationDate = null;
    HashMap<String, String> map = new HashMap<String, String>();

    ArrayList<String> opportunity_colleges = new ArrayList<String>();
    ArrayList<String> opportunity_categories = new ArrayList<String>();
    ArrayList<String> opportunity_majors = new ArrayList<String>();
    ArrayAdapter<String> adapterColleges ;
    ArrayAdapter<String> adapterCategories;
    ArrayAdapter<String> adapterMajors;
    Button submitButton;
    String expiration="";

    DatePickerDialog datePickerDialog;
    static EditText createOpportunityName;
    static Spinner createOpportunityOppCollege;
    static EditText createOpportunityJobTitle;
    static EditText createOpportunityNumberOfStudents;
    static EditText createOpportunityExpectedHoursPerWeek;
    static EditText createOpportunityDescription;
    static Spinner createOpportunityCategory;
    static MultiSelectionSpinner createOpportunityMajors;
    static EditText createOpportunityMinGPA;
    static Button createOpportunityExpirationDate;

    ArrayList<String> majors = new ArrayList<String>();

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
        datePickerDialog = new DatePickerDialog(
                this, CreateOpportunity.this, Calendar.getInstance().get(Calendar.YEAR),
                                                                              Calendar.getInstance().get(Calendar.MONTH),
                                                                              Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        submitButton = (Button) findViewById(R.id.createOpportunitySubmitButton);
        createOpportunityName = (EditText) findViewById(R.id.createOpportunityName);
        createOpportunityOppCollege = (Spinner) findViewById(R.id.createOpportunityOppCollege);
        createOpportunityJobTitle = (EditText) findViewById(R.id.createOpportunityJobTitle);
        createOpportunityNumberOfStudents = (EditText) findViewById(R.id.createOpportunityNumberOfStudents);
        createOpportunityExpectedHoursPerWeek = (EditText) findViewById(R.id.createOpportunityExpectedHoursPerWeek);
        createOpportunityCategory = (Spinner) findViewById(R.id.createOpportunityCategory);
        createOpportunityDescription = (EditText) findViewById(R.id.createOpportunityDescription);
        createOpportunityMinGPA = (EditText) findViewById(R.id.createOpportunityMinGPA);
        createOpportunityMajors = (MultiSelectionSpinner) findViewById(R.id.createOpportunityMajors);
        createOpportunityExpirationDate = (Button) findViewById(R.id.createOpportunityExpirationDate);

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

        adapterMajors = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, opportunity_majors);
        adapterMajors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        

        createOpportunityOppCollege.setAdapter(adapterColleges);
        createOpportunityCategory.setAdapter(adapterCategories);
        createOpportunityMajors.setItems(opportunity_majors);
    }

    public void createOpp(View view) {
        oppName = createOpportunityName.getText().toString();
        jobTitle = createOpportunityJobTitle.getText().toString();
        maxNum1 = createOpportunityNumberOfStudents.getText().toString();
        hours1 = createOpportunityExpectedHoursPerWeek.getText().toString();
        details = createOpportunityDescription.getText().toString();
        college1 = Long.toString(createOpportunityOppCollege.getSelectedItemId()+1);
        category1 = Long.toString(createOpportunityCategory.getSelectedItemId()+1);
        gpa1 = createOpportunityMinGPA.getText().toString();
        if(map.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Add at least 1 tag(s) please!", Toast.LENGTH_LONG).show();
        } else {
            StringBuilder sb = new StringBuilder();
            for(int i=0;i<map.size();i++) {
                sb.append(map.get(Integer.toString(i)) + ",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            String tagString = sb.toString();
            String result = saveOpp(oppName, jobTitle, maxNum1, hours1, details, college1, category1, gpa1, tagString);
            try {
                JSONObject responseJSON = new JSONObject(result);
                String msg = responseJSON.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                createOpportunityName.setText("");
                createOpportunityOppCollege.setAdapter(adapterColleges);
                createOpportunityJobTitle.setText("");
                createOpportunityNumberOfStudents.setText("");
                createOpportunityExpectedHoursPerWeek.setText("");
                createOpportunityCategory.setAdapter(adapterCategories);
                createOpportunityMajors.setItems(opportunity_majors);
                createOpportunityDescription.setText("");
                createOpportunityMinGPA.setText("");

                map.clear();
                opportunity_categories.clear();
                opportunity_colleges.clear();
                opportunity_majors.clear();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void showDatePickerDialog(View view)
    {
        datePickerDialog.show();
    }

    public String saveOpp(String name, String title, String maxStudents, String hoursWeekly, String desc, String colleges, String categories, String minGPA, String tagString)
    {
        String temp = readToken();
        RequestBody formBody = null;
        if(createOpportunityExpirationDate.getText().toString().equals("Select Date...")) {
            formBody = new FormBody.Builder()
                    .add("name", name)
                    .add("description", desc)
                    .add("position", title)
                    .add("maxStudents", maxStudents)
                    .add("hoursWeekly", hoursWeekly)
                    .add("college", colleges)
                    .add("category", categories)
                    .add("minGPA", minGPA)
                    .add("tags", tagString)
                    .build();
        }
        else
        {
            int date = (int) (expirationDate.getTime() / 1000);
            formBody = new FormBody.Builder()
                    .add("name", name)
                    .add("description", desc)
                    .add("position", title)
                    .add("maxStudents", maxStudents)
                    .add("hoursWeekly", hoursWeekly)
                    .add("college", colleges)
                    .add("category", categories)
                    .add("minGPA", minGPA)
                    .add("deadline", Integer.toString(date))
                    .add("tags", tagString)
                    .build();
            createOpportunityExpirationDate.setText("Select Date...");
        }
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

                JSONArray majorsInfo = dataJSON.getJSONArray("degrees");
                for(int i=0;i<majorsInfo.length();i++) {
                    map.put(majorsInfo.getJSONObject(i).getString("id"), majorsInfo.getJSONObject(i).getString("degree"));
                    opportunity_majors.add(majorsInfo.getJSONObject(i).getString("degree"));
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.fac_profile) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(CreateOpportunity.this, FacultyProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_create) {

        } else if (id == R.id.fac_edit_opp) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(CreateOpportunity.this, EditOpportunity.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_applicants) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(CreateOpportunity.this, FacultyViewListOfOpp.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(CreateOpportunity.this, ContactUs.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(CreateOpportunity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

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

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        expiration = Integer.toString(i1+1) + "/" + Integer.toString(i2) + "/" + Integer.toString(i);
        createOpportunityExpirationDate.setText(expiration);
        expirationDate = new GregorianCalendar(i, i1, i2).getTime();
    }
}
