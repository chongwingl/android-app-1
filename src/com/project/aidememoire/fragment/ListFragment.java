package com.project.aidememoire.fragment;

import java.util.ArrayList;
import java.util.List;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.listener.OnFragmentChange;
import com.project.aidememoire.model.Person;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ListFragment extends Fragment implements OnFragmentChange{

	private final static String TAG = "AddFragment";
	
	private List<Person> persons;
	private ListView peopleListView;
	private PersonListAdapter adapter;
	
	private View fragmentView;
	private DataBaseAdapter mDbHelper;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		fragmentView = super.onCreateView(inflater, container, savedInstanceState);
		
		fragmentView = inflater.inflate(R.layout.list_layout, container, false);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.peopleListView);
		persons = getPersons();

		adapter = new PersonListAdapter(getActivity(), R.layout.person_list, persons);
		peopleListView.setAdapter(adapter);
		
		return fragmentView;
	}
	
	public List<Person> getPersons(){
		Cursor c = this.getData();
		List<Person> personRetrieved = new ArrayList<Person>();

		while (c.moveToNext()){
			personRetrieved.add(new Person(c.getString(1), c.getString(2)));
			Log.i(TAG, "People : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
		}
		mDbHelper.close();
		return personRetrieved;
		
	}
	
	public Cursor getData(){
		mDbHelper = new DataBaseAdapter(this.getActivity());
		mDbHelper.open();
		mDbHelper.addCreditLine("Martin", "Pierre", 1234567890, 1234);
		mDbHelper.addCreditLine("Durand", "Pauline", 987654321, 765);
		mDbHelper.addDetteLine("Dupont", "Paul", 1235679, 987);
		return mDbHelper.fetchAllPeople();
		
	}

	@Override
	public void OnFragmentVisible() {
		// TODO Auto-generated method stub
		
	}
	
	

}
