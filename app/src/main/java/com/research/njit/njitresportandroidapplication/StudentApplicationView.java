package com.research.njit.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import okhttp3.OkHttpClient;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentApplicationView extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    ScrollView scrollView_student_application_view;
    Button contactFaculty;
    TextView appName;
    TextView appCollege;
    TextView appDescription;
    TextView appFacultyName;
    TextView appFacultyUCID;
    TextView appStatus;
    String appId;
    int statusInt;
    String status;
    String facultyUCID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_application_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        scrollView_student_application_view = (ScrollView) findViewById(R.id.scrollView_student_application_view);
        contactFaculty = (Button) findViewById(R.id.contactFaculty);
        appName = (TextView) findViewById(R.id.view_application_name);
        appCollege = (TextView) findViewById(R.id.view_application_college);
        appDescription = (TextView) findViewById(R.id.view_application_description);
        appFacultyName = (TextView) findViewById(R.id.view_application_faculty_name);
        appFacultyUCID = (TextView) findViewById(R.id.view_application_faculty_ucid);
        appStatus = (TextView) findViewById(R.id.view_application_faculty_status);

        appDescription.setMovementMethod(new ScrollingMovementMethod());

        appDescription.setOnTouchListener(new View.OnTouchListener() {
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
        statusInt = intent.getIntExtra("status",0);
        String name = intent.getStringExtra("Name");
        String college = intent.getStringExtra("College");
        String description = intent.getStringExtra("Description");
        String facultyName = intent.getStringExtra("FacultyName");
        facultyUCID = intent.getStringExtra("FacultyUCID");

        appName.setText(name);
        appCollege.setText(college);
        appDescription.setText(description);
        appFacultyName.setText(facultyName);
        appFacultyUCID.setText(facultyUCID);

        if(statusInt == 0)
        {
            status = "Status:\nPending Review";
            appStatus.setTextColor(getResources().getColor(R.color.pending));
        }
        else if(statusInt == 1)
        {
            status = "Status: Accepted!\nPlease contact faculty for more information!";
            appStatus.setTextColor(getResources().getColor(R.color.accepted));
        }
        else if(statusInt == -1)
        {
            status = "Status: Denied\nSorry, the opportunity has filled up.";
            appStatus.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        appStatus.setText(status);
        appId = intent.getStringExtra("appId");

  /*      scrollView_student_application_view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                appDescription.getParent().requestDisallowInterceptTouchEvent(false);

                return false;
            }
        });
*/
/*
        appDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                appDescription.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
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

        if (id == R.id.stu_profile) {
            Intent intent = new Intent(StudentApplicationView.this, StudentProfile.class);
            startActivity(intent);
        } else if (id == R.id.stu_browse) {
            Intent intent = new Intent(StudentApplicationView.this, StudentOpportunitiesList.class);
            startActivity(intent);
        } else if (id == R.id.stu_status) {
            Intent intent = new Intent(StudentApplicationView.this, StudentApplicationsList.class);
            startActivity(intent);
        } else if (id == R.id.stu_contact) {
            Intent intent = new Intent(StudentApplicationView.this, ContactUsStudent.class);
            startActivity(intent);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentApplicationView.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public void contactFaculty(View view)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ facultyUCID+"@njit.edu" });
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(StudentApplicationView.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
