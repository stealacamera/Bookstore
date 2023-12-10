package bll;

import bll.IServices.IBookPurchaseService;
import bll.dto.BookPurchaseDTO;
import dal.IRepositories.IBookPurchaseRepository;
import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import models.BookPurchase;

public class BookPurchaseService extends Service<BookPurchase, BookPurchaseDTO> implements IBookPurchaseService {
	@SuppressWarnings("unused")
	private final IBookPurchaseRepository db;
	
	public BookPurchaseService(IBookPurchaseRepository db) {
		super(db);
		this.db = db;
	}

	@Override
	protected BookPurchaseDTO convertToDTO(BookPurchase model) {
		return new BookPurchaseDTO(model.getAmount(), model.getDate());
	}

	@Override
	protected BookPurchase convertToDAO(BookPurchaseDTO model) throws NonPositiveInputException, EmptyInputException {
		return new BookPurchase(model.getAmount(), model.getDate());
	}
}
