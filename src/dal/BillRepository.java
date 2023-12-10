package dal;

import dal.IRepositories.IBillRepository;
import models.Bill;

public class BillRepository extends Repository<Bill> implements IBillRepository {
	
	public BillRepository(DbContext context) {
		super(context.table(Bill.class));
	}
}
