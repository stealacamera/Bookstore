package Bookstore.Bookstore.bll.services.iservices;

import Bookstore.Bookstore.bll.dto.BookInventoryDTO;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;

public interface IBookInventoryService extends IService<BookInventoryDTO> {
	BookInventoryDTO getByISBN(String isbn) throws EmptyInputException;
	void updateStock(BookInventoryDTO instance, int newStock) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException;
	
}
