package com.project.aidememoire.fragment;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.database.api.DatabaseApi;
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
	
	public final static String NAME = "name";
	public final static String SURNAME = "surname";
	public final static String SUM = "sum";
	public final static String DATE = "date";
	public final static String TYPE = "type";
	public final static String P_ID = "person_id";
	public final static String S_ID = "sum_id";
	
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
	        	return edit(info.position);
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
		
		Money money = new Money(c.getInt(3), c.getString(2), c.getString(4));
		money.setId(c.getLong(0));
  	   	
		if(dataBaseApi.deleteSomme(money)){
  	   		Person person = new Person(c.getString(6), c.getString(7));
  	   		person.setId(c.getLong(1));
  	   		
  	   		if(dataBaseApi.hasPersonWithMoney(person)){
  	   			dataBaseApi.deletePerson(person);
  	   		}
  	   		
  	   		getLoaderManager().restartLoader(PersonListAdapter.LOADER_ID, null, adapter).forceLoad();
			adapter.notifyDataSetChanged();  
			return true;
  	   	}
  	   	return false;
	}
	
	private boolean edit(int position){
		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
       	EditFragment editFragment = new EditFragment();
       	Bundle bundle = new Bundle();
       	Cursor c = (Cursor) adapter.getItem(position);
    	
       	bundle.putString(NAME, c.getString(6));
       	bundle.putString(SURNAME, c.getString(7));
       	bundle.putString(SUM, c.getString(3));
       	bundle.putString(DATE, c.getString(2));
       	bundle.putString(TYPE, c.getString(4));
       	bundle.putLong(P_ID, c.getLong(1));
       	bundle.putLong(S_ID, c.getLong(0));
       	
       	editFragment.setArguments(bundle);
       	
       	fragmentTransaction.replace(R.id.main_container, editFragment);
       	fragmentTransaction.addToBackStack(null);
       	fragmentTransaction.commit();
        
       	return true;
	}
		
}
