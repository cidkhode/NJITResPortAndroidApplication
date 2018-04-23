package com.research.njit.njitresportandroidapplication;

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
            TextView tt1 = (TextView) v.findViewById(R.id.applied_opportunityTitle);
            TextView tt2 = (TextView) v.findViewById(R.id.applied_opportunityDesc);
            TextView tt3 = (TextView) v.findViewById(R.id.applied_opportunityProf);
            TextView tt4 = (TextView) v.findViewById(R.id.category1_applied);
            TextView tt5 = (TextView) v.findViewById(R.id.category2_applied);
            TextView tt6 = (TextView) v.findViewById(R.id.applied_opportunityStatus);

            if (tt1 != null) {
                tt1.setText(p.getName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(p.getCollege());
            }

            String category1 = p.getCategory().split("and")[0].trim();
            String category2 = p.getCategory().split("and")[1].trim();
            if (tt4 != null) {
                if(category1.equals("Paid")) {
                    tt4.setBackgroundResource(R.drawable.rounded_textbox_paid);
                }
                else if(category1.equals("Unpaid")) {
                    tt4.setBackgroundResource(R.drawable.rounded_textbox_unpaid);
                }
                tt4.setText(category1);
            }

            if (tt5 != null) {
                if(category2.equals("For Credit")) {
                    tt5.setBackgroundResource(R.drawable.rounded_textbox_for_credit);
                }
                else if(category2.equals("No Credit")) {
                    tt5.setBackgroundResource(R.drawable.rounded_textbox_not_for_credit);
                }
                tt5.setText(category2);
            }

            int status = p.getStatus();
            if (tt6 != null) {
                if(status == 0) {
                    tt6.setTextColor(tt6.getResources().getColor(R.color.pending));
                    tt6.setBackgroundResource(R.drawable.rounded_textbox_orange_outline);
                    tt6.setText("Pending");
                } else if(status == 1) {
                    tt6.setTextColor(tt6.getResources().getColor(R.color.accepted));
                    tt6.setBackgroundResource(R.drawable.rounded_textbox_green_outline);
                    tt6.setText("Accepted!");
                } else if(status == -1) {
                    tt6.setTextColor(tt6.getResources().getColor(R.color.colorPrimary));
                    tt6.setBackgroundResource(R.drawable.rounded_textbox_red_outline);
                    tt6.setText("Denied :(");
                }
            }
        }

        return v;
    }
}
