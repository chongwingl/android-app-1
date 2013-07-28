package com.project.aidememoire.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class CreditActivity extends Activity{
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textview = new TextView(this);
        textview.setText("This is the Credit tab");
        setContentView(textview);
    }


}
