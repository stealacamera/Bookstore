package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.helpers.CustomDate;
import models.helpers.ListIO;

public class Bill implements Serializable {
	private static final long serialVersionUID = 3285272564248001502L;
	public static final String FILE_NAME = "bills.dat";
	private static ArrayList<Bill> bills;
	
	private double saleAmount;
	private int numOfBooks;
	private Employee seller;
	private CustomDate date;
	
	public Bill(Employee seller, double saleAmount, int numOfBooks) {
		this.seller = seller;
		this.saleAmount = saleAmount;
		this.numOfBooks = numOfBooks;
		this.date = new CustomDate();
	}
	
	//Class methods
	public static void setList() {
		ArrayList<Bill> currentList = ListIO.readFromFile(FILE_NAME);
		bills = currentList == null ? new ArrayList<>() : currentList;
	}
	
	public static List<Bill> getAll() {
		return Collections.unmodifiableList(bills);
	}
	
	public static boolean add(Bill bill) {
		if(bill != null) {
			bills.add(bill);
			return true;
		}
		
		return false;
	}
	
	public static int listSize() {
		return bills.size();
	}

	//Getters & setters
	public double getAmount() {
		return saleAmount;
	}

	public int getNumberOfBooks() {
		return numOfBooks;
	}
	
	public LocalDate getDate() {
		return date.getDate();
	}
	
	public Employee getSeller() {
		return new Employee(seller);
	}
}
