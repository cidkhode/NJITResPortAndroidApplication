package com.resport.cid.njitresportandroidapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ritupatel on 4/5/18.
 */

public class FacultyListApplicantAdapter extends ArrayAdapter<FacultyListApplicant> {

    public FacultyListApplicantAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FacultyListApplicantAdapter(Context context, int resource, List<FacultyListApplicant> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_faculty_list_app, null);
        }

        FacultyListApplicant p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.faculty_name);
            TextView tt2 = (TextView) v.findViewById(R.id.faculty_major);
            TextView tt3 = (TextView) v.findViewById(R.id.faculty_gpa);
            TextView tt4 = (TextView) v.findViewById(R.id.faculty_class);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getMajor());
            }

            if (tt3 != null) {
                tt3.setText(String.valueOf(p.getGpa()));
            }
            if (tt4 != null) {
                tt4.setText(p.getClass1());
            }
        }

        return v;
    }
}
