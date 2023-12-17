package dal;

import dal.IRepositories.IBillRepository;
import dal.models.Bill;

public class BillRepository extends Repository<dal.models.Bill> implements IBillRepository {
	
	public BillRepository(String dataDirPath, DbContext context) {
		super(context.table(dataDirPath, Bill.class));
	}
}
