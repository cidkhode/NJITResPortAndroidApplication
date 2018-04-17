package com.resport.cid.njitresportandroidapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import java.util.GregorianCalendar;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class EditSpecificOpp extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DatePickerDialog.OnDateSetListener {

    boolean editMode = false;
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
    ArrayList<String> opportunity_colleges = new ArrayList<String>();
    ArrayList<String> opportunity_categories = new ArrayList<String>();
    ArrayAdapter<String> adapterColleges ;
    ArrayAdapter<String> adapterCategories;
    Button submit;
    String expiration="";
    String info;
    Integer oppID;

    DatePickerDialog datePickerDialog;
    static EditText createOpportunityName;
    static Spinner createOpportunityOppCollege;
    static EditText createOpportunityJobTitle;
    static EditText createOpportunityNumberOfStudents;
    static EditText createOpportunityExpectedHoursPerWeek;
    static EditText createOpportunityDescription;
    static Spinner createOpportunityCategory;
    static EditText createOpportunityMinGPA;
    static Button createOpportunityExpirationDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_specific_opp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        editMode = false;
        datePickerDialog = new DatePickerDialog(
                this, EditSpecificOpp.this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        submit = (Button) findViewById(R.id.editSaveOpp);
        createOpportunityName = (EditText) findViewById(R.id.createOpportunityName);
        createOpportunityOppCollege = (Spinner) findViewById(R.id.createOpportunityOppCollege);
        createOpportunityJobTitle = (EditText) findViewById(R.id.createOpportunityJobTitle);
        createOpportunityNumberOfStudents = (EditText) findViewById(R.id.createOpportunityNumberOfStudents);
        createOpportunityExpectedHoursPerWeek = (EditText) findViewById(R.id.createOpportunityExpectedHoursPerWeek);
        createOpportunityCategory = (Spinner) findViewById(R.id.createOpportunityCategory);
        createOpportunityDescription = (EditText) findViewById(R.id.createOpportunityDescription);
        createOpportunityMinGPA = (EditText) findViewById(R.id.createOpportunityMinGPA);
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

        createOpportunityOppCollege.setAdapter(adapterColleges);
        createOpportunityCategory.setAdapter(adapterCategories);

        Intent intent = getIntent();
        createOpportunityName.setText(intent.getStringExtra("Name"));
        createOpportunityDescription.setText(intent.getStringExtra("Description"));
        createOpportunityJobTitle.setText(intent.getStringExtra("Position"));
        createOpportunityMinGPA.setText(intent.getStringExtra("minGPA"));
        createOpportunityOppCollege.setSelection(intent.getIntExtra("clg",0)-1);
        createOpportunityNumberOfStudents.setText(intent.getStringExtra("maxStudents"));
        createOpportunityExpectedHoursPerWeek.setText(intent.getStringExtra("hours"));
        createOpportunityCategory.setSelection(intent.getIntExtra("category",0)-1);
        oppID = Integer.parseInt(intent.getStringExtra("Id"));

        if(intent.getStringExtra("Expiration").contains("1969") || intent.getStringExtra("Expiration").contains("1970")){}
        else{createOpportunityExpirationDate.setText(intent.getStringExtra("Expiration"));}


        createOpportunityName.setEnabled(false);
        createOpportunityMinGPA.setEnabled(false);
        createOpportunityOppCollege.setEnabled(false);
        createOpportunityJobTitle.setEnabled(false);
        createOpportunityNumberOfStudents.setEnabled(false);
        createOpportunityExpectedHoursPerWeek.setEnabled(false);
        createOpportunityCategory.setEnabled(false);
        createOpportunityDescription.setEnabled(false);
        createOpportunityExpirationDate.setEnabled(false);

        createOpportunityName.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityOppCollege.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityJobTitle.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityNumberOfStudents.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityExpectedHoursPerWeek.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityCategory.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityDescription.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        createOpportunityExpirationDate.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
    }

    public void updateOpp(View view) {
        if(editMode == false) {
            editMode = true;
            submit.setText("Save Opportunity");

            createOpportunityName.setEnabled(true);
            createOpportunityMinGPA.setEnabled(true);
            createOpportunityOppCollege.setEnabled(true);
            createOpportunityJobTitle.setEnabled(true);
            createOpportunityNumberOfStudents.setEnabled(true);
            createOpportunityExpectedHoursPerWeek.setEnabled(true);
            createOpportunityCategory.setEnabled(true);
            createOpportunityDescription.setEnabled(true);
            createOpportunityExpirationDate.setEnabled(true);

            createOpportunityName.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityOppCollege.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityJobTitle.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityNumberOfStudents.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityExpectedHoursPerWeek.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityCategory.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityDescription.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            createOpportunityExpirationDate.setBackgroundResource(R.drawable.rounded_textbox_shadows);
        }

        else
        {
            editMode = false;

            oppName = createOpportunityName.getText().toString();
            jobTitle = createOpportunityJobTitle.getText().toString();
            maxNum1 = createOpportunityNumberOfStudents.getText().toString();
            hours1 = createOpportunityExpectedHoursPerWeek.getText().toString();
            details = createOpportunityDescription.getText().toString();
            college1 = Long.toString(createOpportunityOppCollege.getSelectedItemId()+1);
            category1 = Long.toString(createOpportunityCategory.getSelectedItemId()+1);
            gpa1 = createOpportunityMinGPA.getText().toString();
            //expiration = createOpportunityExpirationDate.getText().toString();

            submit.setText("Edit Opportunity");
            createOpportunityName.setEnabled(false);
            createOpportunityMinGPA.setEnabled(false);
            createOpportunityOppCollege.setEnabled(false);
            createOpportunityJobTitle.setEnabled(false);
            createOpportunityNumberOfStudents.setEnabled(false);
            createOpportunityExpectedHoursPerWeek.setEnabled(false);
            createOpportunityCategory.setEnabled(false);
            createOpportunityDescription.setEnabled(false);
            createOpportunityExpirationDate.setEnabled(false);

            createOpportunityName.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityOppCollege.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityJobTitle.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityNumberOfStudents.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityExpectedHoursPerWeek.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityCategory.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityDescription.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            createOpportunityExpirationDate.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);

            saveOpp(oppName,jobTitle,maxNum1,hours1,details,college1,category1,gpa1);



        }
    }

    public void saveOpp(String name, String title, String maxStudents, String hoursWeekly, String desc, String colleges, String categories, String minGPA)
    {
            String temp = readToken();
            RequestBody formBody = null;
            if(createOpportunityExpirationDate.getText().toString().equals("Select Date...")) {
                formBody = new FormBody.Builder()
                        .add("id", oppID.toString())
                        .add("name", name)
                        .add("description", desc)
                        .add("position", title)
                        .add("maxStudents", maxStudents)
                        .add("hoursWeekly", hoursWeekly)
                        .add("college", colleges)
                        .add("category", categories)
                        .add("minGPA", minGPA)
                        .build();
            }
            else
            {
                int date = (int) (expirationDate.getTime() / 1000);
                formBody = new FormBody.Builder()
                        .add("id", oppID.toString())
                        .add("name", name)
                        .add("description", desc)
                        .add("position", title)
                        .add("maxStudents", maxStudents)
                        .add("hoursWeekly", hoursWeekly)
                        .add("college", colleges)
                        .add("category", categories)
                        .add("minGPA", minGPA)
                        .add("deadline", Integer.toString(date))
                        .build();
                createOpportunityExpirationDate.setText("Select Date...");
            }
            try {
                Request request = new Request.Builder()
                        .url("https://web.njit.edu/~db329/resport/api/v1/edit_opportunity")
                        .header("Authorization", "Bearer " + temp)
                        .post(formBody)
                        .build();
                Response response = null;
                response = client.newCall(request).execute();
                String res = (response.body().string());
                try {
                    JSONObject responseJSON = new JSONObject(res);
                    String msg = responseJSON.getString("msg");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
    }

    public void getIds() {
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

    public void parseVariableInformation(String response) {
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

    public void showDatePickerDialog(View view)
    {
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        expiration = Integer.toString(i1+1) + "/" + Integer.toString(i2) + "/" + Integer.toString(i);
        createOpportunityExpirationDate.setText(expiration);
        expirationDate = new GregorianCalendar(i, i1, i2).getTime();
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
        getMenuInflater().inflate(R.menu.edit_specific_opp, menu);
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
                    Intent intent = new Intent(EditSpecificOpp.this, FacultyProfile.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_create) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditSpecificOpp.this, CreateOpportunity.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_applicants) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditSpecificOpp.this, FacultyViewListOfOpp.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        }else if (id == R.id.fac_edit_opp) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditSpecificOpp.this, EditOpportunity.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(EditSpecificOpp.this, ContactUs.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.fac_logout) {
            clearToken();
            Intent intent = new Intent(EditSpecificOpp.this, MainActivity.class);
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
