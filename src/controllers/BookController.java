package controllers;

import java.time.LocalDate;

import bll.IServices.IBookInventoryService;
import bll.IServices.IBookPurchaseService;
import bll.IServices.ICategoryService;
import bll.dto.BookInventoryDTO;
import bll.dto.BookPurchaseDTO;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import utils.Utils;
import views.IView;
import views.books.AddBookView;
import views.books.ManageBooksView;

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
				
				int oldStockNr = e.getOldValue().intValue(), newStockNr = e.getNewValue().intValue();
				
				if(newStockNr > oldStockNr) {
					double purchasePrice = e.getRowValue().getPurchasedPrice() * (newStockNr - oldStockNr);
					bookPurchaseService.add(new BookPurchaseDTO(purchasePrice, LocalDate.now()));
				}
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
		bookService.updateStock(book, stock);
		
		BookPurchaseDTO purchase = new BookPurchaseDTO(book.getSellingPrice() * stock, LocalDate.now());
		bookPurchaseService.add(purchase);
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
