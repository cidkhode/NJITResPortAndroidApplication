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

public class FacultyOppAdapter extends ArrayAdapter<FacultyOpp> {

    public FacultyOppAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public FacultyOppAdapter(Context context, int resource, List<FacultyOpp> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_faculty_opp, null);
        }

        FacultyOpp p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.faculty_opportunityTitle);
            TextView tt2 = (TextView) v.findViewById(R.id.faculty_opportunityDesc);
            TextView tt3 = (TextView) v.findViewById(R.id.faculty_opportunityPosition);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(p.getPosition());
            }
        }

        return v;
    }
}
