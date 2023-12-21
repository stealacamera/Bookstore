package Bookstore.Bookstore.dal.repositories;

import Bookstore.Bookstore.dal.repositories.irepositories.IBookPurchaseRepository;
import Bookstore.Bookstore.dal.models.BookPurchase;

public class BookPurchaseRepository extends Repository<BookPurchase> implements IBookPurchaseRepository {
	
	public BookPurchaseRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, BookPurchase.class));
	}
}