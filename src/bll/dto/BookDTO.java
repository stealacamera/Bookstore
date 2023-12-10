package bll.dto;

public class BookDTO {
	private String isbn, title, author, supplier;
	private int categoryId;
	
	public BookDTO(String isbn, String title, String author, String supplier, int categoryId) {
		setIsbn(isbn);
		setTitle(title);
		setAuthor(author);
		setSupplier(supplier);
		setCategoryId(categoryId);
	}
	
	public String getIsbn() {
		return isbn;
	}
	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}	
}
