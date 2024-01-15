package Bookstore.Bookstore.bll.dto;

public class BookDTO {
	private String isbn, title, author, supplier;
	private int categoryId;
	
	public BookDTO(BookDTO model) {
		this(model.getIsbn(), model.getTitle(), model.getAuthor(), model.getSupplier(), model.getCategoryId());
	}
	
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
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof BookDTO))
			return false;
		
		BookDTO model = (BookDTO) o;
		
		return this.getIsbn().equals(model.getIsbn()) && getTitle().equals(model.getTitle()) &&
			getAuthor().equals(model.getAuthor()) && getSupplier().equals(model.getSupplier());
	}
	
	@Override
	public int hashCode() {
		return getTitle().hashCode();
	}
	
	@Override
	public String toString() {
		return String.format("(%s) \"%s\" by %s", getIsbn(), getTitle(), getAuthor());
	}
}
