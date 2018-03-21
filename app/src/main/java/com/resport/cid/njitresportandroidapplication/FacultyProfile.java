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
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class FacultyProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    boolean editMode = false;
    OkHttpClient client = new OkHttpClient();
    String ucid = "";
    String fname = "";
    String lname = "";
    String email = "";
    int college = 0;
    String office = "";
    String fieldOfStudy = "";
    String experience = "";
    static EditText faculty_fname;
    static EditText faculty_lname;
    static EditText faculty_email;
    static EditText faculty_field;
    static EditText faculty_years;
    static EditText faculty_office;
    static Spinner faculty_college;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        faculty_fname = (EditText) findViewById(R.id.editText15);
        faculty_lname = (EditText) findViewById(R.id.editText16);
        faculty_email = (EditText) findViewById(R.id.editText17);
        faculty_field = (EditText) findViewById(R.id.editText18);
        faculty_years = (EditText) findViewById(R.id.editText19);
        faculty_office = (EditText) findViewById(R.id.editText21);
        faculty_college = (Spinner) findViewById(R.id.spinner4);

        faculty_fname.setEnabled(false);
        faculty_lname.setEnabled(false);
        faculty_email.setEnabled(false);
        faculty_field.setEnabled(false);
        faculty_years.setEnabled(false);
        faculty_office.setEnabled(false);
        faculty_college.setEnabled(false);

        faculty_fname.setSelection(faculty_fname.getText().length());
        faculty_lname.setSelection(faculty_lname.getText().length());
        faculty_email.setSelection(faculty_email.getText().length());
        faculty_field.setSelection(faculty_field.getText().length());
        faculty_years.setSelection(faculty_years.getText().length());
        faculty_office.setSelection(faculty_office.getText().length());

        loadProfile();
    }

    public void updateProfile(View view) {
        if (editMode == false) {
            editMode = true;
            faculty_fname.setEnabled(true);
            faculty_lname.setEnabled(true);
            faculty_email.setEnabled(true);
            faculty_field.setEnabled(true);
            faculty_years.setEnabled(true);
            faculty_office.setEnabled(true);
            faculty_college.setEnabled(true);

            faculty_fname.setSelection(faculty_fname.getText().length());
            faculty_lname.setSelection(faculty_lname.getText().length());
            faculty_email.setSelection(faculty_email.getText().length());
            faculty_field.setSelection(faculty_field.getText().length());
            faculty_years.setSelection(faculty_years.getText().length());
            faculty_office.setSelection(faculty_office.getText().length());

            faculty_fname.setBackgroundResource(R.drawable.rounded_textbox);
            faculty_lname.setBackgroundResource(R.drawable.rounded_textbox);
            faculty_email.setBackgroundResource(R.drawable.rounded_textbox);
            faculty_field.setBackgroundResource(R.drawable.rounded_textbox);
            faculty_years.setBackgroundResource(R.drawable.rounded_textbox);
            faculty_office.setBackgroundResource(R.drawable.rounded_textbox);
            faculty_college.setBackgroundResource(R.drawable.rounded_textbox);

        }
        else
        {
            editMode = false;
            faculty_fname.setEnabled(false);
            faculty_lname.setEnabled(false);
            faculty_email.setEnabled(false);
            faculty_field.setEnabled(false);
            faculty_years.setEnabled(false);
            faculty_office.setEnabled(false);
            faculty_college.setEnabled(false);

            faculty_fname.setBackgroundResource(R.drawable.rounded_textbox_faded);
            faculty_lname.setBackgroundResource(R.drawable.rounded_textbox_faded);
            faculty_email.setBackgroundResource(R.drawable.rounded_textbox_faded);
            faculty_field.setBackgroundResource(R.drawable.rounded_textbox_faded);
            faculty_years.setBackgroundResource(R.drawable.rounded_textbox_faded);
            faculty_office.setBackgroundResource(R.drawable.rounded_textbox_faded);
            faculty_college.setBackgroundResource(R.drawable.rounded_textbox_faded);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.faculty_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up mainLoginButton, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fac_profile) {

        } else if (id == R.id.fac_create) {
            Intent intent = new Intent(FacultyProfile.this, CreateOpportunity.class);
            startActivity(intent);
        } else if (id == R.id.fac_applicants) {

        } else if (id == R.id.fac_contact) {
            Intent intent = new Intent(FacultyProfile.this, ContactUs.class);
            startActivity(intent);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(FacultyProfile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadProfile(){
        String temp = readToken();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/user")
                    .header("Authorization", "Bearer "+temp)
                    .build();
            Response response = client.newCall(request).execute();
            parseInformation(response.body().string());
        } catch (IOException exception) {
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

    public void parseInformation(String response)  {
        JSONObject responseJSON = null;
        try {
            responseJSON = new JSONObject(response);
            String data;
            if(responseJSON.length()==4)
            {
                data = responseJSON.getString("data");
                JSONObject dataJSON = new JSONObject(data);
                ucid = dataJSON.getString("ucid");
                fname = dataJSON.getString("fname");
                faculty_fname.setText(fname);
                lname = dataJSON.getString("lname");
                faculty_lname.setText(lname);
                email = dataJSON.getString("email");
                faculty_email.setText(email);
                college = dataJSON.getInt("college");
                faculty_college.setSelection(college);      //may be college-1 or college-2, depending on starting index
                office = dataJSON.getString("office");
                faculty_office.setText(office);
                fieldOfStudy = dataJSON.getString("fieldOfStudy");
                faculty_field.setText(fieldOfStudy);
                experience = dataJSON.getString("experience");
                faculty_years.setText(experience);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
