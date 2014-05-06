package com.project.aidememoire.model;


public class Money {
	private int somme;
	private String date;
	private String type;
	private long id;

	public Money(int somme, String date, String type) {
		this.somme = somme;
		this.date = date;
		this.type = type;
		this.id = -1;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
