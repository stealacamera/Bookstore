package dal.models;

import java.io.Serializable;

import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;

public class Book implements Serializable, Comparable<Book> {
	private static final long serialVersionUID = -7995962663053711982L;
	
	private String isbn, title, author, supplier;
	private int categoryId;
	
	public Book(Book model) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		this(model.getIsbn(), model.getTitle(), model.getAuthor(), model.getSupplier(), model.getCategoryId());
	}
	
	public Book(String isbn, String title, String author, String supplier, int categoryId) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		setIsbn(isbn);
		setTitle(title);
		setAuthor(author);
		setSupplier(supplier);
		setCategoryId(categoryId);
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) throws EmptyInputException, WrongFormatException {
		if(isbn == null || isbn.isBlank())
			throw new EmptyInputException("ISBN");
		else if(!isbn.matches("\\d{3}-\\d-\\d{2}-\\d{6}-\\d"))
			throw new WrongFormatException("ISBN", "(3 digits)-(1 digit)-(2 digits)-(6 digits)-(1 digit)");
		
		this.isbn = isbn;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) throws EmptyInputException, WrongLengthException {
		if(title == null || title.isBlank())
			throw new EmptyInputException("title");
		else if(title.length() > 30)
			throw new WrongLengthException("title", 30);
		
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) throws EmptyInputException, WrongLengthException, WrongFormatException {
		int maxLength = 45;
		
		if(author == null || author.isBlank())
			throw new EmptyInputException("author");
		else if(author.length() > maxLength)
			throw new WrongLengthException("author", maxLength);
		else if(!author.matches("[a-zA-Z]+(\\s[a-zA-Z]+)*"))
			throw new WrongFormatException("author name", "space-separated words, no numbers");
		
		this.author = author;			
	}
	
	public String getSupplier() {
		return supplier;
	}
	
	public void setSupplier(String supplier) throws EmptyInputException, WrongLengthException {
		if(supplier == null || supplier.isBlank())
			throw new EmptyInputException("supplier");
		else if(supplier.length() > 50)
			throw new WrongLengthException("supplier", 50);
		
		this.supplier = supplier;
	}
	
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) throws NonPositiveInputException {
		if(categoryId <= 0)
			throw new NonPositiveInputException("category id");
		
		this.categoryId = categoryId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Book)
			return ((Book) obj).getIsbn().equals(getIsbn());
		
		return false;
	}

	@Override
	public int hashCode() {
		return this.getIsbn().hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s by %s (%s)", getTitle(), getAuthor(), getIsbn());
	}

	@Override
	public int compareTo(Book o) {
		return this.getIsbn().compareTo(o.getIsbn());
	}
}
