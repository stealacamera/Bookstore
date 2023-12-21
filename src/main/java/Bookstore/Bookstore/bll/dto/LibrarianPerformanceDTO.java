package Bookstore.Bookstore.bll.dto;

public class LibrarianPerformanceDTO {
	private String employeeDescription;
	private int numOfBills, numOfBooks;
	private double salesAmount;
	
	public LibrarianPerformanceDTO(String employeeDescription, int numOfBooks, double salesAmount) {
		setEmployeeDescription(employeeDescription);
		setNumOfBills(1);
		setNumOfBooks(numOfBooks);
		setSalesAmount(salesAmount);
	}
	public String getEmployeeDescription() {
		return employeeDescription;
	}

	public void setEmployeeDescription(String employeeDescription) {
		this.employeeDescription = employeeDescription;
	}
	
	public int getNumOfBills() {
		return numOfBills;
	}

	public void setNumOfBills(int numOfBills) {
		this.numOfBills = numOfBills;
	}
	
	public void addNumOfBills() {
		numOfBills++;
	}

	public int getNumOfBooks() {
		return numOfBooks;
	}

	public void setNumOfBooks(int numOfBooks) {
		this.numOfBooks = numOfBooks;
	}
	
	public void addNumOfBooks(int numOfBooks) {
		this.numOfBooks += numOfBooks;
	}

	public double getSalesAmount() {
		return salesAmount;
	}

	public void setSalesAmount(double salesAmount) {
		this.salesAmount = salesAmount;
	}
	
	public void addSalesAmount(double salesAmount) {
		this.salesAmount += salesAmount;
	}
}
