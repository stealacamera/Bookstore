package models;

import java.io.Serializable;
import java.time.LocalDate;

import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import models.utilities.CustomDate;

public class BookPurchase implements Serializable {
	private static final long serialVersionUID = 8258344977448667240L;
	
	private double amount;
	private CustomDate date;
	
	public BookPurchase(double amount) throws NonPositiveInputException {
		setAmount(amount);
		this.date = new CustomDate(LocalDate.now());
	}
	
	public BookPurchase(double amount, CustomDate date) throws NonPositiveInputException, EmptyInputException {
		setAmount(amount);
		setDate(date);
	}

	public void setAmount(double amount) throws NonPositiveInputException {
		if(amount <= 0)
			throw new NonPositiveInputException("sale amount");
			
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public CustomDate getDate() {
		return new CustomDate(date.getDate());
	}
	
	public void setDate(CustomDate date) throws EmptyInputException {
		if(date == null)
			throw new EmptyInputException("date");
		
		this.date = date;
	}
}
