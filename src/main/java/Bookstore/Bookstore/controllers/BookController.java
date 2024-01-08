package Bookstore.Bookstore.controllers;

import java.time.LocalDate;

import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.bll.services.iservices.IBookInventoryService;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.services.iservices.ICategoryService;
import Bookstore.Bookstore.views.IView;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;
import Bookstore.Bookstore.views.books.AddBookView;
import Bookstore.Bookstore.views.books.ManageBooksView;

public class BookController {
	private IBookInventoryService bookService;
	private IBookPurchaseService bookPurchaseService;
	private ICategoryService categoryService;
	
	public BookController(IBookInventoryService bookService, IBookPurchaseService bookPurchaseService, ICategoryService categoryService) {
		this.bookService = bookService;
		this.bookPurchaseService = bookPurchaseService;
		this.categoryService = categoryService;
	}
	
	public ManageBooksView getIndexView() {
		ManageBooksView view = new ManageBooksView(bookService.getAll());
		
		view.setStockListener(e -> {
			try {
				if(e.getNewValue() == null)
					throw new EmptyInputException("stock");
				
				updateStock(e.getRowValue(), e.getNewValue());
			} catch(NonPositiveInputException | ExistingObjectException | EmptyInputException | WrongFormatException | WrongLengthException | IncorrectPermissionsException ex) {
				e.getTableView().getColumns().get(2).setVisible(false);
				e.getTableView().getColumns().get(2).setVisible(true);
				view.displayError(ex.getLocalizedMessage());
			}
		});
		
		view.setAddListener(e -> {
			Utils.createPopupStage("Add new book", getInsertView()).showAndWait();
			view.refreshTable(bookService.getAll());
		});
		
		view.setDeleteListener(e -> {
			try {
				delete(view.getSelectedIndex());
				view.refreshTable(bookService.getAll());
			} catch (Exception exc) {
				view.displayError(exc.getMessage());
			}
		});
		
		return view;
	}
	
	public IView getInsertView() {
		AddBookView	view = new AddBookView();
		view.setCategoryLv(categoryService.getAll());
		
		view.setSubmitAction(e -> {
			try {
				addBook(view.submitValues());
				Utils.getCurrentStage(e).close();
			} catch (ExistingObjectException | EmptyInputException | NonPositiveInputException | WrongFormatException
					| WrongLengthException | IncorrectPermissionsException exc) {
				view.displayError(exc.getMessage());
			}
		});
		
		return view;
	}
	
	private void updateStock(BookInventoryDTO book, int stock) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException, ExistingObjectException, IncorrectPermissionsException {		
		if(stock > book.getStock()) {
			BookPurchaseDTO purchase = new BookPurchaseDTO(book.getPurchasedPrice() * (stock - book.getStock()), LocalDate.now());
			bookPurchaseService.add(purchase);
		}
		
		bookService.updateStock(book, stock);
	}
	
	private void delete(int index) throws Exception {
		if(index == -1)
			throw new Exception("Please select an item");
		
		bookService.remove(index);
	}
	
	private void addBook(BookInventoryDTO model) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		bookService.add(model);

		double purchaseCost = model.getSellingPrice() * model.getStock();
		bookPurchaseService.add(new BookPurchaseDTO(purchaseCost, LocalDate.now())); // Adding expense to DB
	}
}
