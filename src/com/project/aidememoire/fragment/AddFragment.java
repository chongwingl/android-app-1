package com.project.aidememoire.fragment;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.project.aidememoire.R;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.database.loader.DataBaseLoader;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class AddFragment extends Fragment implements LoaderCallbacks<Cursor>{

	private final static String TAG = "AddFragment";
	private final static int LOADER_ID = 10;
	
	public final static String AUTOCOMPLETE = "autocomplete";
	
	private View fragmentView;

	private DatabaseApi dataBaseApi;
	
	private Button addButton;
	private Button cancelButton;
	private AutoCompleteTextView  nameEdit;
	private AutoCompleteTextView surnameEdit;
	private EditText sumEdit;
	private RadioGroup sumSignsRadioGroup;
	private DatePicker datePicker;
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		
		dataBaseApi =  DatabaseApi.getInstance(getActivity());
		Bundle bundle = new Bundle();
		bundle.putString(AUTOCOMPLETE, AUTOCOMPLETE);
		getLoaderManager().initLoader(LOADER_ID, bundle, this).forceLoad();

		fragmentView = inflater.inflate(R.layout.set_layout, container, false);
		
		addButton = (Button) fragmentView.findViewById(R.id.validate);
		cancelButton = (Button) fragmentView.findViewById(R.id.cancel);
		
		nameEdit = (AutoCompleteTextView) fragmentView.findViewById(R.id.name);
		surnameEdit = (AutoCompleteTextView) fragmentView.findViewById(R.id.surname);
		sumEdit = (EditText) fragmentView.findViewById(R.id.sum);
		datePicker = (DatePicker) fragmentView.findViewById(R.id.date);
		sumSignsRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.sumSign);
		
		datePicker.setCalendarViewShown(false);
		datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
		
		addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	
            	Person person;
            	Money money;
            	String type;
            	
            	type = "credit";
            	if(sumSignsRadioGroup.getCheckedRadioButtonId() == R.id.minus){
            		type = "dette";
            	}
            	String date = String.valueOf(datePicker.getDayOfMonth()) + " " + 
            			MonthConversion.getStringMonth(datePicker.getMonth()) + " " + 
            			String.valueOf(datePicker.getYear());
            	
            	if(!sumEdit.getText().toString().equals("") && !surnameEdit.getText().toString().equals("") && !nameEdit.getText().toString().equals("")){
            		money = new Money(Integer.parseInt(sumEdit.getText().toString()), date, type);
                	person = new Person(nameEdit.getText().toString(), surnameEdit.getText().toString(), money);
                	
                	if(dataBaseApi.open()){
                		addPerson(person);
                	}
                	
                	getFragmentManager().popBackStack();
            	}
            	else {
            		if(sumEdit.getText().toString().equals("")){
            			sumEdit.setHint(R.string.error_placeholder);
            		}
            		if(nameEdit.getText().toString().equals("")){
            			nameEdit.setHint(R.string.error_placeholder);
            		}
            		if(surnameEdit.getText().toString().equals("")){
            			surnameEdit.setHint(R.string.error_placeholder);
            		}
            	}
            }
        });
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getFragmentManager().popBackStack();
				
			}
		});

		return fragmentView;
	}
	
	public static class MonthConversion {
		public static String getStringMonth(int num){
			String month = "";
			DateFormatSymbols dfs = new DateFormatSymbols();
	        String[] months = dfs.getMonths();
	        if (num >= 0 && num <= 11 ) {
	            month = months[num];
	        }
	        return month;
		}
		
		public static int getIntMonth(String month){
			int num = 0;
			DateFormatSymbols dfs = new DateFormatSymbols();
	        String[] months = dfs.getMonths();
	        while (!months[num].equals(month) && num <= 11 ) {
	            num++;
	        }
	        return num;
		}
	}
	
	private void addPerson(Person person) {
		if(!dataBaseApi.hasPersonWithSpecifiedMoney(person, person.getMoney().get(0))){
			dataBaseApi.addMoneyOfPerson(person, person.getMoney().get(0));
		}
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle bundle) {
		dataBaseApi.open();
		return new DataBaseLoader(context, dataBaseApi, bundle);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		List<String> names = new ArrayList<String>();
		List<String> surnames = new ArrayList<String>();
		while(cursor.moveToNext()){
			if(!names.contains(cursor.getString(1))){
				names.add(cursor.getString(1));
			}
			if(!surnames.contains(cursor.getString(2))){
				surnames.add(cursor.getString(2));
			}
		}
		ArrayAdapter<String> nameAdapter= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, names);
		nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		nameEdit.setThreshold(1);
		nameEdit.setAdapter(nameAdapter);
		ArrayAdapter<String> surnameAdapter= new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, surnames);
		surnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		surnameEdit.setThreshold(1);
		surnameEdit.setAdapter(surnameAdapter);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		dataBaseApi.close();
	}

}
