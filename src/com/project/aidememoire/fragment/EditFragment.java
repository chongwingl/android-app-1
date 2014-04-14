package com.project.aidememoire.fragment;

import java.text.DateFormatSymbols;
import java.util.Calendar;

import com.project.aidememoire.R;
import com.project.aidememoire.adapter.database.api.DatabaseApi;
import com.project.aidememoire.fragment.AddFragment.MonthConversion;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

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

public class EditFragment extends Fragment {
	
private final static String TAG = "EditFragment";
	
	private View fragmentView;

	private DatabaseApi dataBaseApi;
	
	private Button editButton;
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
		Bundle bundle = getArguments();
	
		fragmentView = inflater.inflate(R.layout.set_layout, container, false);
		
		editButton = (Button) fragmentView.findViewById(R.id.validate);
		editButton.setText(R.string.form_button_edit);
		cancelButton = (Button) fragmentView.findViewById(R.id.cancel);
		
		nameEdit = (EditText) fragmentView.findViewById(R.id.name);
		nameEdit.setText(bundle.getString(ListFragment.NAME));
		
		surnameEdit = (EditText) fragmentView.findViewById(R.id.surname);
		surnameEdit.setText(bundle.getString(ListFragment.SURNAME));
		
		sumEdit = (EditText) fragmentView.findViewById(R.id.sum);
		sumEdit.setText(bundle.getString(ListFragment.SUM));


		sumSignsRadioGroup = (RadioGroup) fragmentView.findViewById(R.id.sumSign);
		if(bundle.getString(ListFragment.TYPE).equals("credit")){
			sumSignsRadioGroup.check(R.id.plus);
		}
		else {
			sumSignsRadioGroup.check(R.id.minus);
		}
		
		datePicker = (DatePicker) fragmentView.findViewById(R.id.date);
		datePicker.setCalendarViewShown(false);
		datePicker.setMaxDate(Calendar.getInstance().getTimeInMillis());
		String [] date = bundle.getString(ListFragment.DATE).split(" ");
		int day = Integer.parseInt(date[0]);
		int month = MonthConversion.getIntMonth(date[1]);
		int year = Integer.parseInt(date[2]);
		datePicker.updateDate(year, month, day);
		
		editButton.setOnClickListener(new View.OnClickListener() {
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
                	
                	// TODO use an updatePerson method
//                	addPerson(person);
                	
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

}
