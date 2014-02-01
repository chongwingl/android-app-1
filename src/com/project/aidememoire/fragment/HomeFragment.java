package com.project.aidememoire.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.PersonListAdapter;
import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.enumeration.TextType;
import com.project.aidememoire.model.Money;
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
	private DatePicker dateEdit;
	private RadioGroup sumSignsRadioGroup;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		Calendar date = Calendar.getInstance();

		fragmentView = inflater.inflate(R.layout.home_fragment, container, false);
		peopleListView = (ListView) fragmentView.findViewById(R.id.peopleListView);
		persons = getPersons();

		adapter = new PersonListAdapter(getActivity(), R.layout.person_list, persons);
		peopleListView.setAdapter(adapter);
		
		addButton = (Button) fragmentView.findViewById(R.id.addElement);
		
		nameEdit = (EditText) fragmentView.findViewById(R.id.name);
		surnameEdit = (EditText) fragmentView.findViewById(R.id.surname);
		sumEdit = (EditText) fragmentView.findViewById(R.id.sum);
		dateEdit = (DatePicker) fragmentView.findViewById(R.id.date);
		dateEdit.updateDate(date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
		
		sumSignsRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.sumSign);
		
		addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	Person person;
            	Money money;
            	
            	money = new Money(Double.parseDouble(sumEdit.getText().toString()), new Date());
            	person = new Person(nameEdit.getText().toString(), surnameEdit.getText().toString());
            	
            	if(sumSignsRadioGroup.getCheckedRadioButtonId() == R.id.minus){
            		person.setDette(money);
            	}
            	else {
            		person.setCredit(money);
            	}
            	
            	adapter.add(person);
            	
            	Log.i(TAG, "name : " + nameEdit.getText());
            	Log.i(TAG, "surname : " + surnameEdit.getText());
            	Log.i(TAG, "sum : " + sumEdit.getText());
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
	
	public void addPerson(Person person) {
		
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
