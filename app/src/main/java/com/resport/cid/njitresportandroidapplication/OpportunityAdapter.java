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
            TextView tt3 = (TextView) v.findViewById(R.id.opportunityProf);

            if (tt1 != null) {
                tt1.setText(p.getOppName());
            }

            if (tt2 != null) {
                tt2.setText(p.getDescription());
            }

            if (tt3 != null) {
                tt3.setText(p.getOppCollege());
            }
        }

        return v;
    }
}
