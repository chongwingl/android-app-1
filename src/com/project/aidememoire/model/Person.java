package com.project.aidememoire.model;

import java.util.ArrayList;
import java.util.List;

import com.project.aidememoire.enumeration.SumType;

public class Person {
	
	private String name;
	private String surname;
	private List<Money> money = new ArrayList<Money>();
	
	public Person(String name, String surname){
		this.name = name;
		this.surname = surname;
	}
	
	public Person(String name, String surname, Money money){
		this.name = name;
		this.surname = surname;
		this.money.add(money);
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
	
	public int allDette(){
		int dette = 0;
		for(Money m : this.money){
			if(m.getType() == "dette"){
				dette += m.getSomme();
			}
		}
		
		return dette;
	}
	
	public int allCredit(){
		int credit = 0;
		for(Money m : this.money){
			if(m.getType() == "credit"){
				credit += m.getSomme();
			}
		}
		
		return credit;
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

}
