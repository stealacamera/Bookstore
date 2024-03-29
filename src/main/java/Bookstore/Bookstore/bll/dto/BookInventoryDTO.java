package Bookstore.Bookstore.bll.dto;

import java.time.LocalDate;

public class BookInventoryDTO {
	private BookDTO book;
	private LocalDate purchasedDate;
	private double purchasedPrice, originalPrice, sellingPrice;
	private int stock;
	
	public BookInventoryDTO(BookDTO book, double purchasedPrice, double originalPrice, double sellingPrice, int stock, LocalDate purchasedDate) {
		setBook(book);
		setPurchasedPrice(purchasedPrice);
		setOriginalPrice(originalPrice);
		setSellingPrice(sellingPrice);
		setStock(stock);
		setPurchasedDate(purchasedDate);
	}
	
	public BookDTO getBook() {
		return new BookDTO(book);
	}
	public void setBook(BookDTO book) {
		this.book = new BookDTO(book);
	}
	public LocalDate getPurchasedDate() {
		return purchasedDate;
	}
	public void setPurchasedDate(LocalDate purchasedDate) {
		this.purchasedDate = purchasedDate;
	}
	public double getPurchasedPrice() {
		return purchasedPrice;
	}
	public void setPurchasedPrice(double purchasedPrice) {
		this.purchasedPrice = purchasedPrice;
	}
	public double getOriginalPrice() {
		return originalPrice;
	}
	public void setOriginalPrice(double originalPrice) {
		this.originalPrice = originalPrice;
	}
	public double getSellingPrice() {
		return sellingPrice;
	}
	public void setSellingPrice(double sellingPrice) {
		this.sellingPrice = sellingPrice;
	}
	public int getStock() {
		return stock;
	}
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BookInventoryDTO))
			return false;
		
		BookInventoryDTO model = (BookInventoryDTO) o;
		
		return getBook().equals(model.getBook()) && getStock() == model.getStock() && getPurchasedDate().equals(model.getPurchasedDate());
	}
	
	@Override
	public int hashCode() {
		return getBook().hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("%s\nPurchased at: %s, Stock: %d\nPurchased price: %.3f, Original price: %.3f, Selling price: %.3f", 
			   book.toString(), getPurchasedDate(), getStock(), getPurchasedPrice(), getOriginalPrice(), getSellingPrice());
	}
}
