package com.research.njit.njitresportandroidapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.FileSystem;
import java.util.ArrayList;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class StudentProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    boolean editMode = false;
    OkHttpClient client = new OkHttpClient();
    Button edit_save;
    Button studentSelectFile;
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
    static String fileName = "";
    static File resumeFile;
    ArrayList<String> student_colleges = new ArrayList<String>();
    ArrayList<String> student_majors = new ArrayList<String>();
    ArrayList<String> student_classes = new ArrayList<String>();

    static TextView resume_file_path;
    static TextView student_name;
    static TextView student_email;
    static Spinner student_major;
    static EditText student_gpa;
    static Spinner student_class;
    static Spinner student_college;
    static CheckBox student_honors;

    ArrayList<File> resumeFileNames; /*cannot be String,
                                         since they are immutable,
                                         and so if user keeps changing
                                         files for resumes, the filepaths
                                         will also change, but the String
                                         variable for the filepath would
                                         be the first filepath, and would
                                         not change after that*/

    ArrayAdapter<String> adapterColleges;
    ArrayAdapter<String> adapterMajors;
    ArrayAdapter<String> adapterClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("---------REQUESTING PERMISSIONS-------");
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 10001);
        }

        resumeFileNames = new ArrayList<File>();

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

        edit_save = (Button) findViewById(R.id.studentEditSave);
        studentSelectFile = (Button) findViewById(R.id.studentSelectFile);
        resume_file_path = (TextView) findViewById(R.id.filePath);

        student_name = (TextView) findViewById(R.id.studentName);
        student_email = (TextView) findViewById(R.id.studentEmail);
        student_major = (Spinner) findViewById(R.id.spinner5);
        student_gpa = (EditText) findViewById(R.id.studentGPA);
        student_class = (Spinner) findViewById(R.id.studentClassStanding);
        student_college = (Spinner) findViewById(R.id.studentCollege);
        student_honors = (CheckBox) findViewById(R.id.studentHonors);

        student_name.setEnabled(false);
        student_email.setEnabled(false);
        student_major.setEnabled(false);
        student_gpa.setEnabled(false);
        student_class.setEnabled(false);
        student_college.setEnabled(false);
        student_honors.setEnabled(false);

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

        studentSelectFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editMode == false) {
                    Toast.makeText(view.getContext(), "Please Click \"Edit Profile\" to Upload a Resume",Toast.LENGTH_LONG).show();
                }
                else {
                    new MaterialFilePicker()
                            .withActivity(StudentProfile.this)
                            .withRequestCode(1000)
                            .withFilter(Pattern.compile(".*\\.pdf")) // Filtering files and directories by file name using regexp
                            .withFilterDirectories(false) // Set directories filterable (false by default)
                            .withHiddenFiles(true) // Show hidden files and folders
                            .start();
                }
            }
        });

        loadProfile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            resume_file_path.setText("Filepath: " + filePath);
            File file = new File(filePath);
            if(resumeFileNames.size() == 0)
                resumeFileNames.add(file);
            else if(resumeFileNames.size() == 1)
            {
                resumeFileNames.set(0, file);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1001: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission granted!", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
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
            edit_save.setText("Save Profile");

            student_major.setEnabled(true);
            student_gpa.setEnabled(true);
            student_class.setEnabled(true);
            student_college.setEnabled(true);
            student_honors.setEnabled(true);
            student_gpa.setSelection(student_gpa.getText().length());

            student_major.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            student_gpa.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            student_class.setBackgroundResource(R.drawable.rounded_textbox_shadows);
            student_college.setBackgroundResource(R.drawable.rounded_textbox_shadows);
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
            {
                honors=false;
                honors1 = Boolean.toString(honors);
            }

            if (!TextUtils.isEmpty(gpa1)&&( Float.parseFloat(gpa1) > 4.0 || Float.parseFloat(gpa1) < 0.0)) {
                Toast.makeText(getApplicationContext(), "Please enter a valid GPA!", Toast.LENGTH_LONG).show();
            }

            else {
                editMode = false;
                edit_save.setText("Edit Profile");
                student_major.setEnabled(false);
                student_gpa.setEnabled(false);
                student_class.setEnabled(false);
                student_college.setEnabled(false);
                student_honors.setEnabled(false);

                student_major.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                student_gpa.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                student_class.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);
                student_college.setBackgroundResource(R.drawable.rounded_textbox_faded_shadows);

                saveProfile(gpa1,major1,class1,college1, honors1);
            }
        }
    }

    public void saveProfile(String gpa1, String major1, String class1, String college1, String honors1)
    {
        String temp = readToken();
        MultipartBody.Builder formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("gpa", gpa1)
                .addFormDataPart("major", major1)
                .addFormDataPart("class", class1)
                .addFormDataPart("college", college1)
                .addFormDataPart("honors", honors1);
        if(resumeFileNames.size() > 0) {
            resume_file_path.setText(resumeFileNames.get(0).getName());
            formBody.addFormDataPart("resume",resumeFileNames.get(0).getName(),
                            RequestBody.create(MediaType.parse("application/pdf"),
                            resumeFileNames.get(0)));
        }

        MultipartBody form = formBody.build();
        try {
            Request request = new Request.Builder()
                    .url("https://web.njit.edu/~db329/resport/api/v1/user")
                    .header("Authorization", "Bearer " + temp)
                    .post(form)
                    .build();
            Response response = null;
            response = client.newCall(request).execute();
            System.out.println(response.body().string());
            Toast.makeText(this, "Save successful!", Toast.LENGTH_LONG).show();
        } catch (IOException exception) {
            Toast.makeText(this, "Save failed.", Toast.LENGTH_LONG).show();
        }
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (id == R.id.stu_profile) {
        } else if (id == R.id.stu_browse) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent =new Intent(StudentProfile.this, StudentOpportunitiesList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_status) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent =new Intent(StudentProfile.this, StudentApplicationsList.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 250);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_contact) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent =new Intent(StudentProfile.this, ContactUsStudent.class).addFlags(FLAG_ACTIVITY_NO_ANIMATION );
                    startActivity(intent);
                    finish();
                }
            }, 200);
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.stu_logout) {
            clearToken();
            Intent intent = new Intent(StudentProfile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            drawer.closeDrawer(GravityCompat.START);
            drawer.closeDrawers();
            startActivity(intent);
        }

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
            exception.printStackTrace();
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
                lname = dataJSON.getString("lname");
                student_name.setText(fname + " " + lname);
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
                student_honors.setChecked(honors);

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
}
