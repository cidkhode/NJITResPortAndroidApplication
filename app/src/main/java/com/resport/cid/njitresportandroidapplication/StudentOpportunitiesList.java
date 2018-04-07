package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentOpportunitiesList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    EditText studentOpportunitiesListFacultyUCIDEditText;
    Spinner studentOpportunitiesListFacultyCollegeSpinner;
    ArrayList<Opportunity> opps;
    ArrayList<String> student_colleges = new ArrayList<String>();
    ArrayList<String> student_categories = new ArrayList<String>();
    JSONObject opp=null;
    String id ="";
    JSONObject info =null ;
    String name ="";
    String desc ="";
    String position = "" ;
    String category = "" ;
    String categoryName ="";
    String college= "" ;
    String collegeName ="";
    String limit= "" ;
    String hours= "" ;
    String minGPA = "" ;
    JSONObject faculty= null ;
    String facultyName= "" ;
    int expirationDateInt;
    String expirationDate = "";
    String email="" ;
    String facUCID = "";
    JSONArray opportunities;
    ListView listView;
    String enteredFacUCID;
    String enteredCollege;
    ArrayAdapter<String> adapterColleges;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_opportunities_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Browse Opportunities");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listView = (ListView) findViewById(R.id.listView);

        studentOpportunitiesListFacultyUCIDEditText = findViewById(R.id.studentOpportunitiesListFacultyUCIDEditText);
        studentOpportunitiesListFacultyCollegeSpinner = findViewById(R.id.studentOpportunitiesListFacultyCollegeSpinner);
        adapterColleges = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, student_colleges);
        opps = new ArrayList<>();

        String responseData = loadOpportunities();
        if(!responseData.equals("Error"))
        {
            JSONObject responseJSON = null;
            try {
                getIds();
                responseJSON = new JSONObject(responseData);
                String data;
                data = responseJSON.getString("data");
                JSONObject dataJSON = new JSONObject(data);
                Integer number = dataJSON.getInt("num");
                opportunities = dataJSON.getJSONArray("opportunities");
                for (int i = 0; i < opportunities.length(); i++) {
                    opp = opportunities.getJSONObject(i);
                    id = opp.getString("id");
                    info = opp.getJSONObject("info");
                    name = info.getString("name");
                    desc = info.getString("desc");
                    position = info.getString("position");
                    category = info.getString("category");
                    categoryName = student_categories.get(Integer.parseInt(category) -1);
                    college = info.getString("college");
                    collegeName = student_colleges.get(Integer.parseInt(college)-1);
                    limit = info.getString("limit");
                    hours = info.getString("hours");
                    minGPA = info.getString("minGPA");
                    expirationDateInt = Integer.parseInt(info.getString("deadline"));
                    expirationDate = new SimpleDateFormat("MM/dd/yyyy")
                            .format(new Date(expirationDateInt * 1000L));
                    faculty = opp.getJSONObject("faculty");
                    facultyName = faculty.getString("name");
                    email = faculty.getString("email");
                    facUCID = faculty.getString("ucid");
                    opps.add(new Opportunity(id, name, collegeName, position, Integer.parseInt(limit), Integer.parseInt(hours), desc, facultyName, facUCID, email, categoryName, expirationDate));
                }

            } catch (JSONException e) {
                e.printStackTrace();}
        }

        OpportunityAdapter customAdapter = new OpportunityAdapter(this, R.layout.layout_opportunities, opps);
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                Opportunity oppItem = (Opportunity) listView.getAdapter().getItem(i);

                //Name: Example Opportunity \n\nCollege: NCE \n\nNumber of Students: 10 \n\nDescription: This is an example research opportunity just to demonstrate the idea.\n\nFaculty: Prof X\n\nFaculty UCID: profx
                startActivity(new Intent(StudentOpportunitiesList.this, StudentOpportunityView.class)
                        .putExtra("oppId" , oppItem.getOppId())
                        .putExtra("Opportunity", "Name: " + oppItem.getOppName() + "\n\nCollege: " + oppItem.getOppCollege()
                                + " \n\nNumber of Students: " + oppItem.getNumStudents() + " \n\nDescription: " + oppItem.getDescription()
                                + " \n\nFaculty: " + oppItem.getFacultyName() + " \n\nFaculty UCID: " + oppItem.getFacultyUCID()));
            }
        });
    }

    public void filter(View view) {
        opps.removeAll(opps);

        enteredFacUCID = studentOpportunitiesListFacultyUCIDEditText.getText().toString();
        enteredCollege = Long.toString(studentOpportunitiesListFacultyCollegeSpinner.getSelectedItemId());

        if (enteredFacUCID.equals("") && enteredCollege.equals("0")) {
            Toast.makeText(StudentOpportunitiesList.this, "Please enter a filter!", Toast.LENGTH_LONG).show();
        }
        else {
            try {
                for (int i = 0; i < opportunities.length(); i++) {
                    opp = opportunities.getJSONObject(i);
                    id = opp.getString("id");
                    info = opp.getJSONObject("info");
                    name = info.getString("name");
                    desc = info.getString("desc");
                    position = info.getString("position");
                    category = info.getString("category");
                    categoryName = student_categories.get(Integer.parseInt(category) - 1);
                    college = info.getString("college");
                    collegeName = student_colleges.get(Integer.parseInt(college)-1);
                    limit = info.getString("limit");
                    hours = info.getString("hours");
                    minGPA = info.getString("minGPA");
                    expirationDateInt = Integer.parseInt(info.getString("deadline"));
                    expirationDate = new SimpleDateFormat("MM/dd/yyyy")
                            .format(new Date(expirationDateInt * 1000L));
                    faculty = opp.getJSONObject("faculty");
                    facultyName = faculty.getString("name");
                    email = faculty.getString("email");
                    facUCID = faculty.getString("ucid");

                    if (enteredFacUCID.equals("") && !enteredCollege.equals("0")) {
                        if (college.equals(enteredCollege)) {
                            opps.add(new Opportunity(id, name, collegeName, position, Integer.parseInt(limit), Integer.parseInt(hours), desc, facultyName, facUCID, email, categoryName, expirationDate));
                        }
                    } else if (!enteredFacUCID.equals("") && enteredCollege.equals("0")) {
                        if (facUCID.equals(enteredFacUCID)) {
                            opps.add(new Opportunity(id, name, collegeName, position, Integer.parseInt(limit), Integer.parseInt(hours), desc, facultyName, facUCID, email, categoryName, expirationDate));
                        }
                    } else if (facUCID.equals(enteredFacUCID) && college.equals(enteredCollege)) {
                        opps.add(new Opportunity(id, name, collegeName, position, Integer.parseInt(limit), Integer.parseInt(hours), desc, facultyName, facUCID, email, categoryName, expirationDate));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            OpportunityAdapter customAdapter = new OpportunityAdapter(this, R.layout.layout_opportunities, opps);
            listView.setAdapter(customAdapter);
        }

    }

    public void clear(View view) {
        opps.removeAll(opps);
        studentOpportunitiesListFacultyCollegeSpinner.setSelection(0);
        studentOpportunitiesListFacultyUCIDEditText.setText("");
        try{
            for (int i = 0; i < opportunities.length(); i++) {
                opp = opportunities.getJSONObject(i);
                id = opp.getString("id");
                info = opp.getJSONObject("info");
                name = info.getString("name");
                desc = info.getString("desc");
                position = info.getString("position");
                category = info.getString("category");
                categoryName = student_categories.get(Integer.parseInt(category) -1);
                college = info.getString("college");
                collegeName = student_colleges.get(Integer.parseInt(college)-1);
                limit = info.getString("limit");
                hours = info.getString("hours");
                minGPA = info.getString("minGPA");
                expirationDateInt = Integer.parseInt(info.getString("deadline"));
                expirationDate = new SimpleDateFormat("MM/dd/yyyy")
                        .format(new Date(expirationDateInt * 1000L));
                faculty = opp.getJSONObject("faculty");
                facultyName = faculty.getString("name");
                email = faculty.getString("email");
                facUCID = faculty.getString("ucid");
                opps.add(new Opportunity(id, name, collegeName, position, Integer.parseInt(limit), Integer.parseInt(hours), desc, facultyName, facUCID, email, categoryName, expirationDate));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OpportunityAdapter customAdapter = new OpportunityAdapter(this, R.layout.layout_opportunities, opps);
        listView.setAdapter(customAdapter);
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

    public String loadOpportunities()
    {
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/opportunities")
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
                        student_colleges.add(collegeInfo.getJSONObject(i).getString("college"));
                    }

                    JSONArray categoriesInfo = dataJSON.getJSONArray("categories");
                    for(int i=0;i<categoriesInfo.length();i++) {
                        student_categories.add(categoriesInfo.getJSONObject(i).getString("category"));
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            result = "Error getting Ids";
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
                    Intent intent = new Intent(StudentOpportunitiesList.this, StudentProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_browse) {

        } else if (id == R.id.stu_status) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentOpportunitiesList.this, StudentApplicationsList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentOpportunitiesList.this, ContactUsStudent.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 100);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentOpportunitiesList.this, MainActivity.class);
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