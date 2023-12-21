package Bookstore.Bookstore.bll.services;

import java.util.Collections;

import Bookstore.Bookstore.dal.models.Employee;
import Bookstore.Bookstore.dal.models.User;
import Bookstore.Bookstore.dal.models.utils.CustomDate;
import Bookstore.Bookstore.dal.repositories.irepositories.IEmployeeRepository;
import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.UserDTO;
import Bookstore.Bookstore.bll.services.iservices.IEmployeeService;
import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.ExistingObjectException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.commons.exceptions.WrongLengthException;
import Bookstore.Bookstore.commons.utils.Utils;

public class EmployeeService extends Service<Employee, EmployeeDTO> implements IEmployeeService {
	private final IEmployeeRepository db;
	
	public EmployeeService(IEmployeeRepository db) {
		super(db);
		this.db = db;
	}
	
	@Override
	public EmployeeDTO getById(int id) {
		Employee instance = db.getById(id);		
		return instance == null ? null : convertToDTO(instance);
	}
	
	@Override
	public EmployeeDTO getByUsername(String username) {
		Employee model = db.getByUsername(username);
		return model == null ? null : convertToDTO(model);
	}
	
	// Adds instances with unique usernames
	// Objects are ordered alphabetically by username
	@Override
	public boolean add(EmployeeDTO instance) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, ExistingObjectException, IncorrectPermissionsException {
		if(instance == null)
			return false;
		
		add(convertToDAO(instance));
		db.saveChanges();
		return true;
	}
	
	@Override
	public void update(EmployeeDTO oldModel, EmployeeDTO newModel) throws ExistingObjectException, IncorrectPermissionsException, EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException {
		Employee newInstance = convertToDAO(newModel), oldInstance = convertToDAO(oldModel);
		
		// Reposition instance in list (db) if the username has changed
		// otherwise, replace
		boolean toReposition = !oldInstance.getUsername().equals(newInstance.getUsername());
		int listIndex = Collections.binarySearch(db.getAll(), oldInstance, Employee::compareTo);
		listIndex = Utils.getOrderedListIndex(listIndex);
		
		db.remove(listIndex);
		
		if(toReposition)
			add(newInstance);
		else
			db.add(listIndex, newInstance);
		
		db.saveChanges();
	}
	
	private void add(Employee instance) throws ExistingObjectException {
		int listIndex = Collections.binarySearch(db.getAll(), instance, Employee::compareTo);

		if(listIndex >= 0)
			throw new ExistingObjectException("username");
		
		db.add(Utils.getOrderedListIndex(listIndex), instance);
	}
	
	@Override
	public boolean changePassword(EmployeeDTO employee, String oldPassword, String newPassword) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		Employee model = convertToDAO(employee);
		
		if(model.isCorrectPassword(oldPassword)) {
			model.setPassword(newPassword);
			db.saveChanges();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean canLogin(String username, String password) throws EmptyInputException {
		if(username == null || username.isBlank())
			throw new EmptyInputException("username");
		
		Employee instance = db.getByUsername(username);
		return instance == null ? false : instance.isCorrectPassword(password);
	}
	
	@Override
	protected EmployeeDTO convertToDTO(Employee model) {
		UserDTO instanceUser = new UserDTO(model.getId(),
				model.getUsername(), model.getFullName(), 
				model.getEmail(), model.getHashedPassword(),
				model.getPhoneNum(), model.getBirthdate().getDate());
		
		EmployeeDTO instance = new EmployeeDTO(instanceUser, model.getSalary(), model.getAccessLvl(), model.getPermissions());
		return instance;
	}

	@Override
	protected Employee convertToDAO(EmployeeDTO model) throws EmptyInputException, NonPositiveInputException, WrongFormatException, WrongLengthException, IncorrectPermissionsException {
		User instanceUser;
		
		if(model.getId() == 0) {
			instanceUser = new User(
				model.getUsername(), model.getFullName(), 
				model.getEmail(), model.getPassword(), 
				model.getPhoneNum(), new CustomDate(model.getBirthdate()));
		} else
			instanceUser = new User(model.getId(),
					model.getUsername(), model.getFullName(), 
					model.getEmail(), model.getHashedPassword(), 
					model.getPhoneNum(), new CustomDate(model.getBirthdate()));
		
		Employee instance = new Employee(instanceUser, model.getSalary(), model.getAccessLvl());
		
		if(model.getId() != 0)
			instance.setPermissions(model.getPermissions());

		return instance;
	}
}
