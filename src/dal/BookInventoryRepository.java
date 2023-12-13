package dal;

import dal.IRepositories.IBookInventoryRepository;
import dal.models.BookInventory;

public class BookInventoryRepository extends Repository<BookInventory> implements IBookInventoryRepository {
	
	public BookInventoryRepository(DbContext context) {
		super(context.table(BookInventory.class));
	}

	@Override
	public BookInventory getByISBN(String isbn) {
		return instances.stream().filter(e -> e.getBook().getIsbn().equals(isbn)).findFirst().orElse(null);
	}
}
