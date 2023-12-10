package controllers.books;

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
import models.utilities.CustomDate;
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
		
		BookPurchaseDTO purchase = new BookPurchaseDTO(book.getSellingPrice() * stock, new CustomDate(LocalDate.now()));
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
		bookPurchaseService.add(new BookPurchaseDTO(purchaseCost, new CustomDate(LocalDate.now())));
	}
	
	/*private File createBookInputFile(AddBookView view) {
		File newBookFile = new File("New Book.txt");
		
		try(PrintWriter write = new PrintWriter(newBookFile, Charset.forName("UTF-8"))) {
			write.println(view.getIsbn() + "\n" + view.getBookTitle() + "\n" + view.getAuthor());
			write.println(view.getCategory() + "\n" + view.getSupplier() + "\n" + view.getPurchasedDate());
			write.println(view.getPurchasedPrice() + "\n" + view.getOriginalPrice() + "\n" + view.getSellingPrice() + "\n" + view.getStock());
		} catch(FileNotFoundException ex) {
			view.displayError(ex.getLocalizedMessage());
		} catch (IOException e) {
			view.displayError("Illegal/unrecognizable character(s) used");
		}
		
		return newBookFile;
	}*/
}
