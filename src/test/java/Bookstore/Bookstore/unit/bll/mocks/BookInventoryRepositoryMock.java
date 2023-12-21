package Bookstore.Bookstore.unit.bll.mocks;

import Bookstore.Bookstore.dal.models.BookInventory;
import Bookstore.Bookstore.dal.repositories.irepositories.IBookInventoryRepository;

public class BookInventoryRepositoryMock extends RepositoryMock<BookInventory> implements IBookInventoryRepository {

	@Override
	public BookInventory getByISBN(String isbn) {
		return dummyData.stream().filter(e -> e.getBook().getIsbn().equals(isbn)).findFirst().orElse(null);
	}

}
