package bll;

import bll.IServices.IBookPurchaseService;
import bll.dto.BookPurchaseDTO;
import dal.IRepositories.IBookPurchaseRepository;
import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import dal.models.BookPurchase;
import dal.models.utilities.CustomDate;

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
