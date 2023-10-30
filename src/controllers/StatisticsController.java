package controllers;

import java.time.LocalDate;
import java.util.ArrayList;

import models.Bill;
import models.BookPurchase;
import models.CashFlow;
import models.Employee;
import models.LibrarianPerformance;
import models.helpers.CustomDate;

public class StatisticsController {
	
	public ArrayList<LibrarianPerformance> getLibrariansPerformance(LocalDate startDate, LocalDate endDate) {
		ArrayList<LibrarianPerformance> data = new ArrayList<>();
		
		for(Bill bill: Bill.getAll()) {
			if(bill.getSeller().getAccessLvl() != 1 || !CustomDate.inDate(startDate, endDate, bill.getDate()))
				continue;
			
			int index = -1;
			
			for(int i = 0; i < data.size(); i++)
				if(bill.getSeller().toString().equals(data.get(i).getEmployeeDescription())) {
					index = i;
					break;
				}
			
			if(index != -1) {
				LibrarianPerformance librarian = data.get(index);
				librarian.addNumOfBills();
				librarian.addNumOfBooks(bill.getNumberOfBooks());
				librarian.addSalesAmount(bill.getAmount());
			} else
				data.add(new LibrarianPerformance(bill.getSeller().toString(), bill.getNumberOfBooks(), bill.getAmount()));
		}
		
		return data;
	}
	
	public double getDailyPurchases(LocalDate date) {
		return CashFlow.getDailyPurchases(BookPurchase.getAll(), date);
	}
	
	public double getDailySales(LocalDate date) {
		return CashFlow.getDailySales(Bill.getAll(), date);
	}
	
	public double[] getMonthlyPurchases() {
		return CashFlow.getMonthlyBookPurchases();
	}
	
	public double[] getMonthlySales() {
		return CashFlow.getMonthlyBookSales();
	}
	
	public double getTotalPurchases() {
		return CashFlow.getTotalBookPurchases();
	}
	
	public double getTotalSales() {
		return CashFlow.getTotalBookSales();
	}
	
	public double getTotalBookSales(LocalDate startDate, LocalDate endDate) { 
		return CashFlow.getPeriodBookSales(startDate, endDate, Bill.getAll());
	}
	
	public double getTotalBookPurchases(LocalDate startDate, LocalDate endDate) {
		return CashFlow.getPeriodBookPurchases(startDate, endDate, BookPurchase.getAll());
	}
	
	public double getTotalSalaries() {
		return CashFlow.getTotalSalaries(new ArrayList<>(Employee.getAll()));
	}
}
