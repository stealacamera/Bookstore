package Bookstore.Bookstore.dal.repositories;

import Bookstore.Bookstore.dal.repositories.irepositories.IBillRepository;
import Bookstore.Bookstore.dal.models.Bill;

public class BillRepository extends Repository<Bill> implements IBillRepository {
	
	public BillRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, Bill.class));
	}
}
