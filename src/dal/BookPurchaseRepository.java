package dal;

import dal.IRepositories.IBookPurchaseRepository;
import models.BookPurchase;

public class BookPurchaseRepository extends Repository<BookPurchase> implements IBookPurchaseRepository {
	
	public BookPurchaseRepository(DbContext context) {
		super(context.table(BookPurchase.class));
	}
}