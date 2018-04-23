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

public class TagAdapter extends ArrayAdapter<String> {

    public TagAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TagAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.layout_tags, null);
        }

        String p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.tag1);
            if (tt1 != null) {
                tt1.setText(p);
            }
        }
        return v;
    }
}
