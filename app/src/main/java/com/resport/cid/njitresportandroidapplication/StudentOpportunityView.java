package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentOpportunityView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    TextView oppName;
    TextView oppCollege;
    TextView oppNumberOfStudents;
    TextView oppDescription;
    TextView oppFacultyName;
    TextView oppFacultyUCID;
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
        oppName = (TextView) findViewById(R.id.view_opportunity_name);
        oppCollege = (TextView) findViewById(R.id.view_opportunity_college);
        oppNumberOfStudents = (TextView) findViewById(R.id.view_opportunity_number_of_students);
        oppDescription = (TextView) findViewById(R.id.view_opportunity_description);
        oppFacultyName = (TextView) findViewById(R.id.view_opportunity_faculty_name);
        oppFacultyUCID = (TextView) findViewById(R.id.view_opportunity_faculty_ucid);

        oppDescription.setMovementMethod(new ScrollingMovementMethod());

        oppDescription.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK){
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        return true;
                }
                return false;
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra("Name");
        String college = intent.getStringExtra("College");
        String numberOfStudents = Integer.toString(intent.getIntExtra("Number",0));
        String description = intent.getStringExtra("Description");
        String facultyName = intent.getStringExtra("FacultyName");
        String facultyUCID = intent.getStringExtra("FacultyUCID");

        oppName.setText(name);
        oppCollege.setText(college);
        oppNumberOfStudents.setText(numberOfStudents);
        oppDescription.setText(description);
        oppFacultyName.setText(facultyName);
        oppFacultyUCID.setText(facultyUCID);

        oppId = intent.getStringExtra("oppId");
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
            Response response = null;
            response = client.newCall(request).execute();
            JSONObject responseBody = new JSONObject(response.body().string());
            String message = responseBody.getString("msg");
            Toast.makeText(StudentOpportunityView.this, message, Toast.LENGTH_LONG).show();

        } catch (IOException exception) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.stu_profile) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentOpportunityView.this, StudentProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_browse) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentOpportunityView.this, StudentOpportunitiesList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_status) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentOpportunityView.this, StudentApplicationsList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(StudentOpportunityView.this, ContactUsStudent.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentOpportunityView.this, MainActivity.class);
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
