package com.project.aidememoire.adapter.database.api;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import android.database.SQLException;

import com.project.aidememoire.adapter.database.DataBaseAdapter;
import com.project.aidememoire.enumeration.SumType;
import com.project.aidememoire.model.Money;
import com.project.aidememoire.model.Person;

public class DatabaseApi {
	
	private static final String TAG = "DataBaseApi";
	private DataBaseAdapter mDbHelper;
	private boolean isOpen;
	
	public DatabaseApi(Context context){
		mDbHelper = new DataBaseAdapter(context);
		open();
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public boolean open(){
		try{
			mDbHelper.open();
		} catch(SQLException exception){
			isOpen = false;
			return false;
		}
		isOpen = true;
		return true;
	}
	
	public void close(){
		mDbHelper.close();
		isOpen = false;
	}

	public void fetchPerson(){
		
	}
	
	public List<Person> fetchAllPerson(){
		Cursor c = mDbHelper.fetchAllPersons();
		return fromDataToPersons(c);
	}
	
	public void fetchPersonDette(){
	}
	
	public void fetchPersonCredit(){
	}
	
	public void fetchPersonMoney(){
	}
	
	public void addPersonCredit(){
	}
	
	public void addPersonDette(){
	}
	
	public boolean hasPerson(){
		return true;
	}
	
	public boolean hasPersonDebt(){
		return true;
	}
	
	public boolean hasPersonCredit(){
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
