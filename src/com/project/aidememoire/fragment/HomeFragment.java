package com.project.aidememoire.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.model.Person;

public class HomeFragment extends Fragment {
	
	private final static String TAG = "HomeFragment";
	
	private View fragmentView;
	private DataBaseAdapter mDbHelper;
	private List<Person> persons;
	private ListView peopleListView;
	private PersonListAdapter adapter;
	
	private Button addButton;
	private EditText nameEdit;
	private EditText surnameEdit;
	private EditText sumEdit;
	private EditText dateEdit;
	private RadioGroup sumSignsRadioGroup;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		fragmentView = inflater.inflate(R.layout.home_fragment, container, false);
		peopleListView = (ListView) fragmentView.findViewById(R.id.peopleListView);
		persons = getPersons();

		adapter = new PersonListAdapter(getActivity(), R.layout.person_list, persons);
		peopleListView.setAdapter(adapter);
		
		addButton = (Button) fragmentView.findViewById(R.id.addElement);
		
		nameEdit = (EditText) fragmentView.findViewById(R.id.name);
		surnameEdit = (EditText) fragmentView.findViewById(R.id.surname);
		sumEdit = (EditText) fragmentView.findViewById(R.id.sum);
		dateEdit = (EditText) fragmentView.findViewById(R.id.date);
		
		sumSignsRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.sumSign);
		
		addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	Log.i(TAG, "name : " + nameEdit.getText());
            	Log.i(TAG, "surname : " + surnameEdit.getText());
            	Log.i(TAG, "sum : " + sumEdit.getText());
            	Log.i(TAG, "date : " + dateEdit.getText());
            }
        });

		return fragmentView;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	public List<Person> getPersons(){
		Cursor c = this.getData();
		List<Person> personRetrieved = new ArrayList<Person>();

		while (c.moveToNext()){
			personRetrieved.add(new Person(c.getString(1), c.getString(2)));
			Log.i(TAG, "People : " + c.getString(0) + " " + c.getString(1) + " " + c.getString(2));
		}
		
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
}
