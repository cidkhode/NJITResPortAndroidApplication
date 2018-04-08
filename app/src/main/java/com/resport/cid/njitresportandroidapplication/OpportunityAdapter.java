package com.resport.cid.njitresportandroidapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rahul on 2/20/2018.
 */

public class OpportunityAdapter extends ArrayAdapter<Opportunity> {

    public OpportunityAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public OpportunityAdapter(Context context, int resource, List<Opportunity> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_opportunities, null);
        }

        Opportunity p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.opportunityTitle);
            TextView tt2 = (TextView) v.findViewById(R.id.opportunityDesc);
            TextView tt3 = (TextView) v.findViewById(R.id.createOpportunityCategory);
            TextView tt4 = (TextView) v.findViewById(R.id.createOpportunityOppCollege);
            TextView tt5 = (TextView) v.findViewById(R.id.createOpportunityNumberOfStudents);
            TextView tt6 = (TextView) v.findViewById(R.id.createOpportunityExpectedHoursPerWeek);
            TextView tt7 = (TextView) v.findViewById(R.id.studentGPA);
            TextView tt8 = (TextView) v.findViewById(R.id.opportunityProf);
            TextView tt9 = (TextView) v.findViewById(R.id.facultyEmail);
            TextView tt10 = (TextView) v.findViewById(R.id.category1);
            TextView tt11 = (TextView) v.findViewById(R.id.category2);
            TextView tt12 = (TextView) v.findViewById(R.id.opportunityExpirationDate);

            if (tt1 != null) {
                tt1.setText(p.getOppName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(p.getCategory());
            }
            if (tt4 != null) {
                tt4.setText(p.getOppCollege());
            }

            if (tt5 != null) {
                tt5.setText(p.getNumStudents());
            }

            if (tt6 != null) {
                tt6.setText(p.getWeeklyHours());
            }
            if (tt8 != null) {
                tt8.setText(p.getFacultyName());
            }

            if (tt9 != null) {
                tt9.setText(p.getFacultyUCID());
            }

            String category1 = p.getCategory().split("and")[0].trim();
            String category2 = p.getCategory().split("and")[1].trim();
            if (tt10 != null) {
                if(category1.equals("Paid")) {
                    tt10.setBackgroundResource(R.drawable.rounded_textbox_paid);
                }
                else if(category1.equals("Unpaid")) {
                    tt10.setBackgroundResource(R.drawable.rounded_textbox_unpaid);
                }
                tt10.setText(category1);
            }

            if (tt11 != null) {
                if(category2.equals("For Credit")) {
                    tt11.setBackgroundResource(R.drawable.rounded_textbox_for_credit);
                }
                else if(category2.equals("No Credit")) {
                    tt11.setBackgroundResource(R.drawable.rounded_textbox_not_for_credit);
                }
                tt11.setText(category2);
            }

            if (tt12 != null) {
                if(p.getExpiration().contains("1970") || p.getExpiration().contains("1969")) {
                    tt12.setTextColor(tt12.getResources().getColor(R.color.accepted));
                    tt12.setBackgroundResource(R.drawable.rounded_textbox_green_outline);
                    tt12.setText("No Expiration!");
                }
                else {
                    tt12.setTextColor(tt12.getResources().getColor(R.color.colorPrimary));
                    tt12.setBackgroundResource(R.drawable.rounded_textbox_red_outline);
                    tt12.setText(p.getExpiration());
                }
            }
        }

        return v;
    }
}
