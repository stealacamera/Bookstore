package Bookstore.Bookstore.bll.services;

import Bookstore.Bookstore.bll.dto.BillDTO;
import Bookstore.Bookstore.bll.services.iservices.IBillService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.dal.models.Bill;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.irepositories.IBillRepository;

public class BillService extends Service<Bill ,BillDTO> implements IBillService {
	@SuppressWarnings("unused")
	private final IBillRepository db;
	
	public BillService(IBillRepository db) {
		super(db);
		this.db = db;
	}

	@Override
	protected BillDTO convertToDTO(Bill model) {
		return new BillDTO(model.getSellerId(), model.getSaleAmount(), model.getNumOfBooks(), model.getDate().getDate());
	}

	@Override
	protected Bill convertToDAO(BillDTO model) throws NonPositiveInputException, EmptyInputException {
		return new Bill(model.getSellerId(), model.getSaleAmount(), model.getNumOfBooks(), new CustomDate(model.getDate()));
	}
}
