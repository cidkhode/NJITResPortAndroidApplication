package com.resport.cid.njitresportandroidapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Cid on 4/2/2018.
 */

public class ApplicationAdapter extends ArrayAdapter<Application> {

    public ApplicationAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ApplicationAdapter(Context context, int resource, List<Application> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_applications, null);
        }

        Application p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.opportunityTitle);
            TextView tt2 = (TextView) v.findViewById(R.id.opportunityDesc);
            TextView tt3 = (TextView) v.findViewById(R.id.createOpportunityCategory);
            TextView tt4 = (TextView) v.findViewById(R.id.createOpportunityOppCollege);
            TextView tt5 = (TextView) v.findViewById(R.id.createOpportunityNumberOfStudents);
            TextView tt6 = (TextView) v.findViewById(R.id.createOpportunityExpectedHoursPerWeek);
            TextView tt7 = (TextView) v.findViewById(R.id.studentGPA);
            TextView tt8 = (TextView) v.findViewById(R.id.opportunityProf);
            TextView tt9 = (TextView) v.findViewById(R.id.facultyProfileEmail);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(p.getCollege());
            }
            if (tt4 != null) {
                tt4.setText(p.getFacultyName());
            }

            if (tt5 != null) {
                tt5.setText(p.getEmail());
            }
        }

        return v;
    }
}
