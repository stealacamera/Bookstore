package Bookstore.Bookstore.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.startup.Session;
import Bookstore.Bookstore.views.CreateBillView;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;

public class BillController {
	private IBillService billService;
	private IBookInventoryService bookService;
	
	public BillController(IBillService billService, IBookInventoryService bookService) {
		this.billService = billService;
		this.bookService = bookService;
	}
	
	public IView getCreateView() {
		CreateBillView view = new CreateBillView(bookService.getAll());
		
		view.setBillItemStockListener(e -> {
			try {
				if(e.getNewValue() == null)
					throw new EmptyInputException("stock");
				else if(e.getRowValue().getKey().getStock() < e.getNewValue().intValue())
					throw new Exception("Not enough stock");
				
				isInStock(e.getRowValue().getKey().getStock(), e.getNewValue());
			} catch(Exception ex) {
				e.getTableView().getColumns().get(2).setVisible(false);
				e.getTableView().getColumns().get(2).setVisible(true);
				
				view.refreshBillView();
				view.displayError(ex.getMessage());
			}
		});
		
		view.setInsertListener(e -> {
			try {
				BookInventoryDTO bookSelected = view.getSelectedBookItem();
				int quantity = view.getQuantity();
				
				if(bookSelected == null)
					throw new Exception("Please select a book");
				else if(!isInStock(bookSelected.getStock(), quantity))
					throw new Exception("Not enough stock");
				else if(view.billContainsBook(bookSelected))
					throw new ExistingObjectException("book entity");
				
				view.addItemToBill(bookSelected, quantity);
			} catch(NumberFormatException ex) {
				view.displayError(new EmptyInputException("stock").getMessage());
			} catch(Exception ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		view.setSubmitBillListener(e -> {
			try {
				submitBill(view.getBill());
				view.refreshView(bookService.getAll());
			} catch(FileNotFoundException ex) {
				view.displayError("Something went wrong: file could not be created. Try again later");
			} catch(IOException ex) {
				view.displayError("Illegal/unrecognizable character(s) was used");
			} catch(ExistingObjectException | EmptyInputException | NonPositiveInputException
					| WrongFormatException | WrongLengthException | IncorrectPermissionsException ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		view.setDeleteListener(e -> {
			try {
				int selectedIndex = view.getSelectedBillItemIndex();
			
				if(selectedIndex == -1)
					throw new Exception("Please select an item in the bill");
				
				view.removeBillItem(selectedIndex);
			} catch(Exception ex) {
				view.displayError(ex.getMessage());
			}
		});
		
		return view;
	}
	
	private boolean isInStock(int stock, int requestQuantity) throws NonPositiveInputException {
		if(requestQuantity <= 0)
			throw new NonPositiveInputException("quantity");
		
		return stock < requestQuantity ? false : true;
	}
	
	private void submitBill(List<Map.Entry<BookInventoryDTO, Integer>> bill) throws FileNotFoundException, IOException, ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		double priceTotal = 0;
		int numOfBooks = 0;
		
		for(Map.Entry<BookInventoryDTO, Integer> soldBook: bill) {
			priceTotal += soldBook.getKey().getSellingPrice() * soldBook.getValue();
			numOfBooks += soldBook.getValue();
			
			bookService.updateStock(soldBook.getKey(), soldBook.getKey().getStock() - soldBook.getValue().intValue());
		}
		
		billService.add(new BillDTO(Session.getCurrentUser().getId(), priceTotal, numOfBooks));
		createBillFile(bill, priceTotal);
	}
	
	private void createBillFile(List<Map.Entry<BookInventoryDTO, Integer>> bill, double billTotal) throws FileNotFoundException, IOException {
		Files.createDirectories(Paths.get(Utils.billsDirPath));
		File billFile = new File(Utils.billsDirPath, (billService.count()) + ".txt");
		
		try(PrintWriter write = new PrintWriter(billFile, Charset.forName("UTF-8"))) {		
			for(Map.Entry<BookInventoryDTO, Integer> soldBook: bill) {
				write.printf("%-4d x %-30s\t", soldBook.getValue(), "\"" + soldBook.getKey().getBook().getTitle() + "\"");
				write.printf("%.3f%n", soldBook.getValue() * soldBook.getKey().getSellingPrice());
			}
			
			write.println("-----------------------------------------------------");
			write.printf("%-36s\t%.3f", "TOTAL:", billTotal);
		}
	}
}