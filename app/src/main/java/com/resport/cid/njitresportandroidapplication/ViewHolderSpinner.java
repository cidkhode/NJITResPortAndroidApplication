package com.resport.cid.njitresportandroidapplication;

import android.widget.CheckBox;
import android.widget.TextView;

public class ViewHolderSpinner {
    public TextView mTextView;
    public CheckBox mCheckBox;

    public String getTextView() {
        return mTextView.getText().toString();
    }

    public boolean getChecked() {
        return mCheckBox.isChecked();
    }
}
