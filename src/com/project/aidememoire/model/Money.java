package com.project.aidememoire.model;

import java.util.Date;

import com.project.aidememoire.enumeration.SumType;

public class Money {
	private int somme;
	private String date;
	private SumType type;

	public Money(int somme, String date, SumType type) {
		this.somme = somme;
		this.date = date;
		this.type = type;
	}
	
	public double addSomme(int sommeToAdd) {
		return (this.somme + sommeToAdd);
	}
	
	public int getSomme() {
		return somme;
	}

	public void setSomme(int somme) {
		this.somme = somme;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public SumType getType() {
		return type;
	}

	public void setType(SumType type) {
		this.type = type;
	}
	
	@Override
	public boolean equals(Object otherMoney) {
	    if (!(otherMoney instanceof Money)) {
	        return false;
	    }

	    Money money = (Money) otherMoney;

	    boolean isEqual = false;
	    // Custom equality check here.
	    isEqual = (this.somme == money.somme);
	    isEqual = isEqual && (this.date == money.date);
	    
	    return isEqual;
	}
}
