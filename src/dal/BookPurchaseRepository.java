package dal;

import dal.IRepositories.IBookPurchaseRepository;
import dal.models.BookPurchase;

public class BookPurchaseRepository extends Repository<BookPurchase> implements IBookPurchaseRepository {
	
	public BookPurchaseRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, BookPurchase.class));
	}
}