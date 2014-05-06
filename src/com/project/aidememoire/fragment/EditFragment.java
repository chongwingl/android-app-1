package com.project.aidememoire.fragment;

import java.util.Calendar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.project.aidememoire.R;
import com.project.aidememoire.database.api.DatabaseApi;
import com.project.aidememoire.fragment.AddFragment.MonthConversion;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

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
	private TextView title;
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	
		context = getActivity();
		dataBaseApi =  DatabaseApi.getInstance(context);
		final Bundle bundle = getArguments();
	
		fragmentView = inflater.inflate(R.layout.set_layout, container, false);
		
		title = (TextView) fragmentView.findViewById(R.id.set_title);
		title.setText(R.string.editfragment_title);
		title.setTextAppearance(context, R.style.TitleFontStyle);
		
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
                	money.setId(bundle.getLong(ListFragment.S_ID));
            		person = new Person(nameEdit.getText().toString(), surnameEdit.getText().toString(), money);
                	person.setId(bundle.getLong(ListFragment.P_ID));
                	
                	
                	if(dataBaseApi.open()){
                		update(person);
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

	private void update(Person person) {
		dataBaseApi.updatePerson(person);
		dataBaseApi.updateMoney(person.getMoney().get(0));
	}

}
