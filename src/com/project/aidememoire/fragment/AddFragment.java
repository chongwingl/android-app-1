package com.project.aidememoire.fragment;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.project.aidememoire.R;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class AddFragment extends Fragment{
	
	private final static String TAG = "AddFragment";
	
	private View fragmentView;

	private DatabaseApi dataBaseApi;
	
	private Button addButton;
	private Button cancelButton;
	private EditText nameEdit;
	private EditText surnameEdit;
	private EditText sumEdit;
	private RadioGroup sumSignsRadioGroup;
	private DatePicker datePicker;
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		dataBaseApi =  DatabaseApi.getInstance(getActivity());

		fragmentView = inflater.inflate(R.layout.set_layout, container, false);
		
		addButton = (Button) fragmentView.findViewById(R.id.validate);
		cancelButton = (Button) fragmentView.findViewById(R.id.cancel);
		
		nameEdit = (EditText) fragmentView.findViewById(R.id.name);
		surnameEdit = (EditText) fragmentView.findViewById(R.id.surname);
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
                	
                	if(!dataBaseApi.isOpen()){
                		dataBaseApi.open();
                	}
                	
                	addPerson(person);
                	
                	if(dataBaseApi.isOpen()){
                		dataBaseApi.close();
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

}
