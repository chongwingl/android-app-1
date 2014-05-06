package com.project.aidememoire.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.aidememoire.R;

public class PartialListAdapter extends CursorAdapter {

	public static final int LOADER_ID = 10;
	
	private View view;
	private TextView sum;
	private TextView date;

	public PartialListAdapter(Context context) {
		super(context, null, false);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
        sum = (TextView) view.findViewById(R.id.money_sum);
        date = (TextView) view.findViewById(R.id.money_date);
        
        String [] columns = cursor.getColumnNames();
        for(int i=0; i<columns.length; i++){
        	if(columns[i].equals("montant")){
        		sum.setText(cursor.getString(i));
        	}
        	else if(columns[i].equals("date")){
        		date.setText(cursor.getString(i));
        	}
        	else if(columns[i].equals("type")){
        		if(cursor.getString(i).equals("credit")){
        			sum.setTextAppearance(context, R.style.GreenFontStyle);
        		}
        		else {
        			sum.setTextAppearance(context, R.style.RedFontStyle);
        		}
        	}
        }
		
	}
	
	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		view = LayoutInflater.from(context).inflate(R.layout.partial_item_list, null);
		
		return view;
	}

}
