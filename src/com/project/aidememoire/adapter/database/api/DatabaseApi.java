package com.project.aidememoire.adapter.database.api;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;

import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.enumeration.SumType;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class DatabaseApi {
	
	private DataBaseAdapter mDbHelper;
	
	public DatabaseApi(Activity activity){
		mDbHelper = new DataBaseAdapter(activity);
	}
	
	public void fetchPerson(){
		mDbHelper.open();
		mDbHelper.close();
	}
	
	public List<Person> fetchAllPerson(){
		mDbHelper.open();
		Cursor c = mDbHelper.fetchAllPersons();
		mDbHelper.close();
		return fromDataToPersons(c);
	}
	
	public void fetchPersonDette(){
		mDbHelper.open();
		mDbHelper.close();
	}
	
	public void fetchPersonCredit(){
		mDbHelper.open();
		mDbHelper.close();
	}
	
	public void fetchPersonMoney(){
		mDbHelper.open();
		mDbHelper.close();
	}
	
	public void addPersonCredit(){
		mDbHelper.open();
		mDbHelper.close();
	}
	
	public void addPersonDette(){
		mDbHelper.open();
		mDbHelper.close();
	}
	
	public boolean hasPerson(){
		mDbHelper.open();
		mDbHelper.close();
		return true;
	}
	
	public boolean hasPersonDebt(){
		mDbHelper.open();
		mDbHelper.close();
		return true;
	}
	
	public boolean hasPersonCredit(){
		mDbHelper.open();
		mDbHelper.close();
		return true;
	}
	
	public Person fromDataToPerson(Cursor c){
		Person person = new Person(c.getString(1), c.getString(2));
		return person;
	}
	
	public List<Person> fromDataToPersons(Cursor c){
		List<Person> persons = new ArrayList<Person>();
		while(c.moveToNext()){
			persons.add(fromDataToPerson(c));
		}
		return persons;
	}
	
	public Money fromDataToMoney(Cursor c, SumType type){
		Money money = new Money(c.getInt(1), c.getInt(2), type);
		return money;
	}

}
