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
            TextView tt4 = (TextView) v.findViewById(R.id.faculty_opportunityExpirationDate);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(p.getPosition());
            }

            if (tt4 != null) {
                if(p.getExpiration().contains("1970") || p.getExpiration().contains("1969")) {
                    tt4.setTextColor(tt4.getResources().getColor(R.color.accepted));
                    tt4.setBackgroundResource(R.drawable.rounded_textbox_green_outline);
                    tt4.setText("No Expiration!");
                }
                else {
                    tt4.setTextColor(tt4.getResources().getColor(R.color.colorPrimary));
                    tt4.setBackgroundResource(R.drawable.rounded_textbox_red_outline);
                    tt4.setText(p.getExpiration());
                }
            }
        }

        return v;
    }
}
