package dal.IRepositories;

import dal.models.BookInventory;

public interface IBookInventoryRepository extends IRepository<BookInventory> {
	BookInventory getByISBN(String isbn);
}
