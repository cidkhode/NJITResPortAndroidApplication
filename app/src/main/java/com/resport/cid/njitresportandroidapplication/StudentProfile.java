package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;


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

public class StudentProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    boolean editMode = false;
    OkHttpClient client = new OkHttpClient();
    String ucid = "";
    String fname = "";
    String lname = "";
    int major = 0;
    double gpa = 0.0;
    int classType = 0;
    int college = 0;
    boolean honors = false;
    String major1="";
    String gpa1="";
    String class1="";
    String college1="";
    String honors1="";
    JSONArray collegeInfo = null;
    JSONArray majorInfo = null;
    JSONArray classesInfo = null;

    ArrayList<String> student_colleges = new ArrayList<String>();
    ArrayList<String> student_majors = new ArrayList<String>();
    ArrayList<String> student_classes = new ArrayList<String>();

    static EditText student_fname;
    static EditText student_lname;
    static EditText student_email;
    static Spinner student_major;
    static EditText student_gpa;
    static Spinner student_class;
    static Spinner student_college;
    static CheckBox student_honors;

    ArrayAdapter<String> adapterColleges;
    ArrayAdapter<String> adapterMajors;
    ArrayAdapter<String> adapterClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //CAD Majors

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        student_fname = (EditText) findViewById(R.id.studentFirstName);
        student_lname = (EditText) findViewById(R.id.studentLastName);
        student_email = (EditText) findViewById(R.id.studentEmail);
        student_major = (Spinner) findViewById(R.id.spinner5);
        student_gpa = (EditText) findViewById(R.id.studentGPA);
        student_class = (Spinner) findViewById(R.id.studentClassStanding);
        student_college = (Spinner) findViewById(R.id.studentCollege);
        student_honors = (CheckBox) findViewById(R.id.studentHonors);

        student_fname.setEnabled(false);
        student_lname.setEnabled(false);
        student_email.setEnabled(false);
        student_major.setEnabled(false);
        student_gpa.setEnabled(false);
        student_class.setEnabled(false);
        student_college.setEnabled(false);

        student_fname.setSelection(student_fname.getText().length());
        student_lname.setSelection(student_lname.getText().length());
        student_email.setSelection(student_email.getText().length());
        student_gpa.setSelection(student_gpa.getText().length());
        loadInfo();

        adapterColleges = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, student_colleges);
        adapterColleges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterMajors = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, student_majors);
        adapterMajors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapterClasses = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, student_classes);
        adapterClasses.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        student_college.setAdapter(adapterColleges);
        student_major.setAdapter(adapterMajors);
        student_class.setAdapter(adapterClasses);

        student_college.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                try{
                    int idOfCollege = position+2;
                    System.out.println(idOfCollege);
                    int idOfMajor;
                    student_majors.clear();
                    for(int i=0;i<majorInfo.length();i++) {
                        idOfMajor = majorInfo.getJSONObject(i).getInt("college");
                        if(idOfMajor == idOfCollege)
                        {
                            System.out.println("HELLO WORLD");
                            student_majors.add(majorInfo.getJSONObject(i).getString("major"));
                        }
                    }
                    adapterMajors.notifyDataSetChanged();

                } catch(JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                return;
            }

        });

        loadProfile();

    }

    public void loadInfo()
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
                collegeInfo = dataJSON.getJSONArray("colleges");
                for(int i=1;i<collegeInfo.length();i++) {
                    student_colleges.add(collegeInfo.getJSONObject(i).getString("college"));
                }

                majorInfo = dataJSON.getJSONArray("majors");
                for(int i=0;i<majorInfo.length();i++) {
                    student_majors.add(majorInfo.getJSONObject(i).getString("major"));
                }

                classesInfo = dataJSON.getJSONArray("class");
                for(int i=0;i<classesInfo.length();i++) {
                    student_classes.add(classesInfo.getJSONObject(i).getString("class"));
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(View view) {
        if(editMode == false) {
            editMode = true;
            student_fname.setEnabled(true);
            student_lname.setEnabled(true);
            student_email.setEnabled(true);
            student_major.setEnabled(true);
            student_gpa.setEnabled(true);
            student_class.setEnabled(true);
            student_college.setEnabled(true);
            student_honors.setEnabled(true);

            student_fname.setSelection(student_fname.getText().length());
            student_lname.setSelection(student_lname.getText().length());
            student_email.setSelection(student_email.getText().length());
            student_gpa.setSelection(student_gpa.getText().length());

            student_fname.setBackgroundResource(R.drawable.rounded_textbox);
            student_lname.setBackgroundResource(R.drawable.rounded_textbox);
            student_email.setBackgroundResource(R.drawable.rounded_textbox);
            student_major.setBackgroundResource(R.drawable.rounded_textbox);
            student_gpa.setBackgroundResource(R.drawable.rounded_textbox);
            student_class.setBackgroundResource(R.drawable.rounded_textbox);
            student_college.setBackgroundResource(R.drawable.rounded_textbox);
        }

        else
        {
            String majorText = student_major.getSelectedItem().toString();
            try {
                for (int i = 0; i < majorInfo.length(); i++) {
                    JSONObject jObj = majorInfo.getJSONObject(i);
                    if(majorText.equals(jObj.getString("major")))
                    {
                        major1 = Integer.toString(jObj.getInt("id"));
                    }
                }
            } catch(JSONException e){
                e.printStackTrace();
            }

            gpa1 = student_gpa.getText().toString();
            class1 = Long.toString(student_class.getSelectedItemId()+1);
            college1 = Long.toString(student_college.getSelectedItemId() + 2);
            if(student_honors.isChecked())
            {
                honors=true;
                honors1 = Boolean.toString(honors);
            }
            else
                honors1 = Boolean.toString(honors);

            editMode = false;
            student_fname.setEnabled(false);
            student_lname.setEnabled(false);
            student_email.setEnabled(false);
            student_major.setEnabled(false);
            student_gpa.setEnabled(false);
            student_class.setEnabled(false);
            student_college.setEnabled(false);
            student_honors.setEnabled(false);
            student_fname.setBackgroundResource(R.drawable.rounded_textbox_faded);
            student_lname.setBackgroundResource(R.drawable.rounded_textbox_faded);
            student_email.setBackgroundResource(R.drawable.rounded_textbox_faded);
            student_major.setBackgroundResource(R.drawable.rounded_textbox_faded);
            student_gpa.setBackgroundResource(R.drawable.rounded_textbox_faded);
            student_class.setBackgroundResource(R.drawable.rounded_textbox_faded);
            student_college.setBackgroundResource(R.drawable.rounded_textbox_faded);
            System.out.println("----------------------------------------------------------");
            System.out.println(major1 + "                 " + college1);
            System.out.println("----------------------------------------------------------");
            saveProfile(gpa1,major1,class1,college1, honors1);
        }
    }

    public void saveProfile(String gpa1, String major1, String class1, String college1, String honors1)
    {
        String temp = readToken();
        RequestBody formBody = new FormBody.Builder()
                .add("gpa", gpa1)
                .add("major", major1)
                .add("class", class1)
                .add("college", college1)
                .add("honors", honors1)
                .build();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/user")
                    .header("Authorization", "Bearer " + temp)
                    .post(formBody)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            //student_gpa.setText(response.toString());//just to see the output from api if successful
        } catch (IOException exception) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.student_profile, menu);
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

        if (id == R.id.stu_profile) {
            // Handle the camera action
        } else if (id == R.id.stu_browse) {

        } else if (id == R.id.stu_status) {

        } else if (id == R.id.stu_contact) {
            Intent intent = new Intent(StudentProfile.this, ContactUsStudent.class);
            startActivity(intent);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentProfile.this, MainActivity.class);
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
                student_fname.setText(fname);
                lname = dataJSON.getString("lname");
                student_lname.setText(lname);
                student_email.setText(ucid+"@njit.edu");
                college = dataJSON.getInt("college");
                student_college.setSelection(college-2);
                major = dataJSON.getInt("major");
                int counter = 0;
                for(int i=0;i<majorInfo.length();i++)
                {
                    JSONObject majorObj = majorInfo.getJSONObject(i);
                    if(majorObj.getInt("college") == college)
                    {
                        if(majorObj.getInt("id") == major)
                        {
                            break;
                        }
                        else
                        {
                            counter++;
                        }
                    }
                }
                student_major.setSelection(counter);
                gpa = dataJSON.getDouble("gpa");
                student_gpa.setText(Double.toString(gpa));
                classType = dataJSON.getInt("class");
                student_class.setSelection(classType-1 );
                honors = dataJSON.getBoolean("honors");
                if(honors == true){
                    student_honors.setChecked(honors);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
