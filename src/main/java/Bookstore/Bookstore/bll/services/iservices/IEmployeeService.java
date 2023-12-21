package Bookstore.Bookstore.bll.services.iservices;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;

public interface IEmployeeService extends IService<EmployeeDTO> {
	EmployeeDTO getById(int id);
	EmployeeDTO getByUsername(String username);
	void update(EmployeeDTO oldInstance, EmployeeDTO newInstance) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException;
	
	boolean changePassword(EmployeeDTO employee, String oldPassword, String newPassword) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException;
	boolean canLogin(String username, String password) throws EmptyInputException;
}
