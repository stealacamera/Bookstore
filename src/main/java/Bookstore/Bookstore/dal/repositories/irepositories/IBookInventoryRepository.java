package Bookstore.Bookstore.dal.repositories.irepositories;

import Bookstore.Bookstore.dal.models.BookInventory;

public interface IBookInventoryRepository extends IRepository<BookInventory> {
	BookInventory getByISBN(String isbn);
}
