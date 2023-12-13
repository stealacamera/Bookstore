package dal;

import dal.IRepositories.IBillRepository;
import dal.models.Bill;

public class BillRepository extends Repository<dal.models.Bill> implements IBillRepository {
	
	public BillRepository(DbContext context) {
		super(context.table(Bill.class));
	}
}
