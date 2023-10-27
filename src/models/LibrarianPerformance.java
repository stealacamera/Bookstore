package models;

public class LibrarianPerformance {
	private String employee;
	private int numOfBills, numOfBooks;
	private double salesAmount;
	
	public LibrarianPerformance(Employee employee, int numOfBooks, double salesAmount) {
		this.employee = employee.toString();
		setNumOfBills(1);
		setNumOfBooks(numOfBooks);
		setSalesAmount(salesAmount);
	}

	public String getEmployee() {
		return employee;
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
