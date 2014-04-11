package com.project.aidememoire.adapter.database.api;

import java.util.ArrayList;
import java.util.Iterator;
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
	
	private static DatabaseApi instance;
	private static final String TAG = "DataBaseApi";
	private DataBaseAdapter mDbHelper;
	private boolean isOpen;
	
	private DatabaseApi(Context context){
		mDbHelper = new DataBaseAdapter(context);
		open();
	}
	
	public static DatabaseApi getInstance(Context context){
		if(DatabaseApi.instance == null){
			DatabaseApi.instance = new DatabaseApi(context);
		}
		return DatabaseApi.instance;
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
	
	public List<Person> fetchAllPerson(){
		Cursor c = mDbHelper.fetchAllPersons();
		return fromDataToPersons(c);
	}
	
	public List<Person> fetchAllPersonAndMoney(){
		Cursor c = mDbHelper.fetchAllPersons();
		List<Person> persons = fromDataToPersons(c);
		for(Person p : persons){
			p.addMoneys(fetchMoneyOfPerson(p));
		}
		return persons;
	}
	
	public Cursor fetchAllPersonAndMoneyCursor(){
		return mDbHelper.fetchAll();
	}
	
	public List<Money> fetchMoneyOfPerson(Person person){
		return fromDataToMoneys(mDbHelper.fetchMoneyOfPerson(person.getName(), person.getSurname()));
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
	
	public Cursor fetchSommeCursor(){
		return mDbHelper.fetchAllMoney();
	}
	
	public void addMoneyOfPerson(Person person, Money money){
		mDbHelper.addSommeLine(person.getName(), 
				person.getSurname(),
				money.getDate(),
				money.getSomme(),
				money.getType());
	}
	
	public boolean hasPerson(){
		return true;
	}
	
	public boolean deleteSomme(Cursor c){
		boolean deleted =  mDbHelper.deleteSomme(c.getLong(1));
		return deleted;
	}
	
	public boolean deletePerson(Cursor c){
		return mDbHelper.deletePerson(c.getLong(5));
	}
	
	public boolean hasPersonWithSpecifiedMoney(Person person, Money money){
		if(fetchPersonWithSpecifiedMoney(person, money).size() > 0){
			return true;
		}
		return false;
	}
	
	public boolean hasPersonWithMoney(Person person){
		if(fetchMoneyOfPerson(person).size() > 0){
			return true;
		}
		return false;
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
	
	public Money fromDataToMoney(Cursor c){
		Money money = new Money(c.getInt(2), c.getString(3), c.getString(4));
		return money;
	}
	
	public List<Money> fromDataToMoneys(Cursor c){
		List<Money> money = new ArrayList<Money>();
		while(c.moveToNext()){
			money.add(fromDataToMoney(c));
		}
		return money;
	}

}
