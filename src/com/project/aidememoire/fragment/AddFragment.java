package com.project.aidememoire.fragment;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.Dialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog.Calls;
import android.support.v4.app.DialogFragment;
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
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.enumeration.SumType;
import com.project.aidememoire.enumeration.TextType;
import com.project.aidememoire.listener.OnPageChange;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class AddFragment extends Fragment implements OnPageChange{
	
	private final static String TAG = "AddFragment";
	
	private View fragmentView;
	
	private DataBaseAdapter mDbHelper;
	private DatabaseApi dataBaseApi;
	
	private Button addButton;
	private EditText nameEdit;
	private EditText surnameEdit;
	private EditText sumEdit;
	private static Button dateEdit;
	private RadioGroup sumSignsRadioGroup;
	private Context context;
	
	private ListView peopleListView;
	private PersonListAdapter adapter;
	private View footerView;
	private Bundle state;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		state = savedInstanceState;
		context = getActivity();
		dataBaseApi = new DatabaseApi(getActivity());
		Calendar date = Calendar.getInstance(TimeZone.getDefault(), new Locale("fr"));

		fragmentView = inflater.inflate(R.layout.add_layout, container, false);
		
		addButton = (Button) fragmentView.findViewById(R.id.addElement);
		
		nameEdit = (EditText) fragmentView.findViewById(R.id.name);
		surnameEdit = (EditText) fragmentView.findViewById(R.id.surname);
		sumEdit = (EditText) fragmentView.findViewById(R.id.sum);
		dateEdit = (Button) fragmentView.findViewById(R.id.date);     
		dateEdit.setText(date.get(Calendar.DAY_OF_MONTH) + " " + getStringMonth(date.get(Calendar.MONTH)) + " " + date.get(Calendar.YEAR));

		sumSignsRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.sumSign);
		
		peopleListView = (ListView) fragmentView.findViewById(R.id.personListView1);
		adapter = new PersonListAdapter(getActivity(), dataBaseApi.fetchAllPersonAndMoneyCursor(), false);
		peopleListView.setAdapter(adapter);
		
		if(adapter.getCount() < 1){
			setFooterView(state);
		}
		
		dateEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showFragment();
			}
		});
		
		addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	Person person;
            	Money money;
            	SumType type;
            	
            	if(sumSignsRadioGroup.getCheckedRadioButtonId() == R.id.minus){
            		type = SumType.DETTE;
            	}
            	else {
            		type = SumType.CREDIT;
            	}
            	
            	money = new Money(Integer.parseInt(sumEdit.getText().toString()), (String) dateEdit.getText(), type);
            	person = new Person(nameEdit.getText().toString(), surnameEdit.getText().toString(), money);


            	addPerson(person);
            	
            	if(footerView != null){
            		peopleListView.removeFooterView(footerView);
            		footerView = null;
            	}
            	
            	adapter.changeCursor(dataBaseApi.fetchAllPersonAndMoneyCursor());
        		adapter.notifyDataSetChanged();
        		
            	Log.i(TAG, "name : " + nameEdit.getText());
            	Log.i(TAG, "surname : " + surnameEdit.getText());
            	Log.i(TAG, "sum : " + sumEdit.getText());
            }
        });

		return fragmentView;
	}
	
	public void setFooterView(Bundle savedInstanceState){
		footerView = this.getLayoutInflater(savedInstanceState).inflate(R.layout.list_footer, null);
		peopleListView.addFooterView(footerView);
	}
	
	static String getStringMonth(int num){
		String month = "";
		DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
	}
	
	static int getIntMonth(String month){
		int num = 0;
		DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        while (!months[num].equals(month) && num <= 11 ) {
            num++;
        }
        return num;
	}
	
	public void showFragment() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "Choisissez une date");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		state = outState;
	}
	
	public void addPerson(Person person) {
		if(!dataBaseApi.hasPersonWithSpecifiedMoney(person, person.getMoney().get(0))){
			dataBaseApi.addMoneyOfPerson(person, person.getMoney().get(0));
		}
		
	}
	
	public static class DatePickerFragment extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {
	
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
		
			// Use the current date as the default date in the picker
			String [] selectedDate = ((String) dateEdit.getText()).split(" ");
			final Calendar c = Calendar.getInstance();
		
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, 
					Integer.parseInt(selectedDate[2]), 
					getIntMonth(selectedDate[1]), 
					Integer.parseInt(selectedDate[0]));
		}
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
		
			dateEdit.setText(dayOfMonth + " " 
					+ getStringMonth(monthOfYear) + " " 
					+ year);
		}
	
	}

	@Override
	public void onPageVisible() {
		Log.i(TAG, "Page is visible");
		if(dataBaseApi != null && !dataBaseApi.isOpen()){
			dataBaseApi.open();
		}
		
		if(adapter != null && adapter.getCount() < 1 && footerView == null){
			setFooterView(state);
		}
		else if(footerView != null){
    		peopleListView.removeFooterView(footerView);
    		footerView = null;
    	}
	}

	@Override
	public void onPageChanged() {
		Log.i(TAG, "Page has been left");
		if(dataBaseApi.isOpen()){
			dataBaseApi.close();
		}
	}
}
