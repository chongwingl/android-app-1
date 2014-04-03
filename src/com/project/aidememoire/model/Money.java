package com.project.aidememoire.model;

import java.util.Date;

import com.project.aidememoire.enumeration.SumType;

public class Money {
	private int somme;
	private int date;
	private SumType type;

	public Money(int somme, int date, SumType type) {
		this.somme = somme;
		this.date = 123456;
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

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public SumType getType() {
		return type;
	}

	public void setType(SumType type) {
		this.type = type;
	}
}
