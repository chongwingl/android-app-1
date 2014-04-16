package com.project.aidememoire.model;

import java.util.ArrayList;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable{
	
	private String name;
	private String surname;
	private List<Money> money = new ArrayList<Money>();
	private long id;
	
	public Person(String name, String surname){
		this.name = name;
		this.surname = surname;
		this.id = -1;
	}
	
	public Person(String name, String surname, Money money){
		this.name = name;
		this.surname = surname;
		this.money.add(money);
		this.id = -1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	
	public List<Money> getMoney() {
		return money;
	}

	public void addMoney(Money money) {
		this.money.add(money);
	}
	
	public void addMoneys(List<Money> money) {
		this.money.addAll(money);
	}
	
	public int getAllDette(){
		int dette = 0;
		for(Money m : this.money){
			if(m.getType().equals("dette")){
				dette += m.getSomme();
			}
		}
		
		return dette;
	}
	
	public int getAllCredit(){
		int credit = 0;
		for(Money m : this.money){
			if(m.getType().equals("credit")){
				credit += m.getSomme();
			}
		}
		
		return credit;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public boolean hasId(){
		if(this.id == -1){
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object otherPerson) {
	    if (!(otherPerson instanceof Person)) {
	        return false;
	    }

	    Person person = (Person) otherPerson;

	    boolean isEqual = false;
	    // Custom equality check here.
	    isEqual = this.name.equals(person.name);
	    isEqual = isEqual && this.surname.equals(person.surname);
	    isEqual = isEqual && (this.money.size() == person.money.size());
	    isEqual = isEqual && (this.money.containsAll(person.money));
	    
	    return isEqual;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		
	}

}
