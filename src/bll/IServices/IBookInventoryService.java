package bll.IServices;

import bll.dto.BookInventoryDTO;
import exceptions.EmptyInputException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;

public interface IBookInventoryService extends IService<BookInventoryDTO> {

	void updateStock(BookInventoryDTO instance, int newStock) throws EmptyInputException, WrongFormatException, WrongLengthException, NonPositiveInputException;
	
}
