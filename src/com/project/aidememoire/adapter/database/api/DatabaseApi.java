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
	
	public List<Person> fetchAllPersonAndMoney(){
	}
	
	public void fetchMoneyOfPerson(Person person){
		
	}
	
	public void fetchDetteOfPerson(Person person){
		
	}
	
	public void fetchCreditOfPerson(){
	}
	
	public List<Person> fetchPersonWithSpecifiedMoney(Person person, Money money){
		Cursor c = mDbHelper.fetchSpecifiedMoney(
					person.getName(), 
					person.getSurname(), 
					money.getSomme(), 
					money.getDate(), 
					money.getType());

		return fromDataToPersons(c);
	}
	
	public void addMoneyOfPerson(Person person, Money money){
		switch (money.getType()) {
		case CREDIT:
			mDbHelper.addCreditLine(person.getName(), 
					person.getSurname(),
					123456,
//					money.getDate(),
					money.getSomme());
			break;
		case DETTE:
			mDbHelper.addDetteLine(person.getName(),
					person.getSurname(),
					123456,
//					money.getDate(),
					money.getSomme());
			break;
		default:
			return;
		}
	}
	
	public boolean hasPerson(){
		return true;
	}
	
	public boolean hasPersonWithSpecifiedMoney(Person person, Money money){
		if(fetchPersonWithSpecifiedMoney(person, money).size() > 0){
			return true;
		}
		return false;
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
		Money money = new Money(c.getInt(3), c.getInt(4), type);
		return money;
	}

}
