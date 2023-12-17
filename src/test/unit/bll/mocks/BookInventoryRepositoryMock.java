package test.unit.bll.mocks;

import dal.IRepositories.IBookInventoryRepository;
import dal.models.BookInventory;

public class BookInventoryRepositoryMock extends RepositoryMock<BookInventory> implements IBookInventoryRepository {

	@Override
	public BookInventory getByISBN(String isbn) {
		return dummyData.stream().filter(e -> e.getBook().getIsbn().equals(isbn)).findFirst().orElse(null);
	}

}
