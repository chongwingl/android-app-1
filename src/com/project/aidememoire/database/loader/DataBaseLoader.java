package com.project.aidememoire.database.loader;

import com.project.aidememoire.adapter.database.DataBaseAdapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

public class DataBaseLoader extends AsyncTaskLoader<Cursor> {
	
	public DataBaseAdapter mDBHelper;

	public DataBaseLoader(Context context, DataBaseAdapter mDBHelper) {
		super(context);
		this.mDBHelper = mDBHelper;
	}

	@Override
	public Cursor loadInBackground() {
		return mDBHelper.fetchAll();
	}

}
