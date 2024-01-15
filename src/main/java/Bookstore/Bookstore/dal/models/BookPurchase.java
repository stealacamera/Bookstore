package Bookstore.Bookstore.dal.models;

import java.io.Serializable;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.dal.models.utils.CustomDate;

public class BookPurchase implements Serializable {
	private static final long serialVersionUID = 8258344977448667240L;
	
	private double amount;
	private CustomDate date;
	
	public BookPurchase(double amount) throws NonPositiveInputException {
		setAmount(amount);
		this.date = new CustomDate();
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
		try {
			return new CustomDate(date.getDate());
		} catch (EmptyInputException e) {
			// Error won't be thrown because date is always instantiated
			e.printStackTrace();
			return null;
		}
	}
	
	public void setDate(CustomDate date) throws EmptyInputException {
		if(date == null)
			throw new EmptyInputException("date");
		
		this.date = new CustomDate(date.getDate());
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BookPurchase))
			return false;
		
		BookPurchase model = (BookPurchase) o;
		return getAmount() == model.getAmount() && getDate().equals(model.getDate());
	}
	
	@Override
	public String toString() {
		return String.format("Amount: %.3f, Date: %s", getAmount(), getDate());
	}
}
