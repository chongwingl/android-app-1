package com.project.aidememoire.model;

public class Person {
	
	private String name;
	private String surname;
	private Money money;
	
	public Person(String name, String surname){
		this.name = name;
		this.surname = surname;
	}
	
	public Person(String name, String surname, Money money){
		this.name = name;
		this.surname = surname;
		this.money = money;
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

	public Money getMoney() {
		return money;
	}

	public void setMoney(Money money) {
		this.money = money;
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
	    isEqual = isEqual && (this.money.getSomme() == person.money.getSomme());
	    isEqual = isEqual && (this.money.getDate() == person.money.getDate());
	    isEqual = isEqual && (this.money.getType() == person.money.getType());
	    
	    return isEqual;
	}

}
