package com.project.aidememoire.model;

import java.util.Date;

public class Money {
	private double somme;
	private Date date;

	public Money(double somme, Date date) {
		this.somme = somme;
		this.date = date;
	}
	
	public double addSomme(double sommeToAdd) {
		return (this.somme + sommeToAdd);
	}
	
	public double getSomme() {
		return somme;
	}

	public void setSomme(double somme) {
		this.somme = somme;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
