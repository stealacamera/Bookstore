package Bookstore.Bookstore.controllers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.LibrarianPerformanceDTO;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonSequentialDatesException;
import Bookstore.Bookstore.views.statistics.BookExpensesView;
import Bookstore.Bookstore.views.statistics.CashFlowStatsView;
import Bookstore.Bookstore.views.statistics.LibrarianPerformanceView;

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
					throw new NonSequentialDatesException();
				
				double totalBookSales = getTotalBookSales(startDate, endDate), 
						totalBookPurchases = getTotalBookPurchases(startDate, endDate),
						totalSalaries = getTotalSalaries();

				view.setData(totalBookSales, totalBookPurchases, totalSalaries);
			} catch(NonSequentialDatesException | EmptyInputException ex) {
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
			} catch(NonSequentialDatesException ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		return view;
	}
	
	private List<LibrarianPerformanceDTO> getLibrariansPerformance(LocalDate startDate, LocalDate endDate) throws NonSequentialDatesException {
		if(endDate.isBefore(startDate))
			throw new NonSequentialDatesException();
			
		Map<Integer, LibrarianPerformanceDTO> data = new HashMap<>();
		
		for(BillDTO bill: billService.getAll()) {
			EmployeeDTO seller = employeeService.getById(bill.getSellerId());
			
			if(seller.getAccessLvl() != 1 || !CustomDate.isInDate(startDate, endDate, bill.getDate()))
				continue;
			
			int key = seller.getId();
			
			if(data.containsKey(key)) {
				LibrarianPerformanceDTO librarian = data.get(key);
				librarian.addNumOfBills();
				librarian.addNumOfBooks(bill.getNumOfBooks());
				librarian.addSalesAmount(bill.getSaleAmount());
			} else
				data.put(key, new LibrarianPerformanceDTO(
						String.format("%s (%s)", seller.getFullName(), seller.getUsername()), 
						bill.getNumOfBooks(), 
						bill.getSaleAmount())
				);
		}
		
		return new ArrayList<>(data.values());
	}
	
	private double getDailyPurchases(LocalDate date) {
		double booksBought = 0;
		
		for(BookPurchaseDTO purchase: bookPurchaseService.getAll()) {
			if(purchase.getDate().equals(date))
				booksBought += purchase.getAmount();
		}
		
		return booksBought;
	}
	
	private double getDailySales(LocalDate date) {
		double booksSold = 0;
		
		for(BillDTO bill: billService.getAll()) {
			if(bill.getDate().equals(date))
				booksSold += bill.getSaleAmount();
		}
		
		return booksSold;
	}
	
	private double[] getMonthlyPurchases() {
		double[] monthlyBookPurchases = new double[12];
		
		for(BookPurchaseDTO purchase: bookPurchaseService.getAll())
			monthlyBookPurchases[purchase.getDate().getMonthValue() - 1] += purchase.getAmount();
		
		return monthlyBookPurchases;
	}
	
	private double[] getMonthlySales() {
		double[] monthlyBookSales = new double[12];
		
		for(BillDTO bill: billService.getAll())
			monthlyBookSales[bill.getDate().getMonthValue() - 1] += bill.getSaleAmount();
		
		return monthlyBookSales;
	}
	
	private double getTotalPurchases() {
		double totalBookPurchases = 0;
		
		for(double purchase: getMonthlyPurchases())
			totalBookPurchases += purchase;
		
		return totalBookPurchases;
	}
	
	private double getTotalSales() {
		return getTotalBookSales();
	}
	
	private double getTotalBookSales() { 
		double total = 0;
		
		for(BillDTO bill: billService.getAll())
			total += bill.getSaleAmount();
		
		return total;
	}
	
	private double getTotalBookSales(LocalDate startDate, LocalDate endDate) { 
		double total = 0;
		
		for(BillDTO bill: billService.getAll()) {
			if(CustomDate.isInDate(startDate, endDate, bill.getDate()))
				total += bill.getSaleAmount();
		}
		
		return total;
	}
	
	private double getTotalBookPurchases(LocalDate startDate, LocalDate endDate) {
		double total = 0;
		
		for(BookPurchaseDTO purchase: bookPurchaseService.getAll()) {
			if(CustomDate.isInDate(startDate, endDate, purchase.getDate()))
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
