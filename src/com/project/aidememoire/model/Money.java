package com.project.aidememoire.model;

import java.util.Date;

import com.project.aidememoire.enumeration.SumType;

public class Money {
	private int somme;
	private Date date;
	private SumType type;

	public Money(int somme, Date date, SumType type) {
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public SumType getType() {
		return type;
	}

	public void setType(SumType type) {
		this.type = type;
	}
}
