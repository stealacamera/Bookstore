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
		
		return this.getBook().getIsbn().equals(((BookInventoryDTO) o).getBook().getIsbn());
	}
	
	@Override
	public int hashCode() {
		return getBook().hashCode();
	}
}
