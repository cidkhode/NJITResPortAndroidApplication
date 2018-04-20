package com.resport.cid.njitresportandroidapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class MajorsAdapter extends ArrayAdapter<MultiSelectMajors> {
    private Context mContext;
    private ArrayList<MultiSelectMajors> listState;
    private MajorsAdapter majorsAdapter;
    private boolean isFromView = false;
    public static ArrayList<String> checkedList;
    public static ViewHolderSpinner holder;

    public MajorsAdapter(Context context, int resource, List<MultiSelectMajors> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<MultiSelectMajors>) objects;
        this.majorsAdapter = this;
        checkedList = new ArrayList<String>();
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public ViewHolderSpinner getHolder() {
        return holder;
    }

    public ArrayList<String> getCheckedList() {
        return checkedList;
    }

    public View getCustomView(final int position, View convertView,
                              ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spinner_item, null);
            holder = new ViewHolderSpinner();
            holder.mTextView = (TextView) convertView
                    .findViewById(R.id.text);
            holder.mCheckBox = (CheckBox) convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderSpinner) convertView.getTag();
        }

        holder.mTextView.setText(listState.get(position).getTitle());

        isFromView = true;
        holder.mCheckBox.setChecked(listState.get(position).isSelected());
        isFromView = false;

        holder.mCheckBox.setTag(position);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int getPosition = (Integer) buttonView.getTag();
                checkedList.add(holder.mTextView.getText().toString());
                //System.out.println("----------CHECKED----------- " + checkedList.toString());
            }
        });
        return convertView;
    }
}
