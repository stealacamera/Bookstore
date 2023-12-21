package Bookstore.Bookstore.bll.services;

import Bookstore.Bookstore.dal.models.BookPurchase;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.irepositories.IBookPurchaseRepository;
import Bookstore.Bookstore.bll.services.iservices.IBookPurchaseService;
import Bookstore.Bookstore.bll.dto.BookPurchaseDTO;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;

public class BookPurchaseService extends Service<BookPurchase, BookPurchaseDTO> implements IBookPurchaseService {
	@SuppressWarnings("unused")
	private final IBookPurchaseRepository db;
	
	public BookPurchaseService(IBookPurchaseRepository db) {
		super(db);
		this.db = db;
	}

	@Override
	protected BookPurchaseDTO convertToDTO(BookPurchase model) {
		return new BookPurchaseDTO(model.getAmount(), model.getDate().getDate());
	}

	@Override
	protected BookPurchase convertToDAO(BookPurchaseDTO model) throws NonPositiveInputException, EmptyInputException {
		return new BookPurchase(model.getAmount(), new CustomDate(model.getDate()));
	}
}
