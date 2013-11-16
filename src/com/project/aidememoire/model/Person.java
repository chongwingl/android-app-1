package com.project.aidememoire.model;

public class Person {
	
	private String name;
	private String surname;
	private Money credit;
	private Money dette;
	
	public Person(String name, String surname){
		this.name = name;
		this.surname = surname;
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

	public Money getCredit() {
		return credit;
	}

	public void setCredit(Money credit) {
		this.credit = credit;
	}

	public Money getDette() {
		return dette;
	}

	public void setDette(Money dette) {
		this.dette = dette;
	}
}
