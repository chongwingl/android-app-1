package com.project.aidememoire.model;

import java.util.Date;

public class Money {
	private int somme;
	private Date date;

	public Money(int somme, Date date) {
		this.somme = somme;
		this.date = date;
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
}
