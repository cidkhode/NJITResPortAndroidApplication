package com.resport.cid.njitresportandroidapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class StudentApplicationsBrowse extends AppCompatActivity {

    ArrayList<Opportunity> opps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_applications_browse);

        ListView listView = (ListView) findViewById(R.id.studentAppsViewListView);

        opps = new ArrayList<>();
        opps.add(new Opportunity("Research Opportunity in Machine Learning", "College of Computing Sciences","Research Assistant",
                2, 3, "Machine Learning opportunity using R and big data technologies like Hadoop.","Prof. ML Profes", "abc123"));

        opps.add(new Opportunity("Biological Study in Fish", "College of Science and Liberal Arts","Volunteer",
                4, 5, "Studying fish and other animals.","Prof. Biol Dept", "defg"));

        OpportunityAdapter customAdapter = new OpportunityAdapter(this, R.layout.layout_opportunities, opps);
        listView.setAdapter(customAdapter);

    }
}
