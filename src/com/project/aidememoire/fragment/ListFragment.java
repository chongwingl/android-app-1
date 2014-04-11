package com.project.aidememoire.fragment;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
		dataBaseApi = DatabaseApi.getInstance(getActivity());
		
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.list_layout, container, false);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.personListView);
		addButton = (Button) fragmentView.findViewById(R.id.footer_button);

		adapter = new PersonListAdapter(getActivity(), getLoaderManager());
		peopleListView.setAdapter(adapter);

		registerForContextMenu(peopleListView);
		
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

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.menu_delete:
	        	return delete(info.position);
	        case R.id.menu_edit:
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		getActivity().getMenuInflater().inflate(R.menu.item_contextual_menu, menu);
	}
	
	private boolean delete(int position){
		Cursor c = (Cursor) adapter.getItem(position);
	 	// 0: _id
	 	// 1: p_id
	 	// 2: date
	 	// 3: montant
	 	// 4: type
	 	// 5: _id
	 	// 6: name
	 	// 7: surname

		if(!dataBaseApi.isOpen()){
			dataBaseApi.open();
		}
  	   	if(dataBaseApi.deleteSomme(c)){
  	   		getLoaderManager().restartLoader(PersonListAdapter.LOADER_ID, null, adapter).forceLoad();
			adapter.notifyDataSetChanged();  
			return true;
  	   	}
  	   	return false;
	}
		
}
