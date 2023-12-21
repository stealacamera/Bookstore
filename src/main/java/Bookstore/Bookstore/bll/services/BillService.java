package Bookstore.Bookstore.bll.services;

import Bookstore.Bookstore.dal.models.Bill;
import Bookstore.Bookstore.dal.repositories.irepositories.IBillRepository;
import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;

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
