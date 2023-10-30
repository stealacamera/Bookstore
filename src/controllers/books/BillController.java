package controllers.books;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Map;

import exceptions.NonPositiveInputException;
import javafx.collections.ObservableList;
import models.Bill;
import models.Book;
import models.CashFlow;
import models.helpers.ListIO;
import models.helpers.Session;

public class BillController {	
	public ObservableList<Book> getBooks() {
		return Book.getAll();
	}
	
	public void submitBill(ArrayList<Map.Entry<Book, Integer>> bill) throws FileNotFoundException, IOException {
		double priceTotal = 0;
		int numOfBooks = 0;
		
		for(Map.Entry<Book, Integer> soldBook: bill) {
			priceTotal += soldBook.getKey().getSellingPrice() * soldBook.getValue();
			numOfBooks += soldBook.getValue();
			
			try {
				soldBook.getKey().setStock(soldBook.getKey().getStock() - soldBook.getValue());
			} catch (NonPositiveInputException ex) {
				System.out.println(ex);
			}
		}
		
		createBillFile(bill, priceTotal);
		Bill newSale = new Bill(Session.getCurrentUser(), priceTotal, numOfBooks);
		Bill.add(newSale);
		CashFlow.addBookSale(newSale);		
		
		ListIO.writeToFile(Bill.FILE_NAME, Bill.getAll());
		ListIO.writeToFile(Book.FILE_NAME, new ArrayList<>(Book.getAll()));
		CashFlow.writeToFile();
	}
	
	private void createBillFile(ArrayList<Map.Entry<Book, Integer>> bill, double billTotal) throws FileNotFoundException, IOException {
		String userHome = System.getProperty("user.home") ;
		File billFile = new File(userHome, (Bill.listSize() + 1) + ".txt");
		
		try(PrintWriter write = new PrintWriter(billFile, Charset.forName("UTF-8"))) {		
			for(Map.Entry<Book, Integer> soldBook: bill) {
				write.printf("%-3d x %-30s\t", soldBook.getValue(), soldBook.getKey().getTitle());
				write.printf("%.3f%n", soldBook.getValue() * soldBook.getKey().getSellingPrice());
			}
			
			write.println("------------------------------------------------");
			write.printf("%-36s\t%.3f", "TOTAL:", billTotal);
		}
	}
}