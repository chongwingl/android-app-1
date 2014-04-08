package com.project.aidememoire.fragment;

import java.util.List;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.listener.OnPageChange;
import com.project.aidememoire.model.Person;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ListFragment extends Fragment implements OnPageChange{

	private final static String TAG = "ListFragment";
	
	private ListView peopleListView;
	private PersonListAdapter adapter;
	private View footerView;
	private Bundle state;
	
	private View fragmentView;
	private DatabaseApi dataBaseApi;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		state = savedInstanceState;
		dataBaseApi = new DatabaseApi(getActivity());
		
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.list_layout, container, false);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.personListView2);

		adapter = new PersonListAdapter(getActivity(), dataBaseApi.fetchAllPersonAndMoneyCursor(), false);
		peopleListView.setAdapter(adapter);
		if(adapter.getCount() < 1){
			setFooterView(state);
		}

		peopleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
             
               Cursor c = (Cursor) adapter.getItem(position);
        	   if(dataBaseApi.deleteSomme(c.getLong(1))){
           	   		adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
          			adapter.notifyDataSetChanged();
          			
          			if(adapter.getCount() < 1){
          				setFooterView(state);
          			}
          			
        	   }
               if(dataBaseApi.deleteSomme(c.getLong(1))){
            	   	adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
           			adapter.notifyDataSetChanged();
               }
//               Log.i(TAG, "_id: "+ c.getString(0));
//               Log.i(TAG, "p_id: "+ c.getString(1));
//               Log.i(TAG, "date: "+ c.getString(2));
//               Log.i(TAG, "montant: "+ c.getString(3));
//               Log.i(TAG, "type"+ c.getString(4));
//               Log.i(TAG, "_id"+ c.getString(5));
//               Log.i(TAG, "name"+ c.getString(5));
//               Log.i(TAG, "surname"+ c.getString(5));
            }
        });
		
		return fragmentView;
	}
	public void setFooterView(Bundle state){
		footerView = this.getLayoutInflater(state).inflate(R.layout.list_footer, null);
		peopleListView.addFooterView(footerView);

	@Override
	public void onPageVisible() {
		Log.i(TAG, "Page is visible");
		
		if(!dataBaseApi.isOpen()){
			dataBaseApi.open();
		}
		
		adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
		adapter.notifyDataSetChanged();
		if(footerView != null){
    		peopleListView.removeFooterView(footerView);
    		footerView = null;
	
	}

	@Override
	public void onPageChanged() {
		Log.i(TAG, "Page has been left");
		
		if(dataBaseApi.isOpen()){
			dataBaseApi.close();
		}
	}
	
	
}
