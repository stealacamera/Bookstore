package bll.dto;

import java.time.LocalDate;

public class BillDTO {
	private double saleAmount;
	private int numOfBooks;
	private int sellerId;
	private LocalDate date;
	
	public BillDTO(int sellerId, double saleAmount, int numOfBooks) {
		setSellerId(sellerId);
		setSaleAmount(saleAmount);
		setNumOfBooks(numOfBooks);
		date = LocalDate.now();
	}

	public double getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(double saleAmount) {
		this.saleAmount = saleAmount;
	}

	public int getNumOfBooks() {
		return numOfBooks;
	}

	public void setNumOfBooks(int numOfBooks) {
		this.numOfBooks = numOfBooks;
	}

	public int getSellerId() {
		return sellerId;
	}

	public void setSellerId(int sellerId) {
		this.sellerId = sellerId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	
}
