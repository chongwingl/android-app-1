package com.project.aidememoire.fragment;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.MainListAdapter;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.fragment.AddFragment.MonthConversion;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class ListFragment extends Fragment{

	private final static String TAG = "ListFragment";
	
	public final static String NAME = "name";
	public final static String SURNAME = "surname";
	public final static String SUM = "sum";
	public final static String DATE = "date";
	public final static String TYPE = "type";
	public final static String P_ID = "person_id";
	public final static String S_ID = "sum_id";
	
	public final static String SORT_FILTER = "sort_filter";
	public final static String FILTER_DATA = "filter_data";
	public final static String PERSON = "person";
	
	private ListView peopleListView;
	private MainListAdapter adapter;
	
	private Button addButton;
	
	private View fragmentView;
	private DatabaseApi dataBaseApi;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dataBaseApi = DatabaseApi.getInstance(getActivity());
		
		if(getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE){
			setHasOptionsMenu(true);
		}
		
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		fragmentView = inflater.inflate(R.layout.list_layout, container, false);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.personListView);

		adapter = new MainListAdapter(getActivity(), getLoaderManager());
		peopleListView.setAdapter(adapter);

		registerForContextMenu(peopleListView);
		
		addButton = (Button) fragmentView.findViewById(R.id.footer_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	           	fragmentTransaction.replace(R.id.main_container, new AddFragment());
	           	fragmentTransaction.addToBackStack(null);
	           	fragmentTransaction.commit();
			}
		});
		
		
		peopleListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
		       	Bundle bundle = new Bundle();
		       	Cursor c = (Cursor) adapter.getItem(position);
		       	
		       	Money money = new Money(c.getInt(3), c.getString(2), c.getString(4));
		       	money.setId(c.getLong(0));
		       	Person person = new Person(c.getString(6), c.getString(7), money);
		       	person.setId(c.getLong(1));
		       	
		       	InfosFragment infosFragment = new InfosFragment();
		       	bundle.putParcelable(PERSON, person);
		       	infosFragment.setArguments(bundle);
		      
		       	fragmentTransaction.replace(R.id.main_container, infosFragment);
		       	fragmentTransaction.addToBackStack(null);
		       	fragmentTransaction.commit();
			}
		});
		
		return fragmentView;
	}
	
	@Override
	public void onDestroy() {
		dataBaseApi.close();
		super.onDestroy();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    switch (item.getItemId()) {
	        case R.id.context_menu_delete:
	        	return delete(info.position);
	        case R.id.context_menu_edit:
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

		if(dataBaseApi.open()){
			Money money = new Money(c.getInt(3), c.getString(2), c.getString(4));
			money.setId(c.getLong(0));
	  	   	
			if(dataBaseApi.deleteSomme(money)){
	  	   		Person person = new Person(c.getString(6), c.getString(7));
	  	   		person.setId(c.getLong(1));
	  	   		
	  	   		if(dataBaseApi.hasPersonWithMoney(person)){
	  	   			dataBaseApi.deletePerson(person);
	  	   		}
	  	   		
	  	   		getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, null, adapter).forceLoad();
				adapter.notifyDataSetChanged();  
				return true;
	  	   	}
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
	
	 @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
	        inflater.inflate(R.menu.main_menu, menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Bundle bundle = new Bundle();
		switch(item.getItemId()){
			case R.id.menu_reset:
				getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, null, adapter).forceLoad();
				adapter.notifyDataSetChanged();  
				break;
			case R.id.submenu_sort_name:
				bundle.putString(SORT_FILTER, DataBaseAdapter.ORDER_BY_NAME);
				getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, bundle, adapter).forceLoad();
				adapter.notifyDataSetChanged();  
				break;
		
			case R.id.submenu_sort_date:
				bundle.putString(SORT_FILTER, DataBaseAdapter.ORDER_BY_DATE); 
				getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, bundle, adapter).forceLoad();
				adapter.notifyDataSetChanged();  
				break;
				
			case R.id.submenu_sort_sum:
				bundle.putString(SORT_FILTER, DataBaseAdapter.ORDER_BY_SUM);
				getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, bundle, adapter).forceLoad();
				adapter.notifyDataSetChanged();  
				break;
				
			case R.id.submenu_sort_surname:
				bundle.putString(SORT_FILTER, DataBaseAdapter.ORDER_BY_SURNAME); 
				getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, bundle, adapter).forceLoad();
				adapter.notifyDataSetChanged();  
				break;
				
			case R.id.submenu_filter_date:
			case R.id.submenu_filter_name:
			case R.id.submenu_filter_sum:
			case R.id.submenu_filter_surname:
				showDialog(item.getItemId());
				break;
				
			default:
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showDialog(int itemId) {
		String action = "";
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(itemId != R.id.submenu_filter_date){
			
			View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_filter, null);
			TextView textView = (TextView) dialogView.findViewById(R.id.dialog_textview);
			final EditText input = (EditText) dialogView.findViewById(R.id.dialog_input);
			switch(itemId){
				case R.id.submenu_filter_name:
					action = DataBaseAdapter.FILTER_BY_NAME;
					textView.setText(R.string.submenu_name);
					break;
				case R.id.submenu_filter_sum:
					action = DataBaseAdapter.FILTER_BY_SUM;
					textView.setText(R.string.submenu_sum);
					break;
				case R.id.submenu_filter_surname:
					action = DataBaseAdapter.FILTER_BY_SURNAME;
					textView.setText(R.string.submenu_surname);
					break;
			}
			
			builder.setView(dialogView);
			final String actionToTransmitToDialog = action;
			
			builder.setPositiveButton(R.string.filter_dialog_OK, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Bundle bundle = new Bundle();
					bundle.putString(SORT_FILTER, actionToTransmitToDialog); 
					
					String filter = input.getText().toString();
					if(filter.equals("")){
						dialog.cancel();
					}
					else{
						bundle.putString(FILTER_DATA, filter);
						getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, bundle, adapter).forceLoad();
						adapter.notifyDataSetChanged();
					}
				}
			});
		} else {
			View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_datepicker, null);
			final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.dialog_date);
			datePicker.setCalendarViewShown(false);
			datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
			builder.setView(dialogView);
			builder.setPositiveButton(R.string.filter_dialog_OK, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Bundle bundle = new Bundle();
					bundle.putString(SORT_FILTER, DataBaseAdapter.FILTER_BY_DATE); 
					
					String date = String.valueOf(datePicker.getDayOfMonth()) + " " + 
	        			MonthConversion.getStringMonth(datePicker.getMonth()) + " " + 
	        			String.valueOf(datePicker.getYear());
					
					bundle.putString(FILTER_DATA, date);
					getLoaderManager().restartLoader(MainListAdapter.LOADER_ID, bundle, adapter).forceLoad();
					adapter.notifyDataSetChanged();
				}
			});
		}
		
		builder.setTitle(R.string.filter_dialog_title);
		builder.setNegativeButton(R.string.filter_dialog_cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		
		builder.show();
	}

}
