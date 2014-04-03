package com.project.aidememoire.adapter;

import java.util.List;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.project.aidememoire.R;
import com.project.aidememoire.model.Person;

public class PersonListAdapter extends ArrayAdapter<Person>{
	
	private List<Person> items;
	private View view;
	private TextView name;
	private TextView surname;
	private TextView credit;
	private TextView dette;
	private LayoutInflater layoutInflater;

	public PersonListAdapter(Context context, int resource, List<Person> items) {
		super(context, resource, items);
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = convertView;
		layoutInflater = LayoutInflater.from(getContext());
		view = layoutInflater.inflate(R.layout.person_list, null);
		
		Person person = items.get(position);
		
		name = (TextView) view.findViewById(R.id.person_name);
        surname = (TextView) view.findViewById(R.id.person_surname);
        credit = (TextView) view.findViewById(R.id.credit);
        dette = (TextView) view.findViewById(R.id.dette);
        
        name.setText(person.getName());
        surname.setText(person.getSurname());
		credit.setText(String.valueOf(person.allCredit()));
		dette.setText(String.valueOf(person.allDette()));
		
		return view;
	}
	
	public List<Person> getItems(){
		return items;
	}

}
