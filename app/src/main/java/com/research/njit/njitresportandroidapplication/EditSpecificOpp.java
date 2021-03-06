package com.research.njit.njitresportandroidapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
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
    ArrayList<String> opportunity_majors = new ArrayList<String>();
    List<String> selectedKeys;
    List<String> selectedValues;

    ArrayAdapter<String> adapterColleges ;
    ArrayAdapter<String> adapterCategories;

    Button submit;
    String expiration="";
    String info;
    Integer oppID;
    String expiryDate;
    String tempDate;
    Integer eDate;

    DatePickerDialog datePickerDialog;
    static EditText editOpportunityName;
    static Spinner editOpportunityOppCollege;
    static EditText editOpportunityJobTitle;
    static EditText editOpportunityNumberOfStudents;
    static EditText editOpportunityExpectedHoursPerWeek;
    static EditText editOpportunityDescription;
    static Spinner editOpportunityCategory;
    static MultiSelectionSpinner editOpportunityMajors;
    static EditText editOpportunityMinGPA;
    static Button editOpportunityExpirationDate;

    String tags[];

    HashMap<String, String> completeDegrees = new HashMap<String, String>();
    HashMap<String, String> selectedDegrees = new HashMap<String, String>();

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

        datePickerDialog = new DatePickerDialog(
                this, EditSpecificOpp.this, Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        submit = (Button) findViewById(R.id.editSaveOpp);
        editOpportunityName = (EditText) findViewById(R.id.editOpportunityName);
        editOpportunityOppCollege = (Spinner) findViewById(R.id.editOpportunityOppCollege);
        editOpportunityJobTitle = (EditText) findViewById(R.id.editOpportunityJobTitle);
        editOpportunityNumberOfStudents = (EditText) findViewById(R.id.editOpportunityNumberOfStudents);
        editOpportunityExpectedHoursPerWeek = (EditText) findViewById(R.id.editOpportunityExpectedHoursPerWeek);
        editOpportunityCategory = (Spinner) findViewById(R.id.editOpportunityCategory);
        editOpportunityDescription = (EditText) findViewById(R.id.editOpportunityDescription);
        editOpportunityMinGPA = (EditText) findViewById(R.id.editOpportunityMinGPA);
        editOpportunityExpirationDate = (Button) findViewById(R.id.editOpportunityExpirationDate);
        editOpportunityMajors = (MultiSelectionSpinner) findViewById(R.id.editOpportunityMajors);

        editOpportunityName.setSelection(editOpportunityName.getText().length());
        editOpportunityJobTitle.setSelection(editOpportunityJobTitle.getText().length());
        editOpportunityNumberOfStudents.setSelection(editOpportunityNumberOfStudents.getText().length());
        editOpportunityExpectedHoursPerWeek.setSelection(editOpportunityExpectedHoursPerWeek.getText().length());
        editOpportunityMinGPA.setSelection(editOpportunityMinGPA.getText().length());

        getIds();
        adapterColleges = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, opportunity_colleges);
        adapterColleges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        adapterCategories = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, opportunity_categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        editOpportunityOppCollege.setAdapter(adapterColleges);
        editOpportunityCategory.setAdapter(adapterCategories);
        editOpportunityMajors.setItems(opportunity_majors);

        editOpportunityDescription.setOnTouchListener(new View.OnTouchListener() {
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
        editOpportunityName.setText(intent.getStringExtra("Name"));
        editOpportunityDescription.setText(intent.getStringExtra("Description"));
        editOpportunityJobTitle.setText(intent.getStringExtra("Position"));
        editOpportunityMinGPA.setText(intent.getStringExtra("minGPA"));
        editOpportunityOppCollege.setSelection(intent.getIntExtra("clg",0)-1);
        editOpportunityNumberOfStudents.setText(intent.getStringExtra("maxStudents"));
        editOpportunityExpectedHoursPerWeek.setText(intent.getStringExtra("hours"));
        editOpportunityCategory.setSelection(intent.getIntExtra("category",0)-1);
        oppID = Integer.parseInt(intent.getStringExtra("Id"));
        eDate = Integer.parseInt(intent.getStringExtra("expirationInt"));
        tags = intent.getStringArrayExtra("tags");

        selectedKeys = Arrays.asList(tags);
        selectedValues = new ArrayList<String>();
        Iterator<String> keyIterator = selectedKeys.iterator();
        while(keyIterator.hasNext()) {
            String key = keyIterator.next();
            String value = completeDegrees.get(key);
            selectedValues.add(value);
        }
        editOpportunityMajors.setSelection(selectedValues);

        if(intent.getStringExtra("Expiration").contains("1969") || intent.getStringExtra("Expiration").contains("1970")){

        }
        else {
            tempDate = intent.getStringExtra("Expiration");
            editOpportunityExpirationDate.setText(tempDate);
        }
        editOpportunityName.setEnabled(false);
        editOpportunityMinGPA.setEnabled(false);
        editOpportunityOppCollege.setEnabled(false);
        editOpportunityJobTitle.setEnabled(false);
        editOpportunityNumberOfStudents.setEnabled(false);
        editOpportunityExpectedHoursPerWeek.setEnabled(false);
        editOpportunityCategory.setEnabled(false);
        editOpportunityDescription.setEnabled(false);
        editOpportunityExpirationDate.setEnabled(false);
        editOpportunityMajors.setEnabled(false);

        editOpportunityName.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityOppCollege.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityJobTitle.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityNumberOfStudents.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityExpectedHoursPerWeek.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityCategory.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityDescription.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityExpirationDate.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
        editOpportunityMajors.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
    }

    public void updateOpp(View view) {
        if(editMode == false) {
            editMode = true;
            submit.setText("Save Opportunity");

            editOpportunityName.setEnabled(true);
            editOpportunityMinGPA.setEnabled(true);
            editOpportunityOppCollege.setEnabled(true);
            editOpportunityJobTitle.setEnabled(true);
            editOpportunityNumberOfStudents.setEnabled(true);
            editOpportunityExpectedHoursPerWeek.setEnabled(true);
            editOpportunityCategory.setEnabled(true);
            editOpportunityMajors.setEnabled(true);
            editOpportunityDescription.setEnabled(true);
            editOpportunityExpirationDate.setEnabled(true);

            editOpportunityName.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityOppCollege.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityJobTitle.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityNumberOfStudents.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityExpectedHoursPerWeek.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityCategory.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityDescription.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityMajors.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            editOpportunityExpirationDate.setBackgroundResource(R.drawable.rounded_textbox_shadows);
        }

        else
        {
            editMode = false;
            oppName = editOpportunityName.getText().toString();
            jobTitle = editOpportunityJobTitle.getText().toString();
            maxNum1 = editOpportunityNumberOfStudents.getText().toString();
            hours1 = editOpportunityExpectedHoursPerWeek.getText().toString();
            details = editOpportunityDescription.getText().toString();
            college1 = Long.toString(editOpportunityOppCollege.getSelectedItemId()+1);
            category1 = Long.toString(editOpportunityCategory.getSelectedItemId()+1);
            gpa1 = editOpportunityMinGPA.getText().toString();
            expiryDate = editOpportunityExpirationDate.getText().toString();

            StringBuilder sbTemp = new StringBuilder();
            for(String s: editOpportunityMajors.getSelectedStrings()) {
                if(completeDegrees.containsValue(s)) {
                    for (String key : completeDegrees.keySet()) {
                        if (completeDegrees.get(key).equals(s)) {
                            selectedDegrees.put(key, s);
                        }
                    }
                }
            }
            if(completeDegrees.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Add at least 1 tag(s) please!", Toast.LENGTH_LONG).show();
            } else {
                StringBuilder sb = new StringBuilder();
                Iterator it = selectedDegrees.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry key_values = (Map.Entry) it.next();
                    if (it.hasNext()) {
                        sb.append(key_values.getKey() + ",");
                    } else sb.append(key_values.getKey());
                }
                String tagString = sb.toString();
                saveOpp(oppName, jobTitle, maxNum1, hours1, details, college1, category1, gpa1, expiryDate, eDate, tagString);

                submit.setText("Edit Opportunity");
                editOpportunityName.setEnabled(false);
                editOpportunityMinGPA.setEnabled(false);
                editOpportunityOppCollege.setEnabled(false);
                editOpportunityJobTitle.setEnabled(false);
                editOpportunityNumberOfStudents.setEnabled(false);
                editOpportunityExpectedHoursPerWeek.setEnabled(false);
                editOpportunityCategory.setEnabled(false);
                editOpportunityDescription.setEnabled(false);
                editOpportunityMajors.setEnabled(false);
                editOpportunityExpirationDate.setEnabled(false);

                editOpportunityName.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityOppCollege.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityJobTitle.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityNumberOfStudents.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityExpectedHoursPerWeek.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityCategory.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityDescription.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityMinGPA.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityExpirationDate.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                editOpportunityMajors.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
            }
        }
    }

    public void saveOpp(String name, String title, String maxStudents, String hoursWeekly, String desc, String colleges, String categories, String minGPA, String expiry, Integer eDateInt, String tagString)
    {
        String temp = readToken();
            RequestBody formBody = null;
            if(expiry.equals("Select Date...")) {
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
                        .add("tags", tagString)
                        .build();
            }
            else if(expiry.equals(tempDate)) {

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
                        .add("deadline", eDateInt.toString())
                        .add("tags", tagString)
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
                        .add("tags", tagString)
                        .build();
                editOpportunityExpirationDate.setText("Select Date...");
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

                JSONObject response1 = new JSONObject(res);
                String msg = response1.getString("msg");
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();


            } catch (IOException exception) {
                exception.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
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

                JSONArray majorsInfo = dataJSON.getJSONArray("degrees");
                for(int i=0;i<majorsInfo.length();i++) {
                    completeDegrees.put(majorsInfo.getJSONObject(i).getString("id"), majorsInfo.getJSONObject(i).getString("degree"));
                    opportunity_majors.add(majorsInfo.getJSONObject(i).getString("degree"));
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
        editOpportunityExpirationDate.setText(expiration);
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
