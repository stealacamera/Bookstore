package controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bll.IServices.IBillService;
import bll.IServices.IBookPurchaseService;
import bll.IServices.IEmployeeService;
import bll.dto.BillDTO;
import bll.dto.BookPurchaseDTO;
import bll.dto.EmployeeDTO;
import bll.dto.LibrarianPerformanceDTO;
import exceptions.EmptyInputException;
import models.utilities.CustomDate;
import views.IView;
import views.stats.BookExpensesView;
import views.stats.CashFlowStatsView;
import views.stats.LibrarianPerformanceView;

public class StatisticsController {
	private IBillService billService;
	private IBookPurchaseService bookPurchaseService;
	private IEmployeeService employeeService;
	
	public StatisticsController(IBillService billService, IEmployeeService employeeService, IBookPurchaseService bookPurchaseService) {
		this.billService = billService;
		this.bookPurchaseService = bookPurchaseService;
		this.employeeService = employeeService;
	}
	
	public IView getBookExpensesView() {
		BookExpensesView view = new BookExpensesView();
		
		view.setDailyChart(getDailySales(view.getDateValue()), getDailyPurchases(view.getDateValue()));
		view.setMonthlyChart(getMonthlySales(), getMonthlyPurchases());
		view.setTotalChart(getTotalSales(), getTotalPurchases());
		
		view.setDateListener((observable, oldValue, newValue) -> view.setDailyChart(
			getDailySales(newValue), getDailyPurchases(newValue)
		));
		
		return view;
	}
	
	public IView getCashFlowStatsView() {
		CashFlowStatsView view = new CashFlowStatsView();
		
		view.setSubmitListener(e -> {
			try {
				LocalDate startDate = view.getStartDate(), endDate = view.getEndDate();
			
				if(startDate == null || endDate == null)
					throw new EmptyInputException("date range");
				else if(endDate.isBefore(startDate))
					throw new Exception("Ending date should be after starting date");
				
				double totalBookSales = getTotalBookSales(startDate, endDate), 
						totalBookPurchases = getTotalBookPurchases(startDate, endDate),
						totalSalaries = getTotalSalaries();
				
				view.setData(totalBookSales, totalBookPurchases, totalSalaries);
			} catch(Exception ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		return view;
	}
	
	public IView getLibrarianPerformanceView() {
		LibrarianPerformanceView view = new LibrarianPerformanceView();
		
		view.setSubmitListener(e -> {
			try {
				view.setPerformanceList(getLibrariansPerformance(view.getStartDate(), view.getEndDate()));
			} catch(Exception ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		return view;
	}
	
	private List<LibrarianPerformanceDTO> getLibrariansPerformance(LocalDate startDate, LocalDate endDate) throws Exception {
		if(endDate.isBefore(startDate))
			throw new Exception("Ending date should follow the starting date");
			
		Map<Integer, LibrarianPerformanceDTO> data = new HashMap<>();
		
		for(BillDTO bill: billService.getAll()) {
			EmployeeDTO seller = employeeService.getById(bill.getSellerId());
			
			if(seller.getAccessLvl() != 1 || !CustomDate.inDate(startDate, endDate, bill.getDate().getDate()))
				continue;
			
			int key = seller.getId();
			
			if(data.containsKey(key)) {
				LibrarianPerformanceDTO librarian = data.get(key);
				librarian.addNumOfBills();
				librarian.addNumOfBooks(bill.getNumOfBooks());
				librarian.addSalesAmount(bill.getSaleAmount());
			} else
				data.put(key, new LibrarianPerformanceDTO(
						seller.getFullName() + "(" + seller.getUsername() + ")", 
						bill.getNumOfBooks(), 
						bill.getSaleAmount())
				);
		}
		
		return new ArrayList<>(data.values());
	}
	
	private double getDailyPurchases(LocalDate date) {
		double booksBought = 0;
		
		for(BookPurchaseDTO purchase: bookPurchaseService.getAll()) {
			if(purchase.getDate().getDate().equals(date))
				booksBought += purchase.getAmount();
		}
		
		return booksBought;
	}
	
	private double getDailySales(LocalDate date) {
		double booksSold = 0;
		
		for(BillDTO bill: billService.getAll()) {
			if(bill.getDate().getDate().equals(date))
				booksSold += bill.getSaleAmount();
		}
		
		return booksSold;
	}
	
	private double[] getMonthlyPurchases() {
		double[] monthlyBookPurchases = new double[12];
		
		for(BookPurchaseDTO purchase: bookPurchaseService.getAll())
			monthlyBookPurchases[purchase.getDate().getDate().getMonthValue() - 1] += purchase.getAmount();
		
		return monthlyBookPurchases;
	}
	
	private double[] getMonthlySales() {
		double[] monthlyBookSales = new double[12];
		
		for(BillDTO bill: billService.getAll())
			monthlyBookSales[bill.getDate().getDate().getMonthValue() - 1] += bill.getSaleAmount();
		
		return monthlyBookSales;
	}
	
	private double getTotalPurchases() {
		double totalBookPurchases = 0;
		
		for(double purchase: getMonthlyPurchases())
			totalBookPurchases += purchase;
		
		return totalBookPurchases;
	}
	
	private double getTotalSales() {
		double totalBookSales = 0;
		
		for(double sale: getMonthlySales())
			totalBookSales += sale;
		
		return totalBookSales;
	}
	
	private double getTotalBookSales(LocalDate startDate, LocalDate endDate) { 
		double total = 0;
		
		for(BillDTO bill: billService.getAll()) {
			if(CustomDate.inDate(startDate, endDate, bill.getDate().getDate()))
				total += bill.getSaleAmount();
		}
		
		return total;
	}
	
	private double getTotalBookPurchases(LocalDate startDate, LocalDate endDate) {
		double total = 0;
		
		for(BookPurchaseDTO purchase: bookPurchaseService.getAll()) {
			if(CustomDate.inDate(startDate, endDate, purchase.getDate().getDate()))
				total += purchase.getAmount();
		}
		
		return total;
	}
	
	private double getTotalSalaries() {
		double salaries = 0;
		
		for(EmployeeDTO employee: employeeService.getAll())
			salaries += employee.getSalary();
		
		return salaries;
	}
}
