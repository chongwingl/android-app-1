package com.project.aidememoire.fragment;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

import android.support.v4.app.FragmentTransaction;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ListFragment extends Fragment{

	private final static String TAG = "ListFragment";
	
	private ListView peopleListView;
	private PersonListAdapter adapter;
	
	private Button addButton;
	
	private View fragmentView;
	private DatabaseApi dataBaseApi;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataBaseApi = new DatabaseApi(getActivity());
		
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.list_layout, container, false);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.personListView);
		addButton = (Button) fragmentView.findViewById(R.id.footer_button);

		adapter = new PersonListAdapter(getActivity(), getLoaderManager());
		peopleListView.setAdapter(adapter);


		peopleListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
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
        });
		
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	           	fragmentTransaction.replace(R.id.main_container, new AddFragment());
	           	fragmentTransaction.addToBackStack(null);
	           	fragmentTransaction.commit();
			}
		});
		
		return fragmentView;
	}
		
}
