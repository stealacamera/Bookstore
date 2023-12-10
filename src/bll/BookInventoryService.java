package bll;

import java.util.Collections;
import java.util.Comparator;

import bll.IServices.IBookInventoryService;
import bll.dto.BookDTO;
import bll.dto.BookInventoryDTO;
import dal.IRepositories.IBookInventoryRepository;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;
import models.Book;
import models.BookInventory;
import utils.Utils;

public class BookInventoryService extends Service<BookInventory, BookInventoryDTO> implements IBookInventoryService {
	private final IBookInventoryRepository db;
	private static Comparator<BookInventory> compareByTitle = new Comparator<BookInventory>() {
		@Override
		public int compare(BookInventory o1, BookInventory o2) {
			return o1.getBook().getTitle().compareToIgnoreCase(o2.getBook().getTitle());
		}
	};
	
	public BookInventoryService(IBookInventoryRepository db) {
		super(db);
		this.db = db;
	}
	
	// Adds only books with unique ISBNs
	// Objects are added alphabetically by title
	@Override
	public boolean add(BookInventoryDTO instance) throws ExistingObjectException, EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		if(instance == null)
			return false;
		
		add(convertToDAO(instance));
		db.saveChanges();
		return true;
	}
	
	@Override
	public void updateStock(BookInventoryDTO instance, int newStock) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		BookInventory model = db.getByISBN(instance.getBook().getIsbn());
		
		if(model == null)
			throw new EmptyInputException("book");
				
		model.setStock(newStock);
		db.saveChanges();
	}
	
	private void add(BookInventory model) throws ExistingObjectException {
		if(db.getByISBN(model.getBook().getIsbn()) != null)
			throw new ExistingObjectException("ISBN");
		
		int listIndex = Collections.binarySearch(db.getAll(), model, compareByTitle);
		db.add(Utils.getOrderedListIndex(listIndex), model);
	}
	
	@Override
	protected BookInventoryDTO convertToDTO(BookInventory model) {
		BookDTO instanceBook = new BookDTO(
				model.getBook().getIsbn(), model.getBook().getTitle(),
				model.getBook().getAuthor(), model.getBook().getSupplier(),
				model.getBook().getCategoryId());
		
		BookInventoryDTO instance = new BookInventoryDTO(
				instanceBook, model.getPurchasedPrice(), 
				model.getOriginalPrice(), model.getSellingPrice(), 
				model.getStock(), model.getPurchasedDate());
		
		return instance;
	}

	@Override
	protected BookInventory convertToDAO(BookInventoryDTO model) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		Book instanceBook = new Book(
				model.getBook().getIsbn(), model.getBook().getTitle(), 
				model.getBook().getAuthor(), model.getBook().getSupplier(), 
				model.getBook().getCategoryId());
		
		return new BookInventory(instanceBook, model.getPurchasedPrice(), model.getOriginalPrice(), model.getSellingPrice(), model.getStock(), model.getPurchasedDate());
	}
}
