package com.project.aidememoire.fragment;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.listener.OnPageChange;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
		
		footerView = this.getLayoutInflater(state).inflate(R.layout.list_footer, null);
		peopleListView.addFooterView(footerView);

		peopleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
	             if(view != footerView){
	            	 	Cursor c = (Cursor) adapter.getItem(position);
		          	   	if(dataBaseApi.deleteSomme(c.getLong(1))){
	             	   		adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
	            			adapter.notifyDataSetChanged();       			
		          	   	} 
	             }
	             else {
	            	 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	            	 fragmentTransaction.replace(R.id.main_container, new AddFragment());
	            	 fragmentTransaction.addToBackStack(null);
	            	 fragmentTransaction.commit();
	            	 
	             }
            }
        });
		
		return fragmentView;
	}

	@Override
	public void onPageVisible() {
		Log.i(TAG, "Page is visible");
		
		if(!dataBaseApi.isOpen()){
			dataBaseApi.open();
		}
		
		adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onPageChanged() {
		Log.i(TAG, "Page has been left");
		
		if(dataBaseApi.isOpen()){
			dataBaseApi.close();
		}
	}
	
	
}
