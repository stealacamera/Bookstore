package bll.IServices;

import bll.dto.EmployeeDTO;
import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import exceptions.WrongLengthException;

public interface IEmployeeService extends IService<EmployeeDTO> {
	EmployeeDTO getById(int id);
	EmployeeDTO getByUsername(String username);
	void update(EmployeeDTO oldInstance, EmployeeDTO newInstance) throws ExistingObjectException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException;
	
	boolean changePassword(EmployeeDTO employee, String oldPassword, String newPassword) throws Exception;
	boolean canLogin(String username, String password) throws EmptyInputException, WrongFormatException;
}
