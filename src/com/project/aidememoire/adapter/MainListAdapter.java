package com.project.aidememoire.adapter;

import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.database.loader.DataBaseLoader;
import com.project.aidememoire.fragment.ListFragment;

public class MainListAdapter extends CursorAdapter implements LoaderCallbacks<Cursor>{

	private final String TAG = "PersonListAdapter";
	public static final int LOADER_ID = 0;
	
	private View view;
	private TextView name;
	private TextView surname;
	private TextView sum;
	private TextView date;
	private LayoutInflater layoutInflater;
	private Context context;
	private DatabaseApi databaseApi;
	private LoaderManager loaderManager;
	
	public MainListAdapter(Context context, LoaderManager loaderManager) {
		super(context, null, false);
		this.context = context;
		databaseApi = DatabaseApi.getInstance(context);
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
		
		if(cursor.getString(4).equals("credit")){
			sum.setTextAppearance(context, R.style.GreenFontStyle);
		}
		else {
			sum.setTextAppearance(context, R.style.RedFontStyle);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		layoutInflater = LayoutInflater.from(context);
		view = layoutInflater.inflate(R.layout.main_item_list, null);
		
		return view;
	}



	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		if(!databaseApi.isOpen()){
			databaseApi.open();
		}
		return new DataBaseLoader(context, databaseApi, bundle);
	}



	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		swapCursor(cursor);
	}



	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		swapCursor(null);
	}

}
