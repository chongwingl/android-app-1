package com.project.aidememoire.adapter;

import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.adapter.database.loader.DataBaseLoader;

public class PersonListAdapter extends CursorAdapter implements LoaderCallbacks<Cursor>{

	private final String TAG = "PersonListAdapter";
	public static final int LOADER_ID = 0;
	
	private View view;
	private TextView name;
	private TextView surname;
	private TextView sum;
	private TextView date;
	private LayoutInflater layoutInflater;
	private Context context;
	private DataBaseAdapter mDBHelper;
	private Cursor cursor;
	private LoaderManager loaderManager;
	
	public PersonListAdapter(Context context, LoaderManager loaderManager) {
		super(context, null, false);
		this.context = context;
		mDBHelper = new DataBaseAdapter(context);
		loaderManager.initLoader(LOADER_ID, null, this).forceLoad();
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



	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		if(!mDBHelper.isOpen()){
			mDBHelper.open();
		}
		return new DataBaseLoader(context, mDBHelper);
	}



	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		this.cursor = cursor;
		swapCursor(cursor);
	}



	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		swapCursor(null);
	}

}
