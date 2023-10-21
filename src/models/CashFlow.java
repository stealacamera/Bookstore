package models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import models.helpers.CustomDate;

public class CashFlow {
	private static final String FILE_NAME = "cashflow.dat";
	private static double[] monthlyBookPurchases, monthlyBookSales;
	
	private CashFlow() {};
	
	public static void readFromFile() {
		File cashFlowFile = new File(FILE_NAME);
		
		if(cashFlowFile.length() != 0) {
			try(FileInputStream stream = new FileInputStream(cashFlowFile)) {
				ObjectInputStream read = new ObjectInputStream(stream);
				
				setMonthlyBookPurchases((double[])read.readObject());
				setMonthlyBookSales((double[])read.readObject());
				
				read.close();
			} catch(IOException | ClassNotFoundException ex) {
				System.out.println(ex);
			}
		} else {
			setMonthlyBookPurchases(new double[12]);
			setMonthlyBookSales(new double[12]);
		}
	}
	
	public static void writeToFile() {
		File cashFlowFile = new File(FILE_NAME);
		
		try(FileOutputStream stream = new FileOutputStream(cashFlowFile)) {
			ObjectOutputStream write = new ObjectOutputStream(stream);
			
			write.writeObject(monthlyBookPurchases);
			write.writeObject(monthlyBookSales);
			
			write.close();
		} catch(IOException ex) {
			System.out.println(ex);
		}
	}
	
	private static void setMonthlyBookPurchases(double[] newMonthlyBookPurchases) {
		monthlyBookPurchases = newMonthlyBookPurchases;
	}
	
	public static double[] getMonthlyBookPurchases() {
		return monthlyBookPurchases;
	}
	
	private static void setMonthlyBookSales(double[] newMonthlyBookSales) {
		monthlyBookSales = newMonthlyBookSales;
	}
	
	public static double[] getMonthlyBookSales() {
		return monthlyBookSales;
	}
	
	public static double getTotalBookSales() {
		double sum = 0;
		
		for(double sale: monthlyBookSales)
			sum += sale;
		
		return sum;
	}
	
	public static double getTotalBookPurchases() {
		double sum = 0;
		
		for(double purchase: monthlyBookPurchases)
			sum += purchase;
		
		return sum;
	}
	
	public static void addBookPurchase(BookPurchase purchase) {
		monthlyBookPurchases[purchase.getDate().getMonthValue() - 1] += purchase.getAmount();
	}
	
	public static double getTotalCosts(ArrayList<Employee> employees) {
		return getTotalBookPurchases() + getTotalSalaries(employees);
	}
	
	public static void addBookSale(Bill bill) {
		monthlyBookSales[bill.getDate().getMonthValue() - 1] += bill.getAmount();
	}
	
	public static double getDailyPurchases(ArrayList<BookPurchase> bookPurchases, LocalDate date) {
		double booksBought = 0;
		
		for(BookPurchase purchase: bookPurchases) {
			if(purchase.getDate().equals(date))
				booksBought += purchase.getAmount();
		}
		
		return booksBought;
	}
	
	public static double getDailySales(ArrayList<Bill> bills, LocalDate date) {
		double booksSold = 0;
		
		for(Bill bill: bills) {
			if(bill.getDate().equals(date))
				booksSold += bill.getAmount();
		}
		
		return booksSold;
	}
	
	public static double getTotalSalaries(ArrayList<Employee> employees) {
		double salaries = 0;
		
		for(Employee employee: employees)
			salaries += employee.getSalary();
		
		return salaries;
	}
	
	public static double getPeriodBookPurchases(LocalDate startDate, LocalDate endDate, ArrayList<BookPurchase> bookPurchases) {
		double total = 0;
		
		for(BookPurchase purchase: bookPurchases) {
			if(CustomDate.inDate(startDate, endDate, purchase.getDate()))
				total += purchase.getAmount();
		}
		
		return total;
	}
	
	public static double getPeriodBookSales(LocalDate startDate, LocalDate endDate, ArrayList<Bill> bookSales) {
		double total = 0;
		
		for(Bill sale: bookSales) {
			if(CustomDate.inDate(startDate, endDate, sale.getDate()))
				total += sale.getAmount();
		}
		
		return total;
	}
}
