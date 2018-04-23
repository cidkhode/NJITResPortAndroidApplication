package com.research.njit.njitresportandroidapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
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

public class FacultyViewApplicant extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    OkHttpClient client = new OkHttpClient();
    String name;
    String email;
    String major;
    String gpa;
    String classStanding;
    String college;
    String link;
    boolean honors;

    TextView facultyViewName;
    TextView facultyViewEmail;
    TextView facultyViewMajor;
    TextView facultyViewGPA;
    TextView facultyViewClassStanding;
    TextView facultyViewCollege;
    CheckBox facultyViewHonors;

    Integer appid;
    String ucid;

    Button facultyDecline;
    Button facultyAccept;
    Button facultyContactStudent;
    Button viewResume;

    Integer status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_view_applicant);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        facultyViewName = (TextView) findViewById(R.id.view_student_applicant_name);
        facultyViewEmail = (TextView) findViewById(R.id.view_student_applicant_email);
        facultyViewMajor = (TextView) findViewById(R.id.view_student_applicant_major);
        facultyViewGPA = (TextView) findViewById(R.id.view_student_applicant_gpa);
        facultyViewClassStanding = (TextView) findViewById(R.id.view_student_applicant_class_standing);
        facultyViewCollege = (TextView) findViewById(R.id.view_student_applicant_college);
        facultyViewHonors = (CheckBox) findViewById(R.id.view_student_applicant_honors);
        facultyViewHonors.setEnabled(false);

        facultyAccept = (Button) findViewById(R.id.facultyAccept);
        facultyDecline = (Button) findViewById(R.id.facultyDecline);
        facultyContactStudent = (Button) findViewById(R.id.facultyContactStudent);
        viewResume = (Button) findViewById(R.id.view_student_resume);

        Intent intent = getIntent();
        name = intent.getStringExtra("Name");
        email = intent.getStringExtra("Email");
        major = intent.getStringExtra("Major");
        gpa = intent.getStringExtra("GPA");
        classStanding = intent.getStringExtra("Class");
        college = intent.getStringExtra("College");
        ucid = intent.getStringExtra("ucid");
        appid = Integer.parseInt(intent.getStringExtra("appid"));
        status = Integer.parseInt(intent.getStringExtra("status"));
        honors = intent.getExtras().getBoolean("Honors");
        link = intent.getStringExtra("Resume");

        System.out.println("--------LINK------ " + link);

        if(link.contains("http")) {
            viewResume.setEnabled(true);
            viewResume.setBackgroundResource(R.drawable.rounded_corner_button_red_outline);
            viewResume.setTextColor(viewResume.getResources().getColor(R.color.colorPrimary));
        }
        else
        {
            viewResume.setEnabled(false);
            viewResume.setBackgroundResource(R.drawable.rounded_textbox_red_disabled_outline);
            viewResume.setTextColor(viewResume.getResources().getColor(R.color.disabledRed));
        }

        facultyViewName.setText(name);
        facultyViewEmail.setText(email);
        facultyViewMajor.setText(major);
        facultyViewGPA.setText(gpa);
        facultyViewClassStanding.setText(classStanding);
        facultyViewCollege.setText(college);
        facultyViewHonors.setChecked(honors);

        facultyAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice("accept");
                facultyDecline.setEnabled(false);
                facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_red_disabled_outline);
                facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_green_disabled_outline);
                facultyDecline.setTextColor(facultyDecline.getResources().getColor(R.color.declineStudentDisabled));
                facultyAccept.setTextColor(facultyAccept.getResources().getColor(R.color.acceptStudentDisabled));
                Toast.makeText(FacultyViewApplicant.this, "Accepted student - Contact for interview!", Toast.LENGTH_LONG).show();

            }
        });

        facultyDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choice("reject");
                facultyAccept.setEnabled(false);
                facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_red_disabled_outline);
                facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_green_disabled_outline);
                facultyDecline.setTextColor(facultyDecline.getResources().getColor(R.color.declineStudentDisabled));
                facultyAccept.setTextColor(facultyAccept.getResources().getColor(R.color.acceptStudentDisabled));
                Toast.makeText(FacultyViewApplicant.this, "Declined student", Toast.LENGTH_LONG).show();
            }
        });

        viewResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(link)).setPackage("com.google.android.apps.docs"));
            }
        });

        facultyContactStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL  , new String[]{ ucid+"@njit.edu" });
                try {
                    startActivity(Intent.createChooser(i, "Send mail..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(FacultyViewApplicant.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(status == 1 || status == -1){
            facultyAccept.setEnabled(false);
            facultyDecline.setEnabled(false);
            facultyDecline.setBackgroundResource(R.drawable.rounded_textbox_red_disabled_outline);
            facultyAccept.setBackgroundResource(R.drawable.rounded_textbox_green_disabled_outline);
            facultyDecline.setTextColor(facultyDecline.getResources().getColor(R.color.declineStudentDisabled));
            facultyAccept.setTextColor(facultyAccept.getResources().getColor(R.color.acceptStudentDisabled));
        }
    }

    public void choice(String decision)
    {
        String temp = readToken();
        RequestBody formBody = null;
        formBody = new FormBody.Builder()
                .add("appid", appid.toString())
                .add("choice", decision)
                .build();

        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/applications")
                    .header("Authorization", "Bearer " + temp)
                    .post(formBody)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            JSONObject responseJSON = new JSONObject(response.body().string());
            String msg = responseJSON.getString("msg");

        } catch (IOException exception) {
            exception.printStackTrace();
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
                    Intent intent = new Intent(FacultyViewApplicant.this, FacultyProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_create) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FacultyViewApplicant.this, CreateOpportunity.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_edit_opp) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FacultyViewApplicant.this, EditOpportunity.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_applicants) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FacultyViewApplicant.this, FacultyViewListOfOpp.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);

        } else if (id == R.id.fac_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(FacultyViewApplicant.this, ContactUs.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(FacultyViewApplicant.this, MainActivity.class);
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
}
