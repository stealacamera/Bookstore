package models;

import java.io.Serializable;

import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import models.utilities.CustomDate;

public class BookInventory implements Serializable {
	private static final long serialVersionUID = -2947267998942410788L;
	
	private Book book;
	private CustomDate purchasedDate;
	private double purchasedPrice, originalPrice, sellingPrice;
	private int stock;
	
	private BookInventory(Book book, double purchasedPrice, double originalPrice, double sellingPrice, int stock) throws NonPositiveInputException {
		setBook(book);
		setPurchasedPrice(purchasedPrice);
		setOriginalPrice(originalPrice);
		setSellingPrice(sellingPrice);
		setStock(stock);
	}
	
	public BookInventory(Book book, double purchasedPrice, double originalPrice, double sellingPrice, int stock, CustomDate purchasedDate) throws NonPositiveInputException {
		this(book, purchasedPrice, originalPrice, sellingPrice, stock);
		setPurchasedDate(purchasedDate);
	}
	
	public BookInventory(Book book, double purchasedPrice, double originalPrice, double sellingPrice, int stock, String purchasedDate) throws NonPositiveInputException, EmptyInputException, WrongFormatException {
		this(book, purchasedPrice, originalPrice, sellingPrice, stock);
		setPurchasedDate(purchasedDate);
	}
	
	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}
	
	public CustomDate getPurchasedDate() {
		return new CustomDate(purchasedDate.getDate());
	}
	
	public void setPurchasedDate(CustomDate purchasedDate) {
		this.purchasedDate = purchasedDate;
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
}
