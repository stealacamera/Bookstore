package models;

import java.io.Serializable;

import exceptions.NonPositiveInputException;
import models.utilities.CustomDate;

public class Bill implements Serializable {
	private static final long serialVersionUID = 3285272564248001502L;
	
	private double saleAmount;
	private int numOfBooks;
	private int sellerId;
	private CustomDate date;
	
	public Bill(int sellerId, double saleAmount, int numOfBooks) throws NonPositiveInputException {
		setSellerId(sellerId);
		setSaleAmount(saleAmount); 
		setNumOfBooks(numOfBooks);
		this.date = new CustomDate();
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) throws NonPositiveInputException {
		if(saleAmount <= 0)
			throw new NonPositiveInputException("sale amount");
		
		this.saleAmount = saleAmount;
	}

	public int getNumOfBooks() {
		return numOfBooks;
	}

	public void setNumOfBooks(int numOfBooks) throws NonPositiveInputException {
		if(numOfBooks <= 0)
			throw new NonPositiveInputException("nr. of books");
		
		this.numOfBooks = numOfBooks;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) throws NonPositiveInputException {
		if(sellerId <= 0)
			throw new NonPositiveInputException("seller id");
		
		this.sellerId = sellerId;
	}

	public CustomDate getDate() {
		return date;
	}	
}
