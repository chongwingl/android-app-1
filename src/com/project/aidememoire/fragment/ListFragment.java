package com.project.aidememoire.fragment;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.enumeration.SumType;
import com.project.aidememoire.listener.OnPageChange;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

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

public class ListFragment extends Fragment{

	private final static String TAG = "ListFragment";
	
	private ListView peopleListView;
	private PersonListAdapter adapter;
	private View footerView;
	private View headerView;
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
		
		footerView = getLayoutInflater(state).inflate(R.layout.list_footer, null);
		headerView = getLayoutInflater(state).inflate(R.layout.list_header, null);
		peopleListView.addFooterView(footerView);
		peopleListView.addHeaderView(headerView);

		peopleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
	             if(position != 0 && position != (adapter.getCount() + 1)){
	            	 	Cursor c = (Cursor) adapter.getItem(position-1);
	            	 	// 0: _id
	            	 	// 1: p_id
	            	 	// 2: date
	            	 	// 3: montant
	            	 	// 4: type
	            	 	// 5: _id
	            	 	// 6: name
	            	 	// 7: surname
	            	 	
	            	 	Person person = new Person(c.getString(6), c.getString(7), new Money(c.getInt(3), c.getString(2), c.getString(4)));

		          	   	if(dataBaseApi.deleteSomme(c)){
	             	   		adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
	            			adapter.notifyDataSetChanged();       			
		          	   	} 
	             }
	             
	             else if(position == (adapter.getCount() + 1)) {
	            	 FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	            	 fragmentTransaction.replace(R.id.main_container, new AddFragment());
	            	 fragmentTransaction.addToBackStack(null);
	            	 fragmentTransaction.commit();
	             }
            }
        });
		
		return fragmentView;
	}
		
}
