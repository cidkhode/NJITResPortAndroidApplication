package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
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

import okhttp3.FormBody;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StudentOpportunitiesList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    static EditText studentOpportunitiesListFacultyUCIDEditText;
    static EditText studentOpportunitiesListFacultyCollegeEditText;
    ArrayList<Opportunity> opps;


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

        ListView listView = (ListView) findViewById(R.id.listView);

        studentOpportunitiesListFacultyUCIDEditText = (EditText) findViewById(R.id.studentOpportunitiesListFacultyUCIDEditText);
        loadOpportunities();


        opps = new ArrayList<>();
        opps.add(new Opportunity("Research Opportunity in Machine Learning", "College of Computing Sciences","Research Assistant",
                2, 3, "Machine Learning opportunity using R and big data technologies like Hadoop.","Prof. ML Profes", "abc123"));

        opps.add(new Opportunity("Biological Study in Fish", "College of Science and Liberal Arts","Volunteer",
                4, 5, "Studying fish and other animals.","Prof. Biol Dept", "defg"));


        OpportunityAdapter customAdapter = new OpportunityAdapter(this, R.layout.layout_opportunities, opps);
        listView.setAdapter(customAdapter);



    }

    public void viewOpp(View view)
    {

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
        getMenuInflater().inflate(R.menu.student_opportunities_list, menu);
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


    public void loadOpportunities()
    {
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/opportunities")
                    .header("Authorization", "Bearer " + temp)
                    .build();
            Response response = client.newCall(request).execute();
            String temp1 = response.body().string();
            studentOpportunitiesListFacultyUCIDEditText.setText(temp1);
            parseInformation(temp1);
        } catch (IOException exception) {
            //return "Error";
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.stu_profile) {
            // Handle the camera action
        } else if (id == R.id.stu_browse) {

        } else if (id == R.id.stu_status) {

        } else if (id == R.id.stu_contact) {
            Intent intent = new Intent(StudentOpportunitiesList.this, ContactUsStudent.class);
            startActivity(intent);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentOpportunitiesList.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

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

    public void parseInformation(String response)  {
        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(response);
            String data;
            data = responseJSON.getString("data");
            JSONObject dataJSON = new JSONObject(data);
            Integer number = dataJSON.getInt("num");
            JSONArray opportunities = dataJSON.getJSONArray("opportunities");
            for (int i = 0; i < opportunities.length(); i++)
            {
                JSONObject opp = opportunities.getJSONObject(i);
                String id = opp.getString("id");
                JSONObject info = opp.getJSONObject("info");
                    String name = info.getString("name");
                    String desc = info.getString("desc");
                    String position = info.getString("position");
                    String category = info.getString("category");
                    String college = info.getString("College");
                    String limit = info.getString("limit");
                    String hours = info.getString("hours");
                    String minGPA = info.getString("minGPA");
                JSONObject faculty = opp.getJSONObject("faculty");
                    String facultyName = faculty.getString("name");
                    String email = faculty.getString("email");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}