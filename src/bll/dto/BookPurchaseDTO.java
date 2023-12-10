package bll.dto;

import models.utilities.CustomDate;

public class BookPurchaseDTO {
	private double amount;
	private CustomDate date;
	
	public BookPurchaseDTO(double amount, CustomDate date) {
		setAmount(amount);
		setDate(date);
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public CustomDate getDate() {
		return date;
	}
	public void setDate(CustomDate date) {
		this.date = date;
	}
}
