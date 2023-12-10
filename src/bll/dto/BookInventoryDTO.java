package bll.dto;

import models.utilities.CustomDate;

public class BookInventoryDTO {
	private BookDTO book;
	private CustomDate purchasedDate;
	private double purchasedPrice, originalPrice, sellingPrice;
	private int stock;
	
	public BookInventoryDTO(BookDTO book, double purchasedPrice, double originalPrice, double sellingPrice, int stock, CustomDate purchasedDate) {
		setBook(book);
		setPurchasedPrice(purchasedPrice);
		setOriginalPrice(originalPrice);
		setSellingPrice(sellingPrice);
		setStock(stock);
		setPurchasedDate(purchasedDate);
	}
	
	public BookDTO getBook() {
		return book;
	}
	public void setBook(BookDTO book) {
		this.book = book;
	}
	public CustomDate getPurchasedDate() {
		return purchasedDate;
	}
	public void setPurchasedDate(CustomDate purchasedDate) {
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
}
