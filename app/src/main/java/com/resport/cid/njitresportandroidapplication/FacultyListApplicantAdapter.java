package com.resport.cid.njitresportandroidapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
            TextView tt5 = (TextView) v.findViewById(R.id.faculty_status);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getMajor());
            }

            double gpa = p.getGpa();
            if (tt3 != null) {
                if(gpa >= 3.50)
                {
                    tt3.setTextColor(tt3.getResources().getColor(R.color.amazingGPA));
                    tt3.setBackgroundResource(R.drawable.rounded_textbox_amazing_gpa);
                }
                else if(gpa >= 3.00 && gpa < 3.50)
                {
                    tt3.setTextColor(tt3.getResources().getColor(R.color.goodGPA));
                    tt3.setBackgroundResource(R.drawable.rounded_textbox_good_gpa);
                }
                else if(gpa >= 2.50 && gpa < 3.00)
                {
                    tt3.setTextColor(tt3.getResources().getColor(R.color.badGPA));
                    tt3.setBackgroundResource(R.drawable.rounded_textbox_bad_gpa);
                }
                else if(gpa < 2.50)
                {
                    tt3.setTextColor(tt3.getResources().getColor(R.color.terribleGPA));
                    tt3.setBackgroundResource(R.drawable.rounded_textbox_terrible_gpa);
                }
                tt3.setText("GPA: " + Double.toString(gpa));
            }

            if (tt4 != null) {
                tt4.setText(p.getClassStanding());
            }

            int status = p.getStatus();
            if (tt5 != null) {
                if(status == 0) {
                    tt5.setTextColor(tt5.getResources().getColor(R.color.pending));
                    tt5.setBackgroundResource(R.drawable.rounded_textbox_orange_outline);
                    tt5.setText("Pending");
                } else if(status == 1) {
                    tt5.setTextColor(tt5.getResources().getColor(R.color.accepted));
                    tt5.setBackgroundResource(R.drawable.rounded_textbox_green_outline);
                    tt5.setText("Accepted");
                } else if(status == -1) {
                    tt5.setTextColor(tt5.getResources().getColor(R.color.colorPrimary));
                    tt5.setBackgroundResource(R.drawable.rounded_textbox_red_outline);
                    tt5.setText("Denied");
                }
            }

        }

        return v;
    }
}
