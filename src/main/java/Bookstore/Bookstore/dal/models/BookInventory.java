package Bookstore.Bookstore.dal.models;

import java.io.Serializable;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.dal.models.utils.CustomDate;

public class BookInventory implements Serializable {
	private static final long serialVersionUID = -2947267998942410788L;
	
	private Book book;
	private CustomDate purchasedDate;
	private double purchasedPrice, originalPrice, sellingPrice;
	private int stock;
	
	public BookInventory(Book book, double purchasedPrice, double originalPrice, double sellingPrice, int stock, CustomDate purchasedDate) throws NonPositiveInputException, EmptyInputException, WrongFormatException, WrongLengthException {
		setBook(book);
		setPurchasedPrice(purchasedPrice);
		setOriginalPrice(originalPrice);
		setSellingPrice(sellingPrice);
		setStock(stock);
		setPurchasedDate(purchasedDate);
	}
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException {
		this.book = new Book(book);
	}
	
	public CustomDate getPurchasedDate() {
		try {
			return new CustomDate(purchasedDate.getDate());
		} catch (EmptyInputException e) {
			// Error won't be thrown because date is always instantiated
			e.printStackTrace();
			return null;
		}
	}
	
	public void setPurchasedDate(CustomDate purchasedDate) throws EmptyInputException {
		this.purchasedDate = new CustomDate(purchasedDate.getDate());
	}
	
	public void setPurchasedDate(String purchasedDate) throws EmptyInputException, WrongFormatException {
		if(purchasedDate.isBlank())
			throw new EmptyInputException("purchased date");
		
		this.purchasedDate = new CustomDate(purchasedDate);
	}
	
	public double getPurchasedPrice() {
		return purchasedPrice;
	}
	
	public void setPurchasedPrice(double purchasedPrice) throws NonPositiveInputException {
		if(purchasedPrice <= 0)
			throw new NonPositiveInputException("purchased price");
		
		this.purchasedPrice = purchasedPrice;
	}
	
	public double getOriginalPrice() {
		return originalPrice;
	}
	
	public void setOriginalPrice(double originalPrice) throws NonPositiveInputException {
		if(originalPrice <= 0)
			throw new NonPositiveInputException("original price");
		
		this.originalPrice = originalPrice;
	}
	
	public double getSellingPrice() {
		return sellingPrice;
	}
	
	public void setSellingPrice(double sellingPrice) throws NonPositiveInputException {
		if(sellingPrice <= 0)
			throw new NonPositiveInputException("selling price");
		
		this.sellingPrice = sellingPrice;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) throws NonPositiveInputException {
		if(stock <= 0)
			throw new NonPositiveInputException("stock");
		
		this.stock = stock;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BookInventory))
			return false;
		
		BookInventory model = (BookInventory) o;
		return getBook().equals(model.getBook()) && getPurchasedDate().equals(model.getPurchasedDate());
	}
	
	@Override
	public int hashCode() {
		return getBook().hashCode() + getPurchasedDate().hashCode();
	}
}
