package com.resport.cid.njitresportandroidapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goToStudentProfile(View view)
    {
        Intent intent = new Intent(MainActivity.this, StudentProfile.class);
        startActivity(intent);
    }

    public void goToFacultyProfile(View view)
    {
        Intent intent = new Intent(MainActivity.this, FacultyProfile.class);
        startActivity(intent);
    }

    public void goStudentApplicationSelect(View view)
    {
        Intent intent = new Intent(MainActivity.this, StudentApplicationSelect.class);
        startActivity(intent);
    }

    public void goStudentApplicationsView(View view)
    {
        Intent intent = new Intent(MainActivity.this, StudentApplicationsBrowse.class);
        startActivity(intent);
    }

    public void goStudentOpportunitySelect(View view)
    {
        Intent intent = new Intent(MainActivity.this, StudentOpportunitySelect.class);
        startActivity(intent);
    }

    public void goStudentOpportunitiesView(View view)
    {
        Intent intent = new Intent(MainActivity.this, StudentOpportunitiesBrowse.class);
        startActivity(intent);
    }

}
