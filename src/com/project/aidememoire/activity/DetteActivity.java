package com.project.aidememoire.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class DetteActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Dette tab");
        setContentView(textview);
    }


}
