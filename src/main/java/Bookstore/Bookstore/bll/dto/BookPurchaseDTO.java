package Bookstore.Bookstore.bll.dto;

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
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BookPurchaseDTO))
			return false;
		
		BookPurchaseDTO model = (BookPurchaseDTO) o;
		return getAmount() == model.getAmount() && getDate().equals(model.getDate());
	}
	
	@Override
	public int hashCode() {
		return (int) getAmount() + getDate().hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("Amount: %.3f, Date: %s", getAmount(), getDate());
	}
}
