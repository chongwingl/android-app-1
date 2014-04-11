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

public class AddFragment extends Fragment{
	
	private final static String TAG = "AddFragment";
	
	private View fragmentView;

	private DatabaseApi dataBaseApi;
	
	private Button addButton;
	private Button cancelButton;
	private EditText nameEdit;
	private EditText surnameEdit;
	private EditText sumEdit;
	private static Button dateEdit;
	private RadioGroup sumSignsRadioGroup;
	private DatePicker datePicker;
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		dataBaseApi =  DatabaseApi.getInstance(getActivity());

		fragmentView = inflater.inflate(R.layout.add_layout, container, false);
		
		addButton = (Button) fragmentView.findViewById(R.id.addElement);
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
            			getStringMonth(datePicker.getMonth()) + " " + 
            			String.valueOf(datePicker.getYear());
            	
            	if(!sumEdit.getText().toString().equals("") && !surnameEdit.getText().toString().equals("") && !nameEdit.getText().toString().equals("")){
            		money = new Money(Integer.parseInt(sumEdit.getText().toString()), date, type);
                	person = new Person(nameEdit.getText().toString(), surnameEdit.getText().toString(), money);

                	addPerson(person);
            		
                	Log.i(TAG, "name : " + nameEdit.getText());
                	Log.i(TAG, "surname : " + surnameEdit.getText());
                	Log.i(TAG, "sum : " + sumEdit.getText());
                	
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
	
	public void addPerson(Person person) {
		if(!dataBaseApi.hasPersonWithSpecifiedMoney(person, person.getMoney().get(0))){
			dataBaseApi.addMoneyOfPerson(person, person.getMoney().get(0));
		}
		
	}

}
