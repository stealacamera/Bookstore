package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

import models.helpers.ListIO;

public class BookPurchase implements Serializable {
	private static final long serialVersionUID = 8258344977448667240L;
	public static final String FILE_NAME = "stock_purchases.dat";
	private static ArrayList<BookPurchase> purchases;
	
	private double amount;
	private LocalDate date;
	
	public BookPurchase(double amount) {
		this.amount = amount;
		this.date = LocalDate.now();
	}

	//Class methods
	public static void setList() {
		ArrayList<BookPurchase> currentList = ListIO.readFromFile(FILE_NAME);
		purchases = currentList == null ? new ArrayList<>() : currentList;
	}
	
	public static ArrayList<BookPurchase> getAll() {
		return purchases;
	}
	
	public static void add(double purchase) {
		purchases.add(new BookPurchase(purchase));
	}
	
	//Getters
	public double getAmount() {
		return amount;
	}

	public LocalDate getDate() {
		return date;
	}
}
