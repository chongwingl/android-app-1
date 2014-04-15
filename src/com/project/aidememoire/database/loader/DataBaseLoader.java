package com.project.aidememoire.database.loader;

import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.fragment.ListFragment;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class DataBaseLoader extends AsyncTaskLoader<Cursor> {
	
	private final static String TAG = "DataBaseLoader"; 
	private DatabaseApi databaseApi;
	private Bundle bundle;

	public DataBaseLoader(Context context, DatabaseApi databaseApi, Bundle bundle) {
		super(context);
		this.databaseApi = databaseApi;
		this.bundle = bundle;
	}

	@Override
	public Cursor loadInBackground() {
		if(bundle != null){
			String action = bundle.getString(ListFragment.SORT_FILTER);
			if(action == DataBaseAdapter.ORDER_BY_DATE
					|| action == DataBaseAdapter.ORDER_BY_NAME
					|| action == DataBaseAdapter.ORDER_BY_SUM
					|| action == DataBaseAdapter.ORDER_BY_SURNAME){
				return databaseApi.fetchAllPersonAndMoneyCursor(action, null);
			}
			else if(action == DataBaseAdapter.FILTER_BY_DATE
					|| action == DataBaseAdapter.FILTER_BY_NAME
					|| action == DataBaseAdapter.FILTER_BY_SUM
					|| action == DataBaseAdapter.FILTER_BY_SURNAME){
				String filter = bundle.getString(ListFragment.FILTER_DATA);
				return databaseApi.fetchAllPersonAndMoneyCursor(action, filter);
			}
		}
		return databaseApi.fetchAllPersonAndMoneyCursor();
	}


}
