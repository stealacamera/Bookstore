package bll;

import bll.IServices.IBillService;
import bll.dto.BillDTO;
import dal.IRepositories.IBillRepository;
import exceptions.NonPositiveInputException;
import dal.models.Bill;

public class BillService extends Service<Bill ,BillDTO> implements IBillService {
	private final IBillRepository db;
	
	public BillService(IBillRepository db) {
		super(db);
		this.db = db;
	}

	@Override
	public int count() {
		return db.count();
	}

	@Override
	protected BillDTO convertToDTO(Bill model) {
		return new BillDTO(model.getSellerId(), model.getSaleAmount(), model.getNumOfBooks());
	}

	@Override
	protected Bill convertToDAO(BillDTO model) throws NonPositiveInputException {
		return new Bill(model.getSellerId(), model.getSaleAmount(), model.getNumOfBooks());
	}
	
}
