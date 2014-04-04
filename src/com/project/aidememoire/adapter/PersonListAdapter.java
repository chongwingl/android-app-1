package com.project.aidememoire.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.aidememoire.R;

public class PersonListAdapter extends CursorAdapter{

	private final String TAG = "PersonListAdapter";
	
	private View view;
	private TextView name;
	private TextView surname;
	private TextView sum;
	private TextView date;
	private LayoutInflater layoutInflater;
	private Context context;
	
	public PersonListAdapter(Context context, Cursor cursor, boolean autoRequery) {
		super(context, cursor, autoRequery);
		// TODO Utiliser un loader
		this.context = context;
	}
	


	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		name = (TextView) view.findViewById(R.id.person_name);
        surname = (TextView) view.findViewById(R.id.person_surname);
        sum = (TextView) view.findViewById(R.id.money_sum);
        date = (TextView) view.findViewById(R.id.money_date);
        
        name.setText(cursor.getString(6));
        surname.setText(cursor.getString(7));
		sum.setText(cursor.getString(3));
		date.setText(cursor.getString(2));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		layoutInflater = LayoutInflater.from(context);
		view = layoutInflater.inflate(R.layout.person_list, null);
		
		return view;
	}


}
