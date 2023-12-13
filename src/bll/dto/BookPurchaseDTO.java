package bll.dto;

import java.time.LocalDate;

public class BookPurchaseDTO {
	private double amount;
	private LocalDate date;
	
	public BookPurchaseDTO(double amount, LocalDate date) {
		setAmount(amount);
		setDate(date);
	}
	
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
}
