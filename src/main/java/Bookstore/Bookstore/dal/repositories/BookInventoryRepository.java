package Bookstore.Bookstore.dal.repositories;

import Bookstore.Bookstore.dal.repositories.irepositories.IBookInventoryRepository;
import Bookstore.Bookstore.dal.models.BookInventory;

public class BookInventoryRepository extends Repository<BookInventory> implements IBookInventoryRepository {
	
	public BookInventoryRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, BookInventory.class));
	}

	@Override
	public BookInventory getByISBN(String isbn) {
		return instances.stream().filter(e -> e.getBook().getIsbn().equals(isbn)).findFirst().orElse(null);
	}
}
