package models;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.CustomDate;
import models.helpers.ListIO;

public class Book implements Serializable {
	private static final long serialVersionUID = -7995962663053711982L;
	public static final String FILE_NAME = "books.dat";
	private static ObservableList<Book> books;
	
	private String isbn, title, author, supplier, category;
	private CustomDate purchasedDate;
	private double purchasedPrice, originalPrice, sellingPrice;
	private int stock;
	
	public Book(File newBookFile) throws EmptyInputException, WrongFormatException, NonPositiveInputException, IOException {
		try(Scanner scan = new Scanner(newBookFile, Charset.forName("UTF-8"))) {
			setIsbn(scan.nextLine());
			setTitle(scan.nextLine());
			setAuthor(scan.nextLine());
			setCategory(scan.nextLine());
			
			setSupplier(scan.nextLine());
			setPurchasedDate(scan.nextLine());
			
			setPurchasedPrice(scan.nextDouble());
			setOriginalPrice(scan.nextDouble());
			setSellingPrice(scan.nextDouble());
			setStock(scan.nextInt());
		} catch(InputMismatchException ex) {
			throw new WrongFormatException("input", "character input for text fields, numeric input for price fields");
		} catch (IOException e) {
			throw new IOException("Illegal/unrecognizable character(s) used");
		} finally {
			if(!newBookFile.delete())
				throw new IOException("File could not be deleted");
		}
	}
	
	//Class methods
	public static void setList() {
		ArrayList<Book> currentList = ListIO.readFromFile(FILE_NAME);
		books = currentList == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(currentList);
	}
	
	public static ObservableList<Book> getAll() {
		return new ReadOnlyListWrapper<>(books);
	}
	
	public static boolean add(Book book) throws ExistingObjectException {
		if(books.contains(book))
			throw new ExistingObjectException("ISBN");
		
		if(book != null) {
			books.add(book);
			return true;
		}
		
		return false;
	}
	
	public static void remove(int index) {
		books.remove(index);
	}
	
	//Getters & setters
	public String getIsbn() {
		return isbn;
	}
	
	public void setIsbn(String isbn) throws EmptyInputException, WrongFormatException {
		if(isbn.isBlank())
			throw new EmptyInputException("ISBN");
		
		if(!isbn.matches("\\d{3}-*\\d-*\\d{2}-*\\d{6}-*\\d"))
			throw new WrongFormatException("ISBN", "(3 digits)-(1 digit)-(2 digits)-(6 digits)-(1 digit) or (3 digits)(1 digit)(2 digits)(6 digits)(1 digit)");
		
		this.isbn = isbn.replace("-", "");
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) throws EmptyInputException, WrongFormatException {
		if(title.isBlank())
			throw new EmptyInputException("title");
		
		if(title.length() > 30)
			throw new WrongFormatException("title", "titles should be 30 characters or less");
		
		this.title = title;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setAuthor(String author) throws EmptyInputException{
		if(author.isBlank())
			throw new EmptyInputException("author");
		
		if(author.matches("[a-zA-Z]{2,}\\s[a-zA-Z]+.*"))
			this.author = author;
	}
	
	public String getSupplier() {
		return supplier;
	}
	
	public void setSupplier(String supplier) throws EmptyInputException {
		if(supplier.isBlank())
			throw new EmptyInputException("supplier");
		
		this.supplier = supplier;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) throws EmptyInputException {
		if(category.isBlank())
			throw new EmptyInputException("category");
		
		this.category = category;
	}
	
	public String getPurchasedDate() {
		return purchasedDate.toString();
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
	public boolean equals(Object obj) {
		if(obj instanceof Book)
			return ((Book) obj).getIsbn().equals(getIsbn());
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.isbn.hashCode();
	}
	
	@Override
	public String toString() {
		return getTitle() + " (" + getAuthor() + ")";
	}
}
