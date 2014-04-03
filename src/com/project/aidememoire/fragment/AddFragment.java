package com.project.aidememoire.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		dataBaseApi = new DatabaseApi(getActivity());
		Calendar date = Calendar.getInstance();

		fragmentView = inflater.inflate(R.layout.add_layout, container, false);
		
		addButton = (Button) fragmentView.findViewById(R.id.addElement);
		
		nameEdit = (EditText) fragmentView.findViewById(R.id.name);
		surnameEdit = (EditText) fragmentView.findViewById(R.id.surname);
		sumEdit = (EditText) fragmentView.findViewById(R.id.sum);
		dateEdit = (Button) fragmentView.findViewById(R.id.date);
		dateEdit.setText(date.get(Calendar.YEAR) + "/" + date.get(Calendar.MONTH) + "/" + date.get(Calendar.DAY_OF_MONTH));
		
		sumSignsRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.sumSign);
		
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
            	
            	money = new Money(Integer.parseInt(sumEdit.getText().toString()), 12356, type);
            	person = new Person(nameEdit.getText().toString(), surnameEdit.getText().toString(), money);


            	addPerson(person);
            	
            	Log.i(TAG, "name : " + nameEdit.getText());
            	Log.i(TAG, "surname : " + surnameEdit.getText());
            	Log.i(TAG, "sum : " + sumEdit.getText());
            }
        });

		return fragmentView;
	}
	
	public void showFragment() {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "Choisissez une date");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
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
		
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);
		
			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
		
			dateEdit.setText(Calendar.getInstance().get(Calendar.YEAR) + "/" 
					+ Calendar.getInstance().get(Calendar.MONTH) + "/" 
					+ Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
		}
	
	}

	@Override
	public void onPageVisible() {
		Log.i(TAG, "Page is visible");
		if(dataBaseApi != null && !dataBaseApi.isOpen()){
			dataBaseApi.open();
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
