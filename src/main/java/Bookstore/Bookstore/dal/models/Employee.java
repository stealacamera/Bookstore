package Bookstore.Bookstore.dal.models;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.IncorrectPermissionsException;
import Bookstore.Bookstore.commons.exceptions.NonPositiveInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;
import Bookstore.Bookstore.dal.models.utils.Role;

public class Employee extends User implements Serializable, Comparable<Employee> {
	private static final long serialVersionUID = 7532508037401596791L;
	
	private double salary;
	private int accessLvl;
	private Map<Role, Boolean> permissions;
	
	public Employee(User base, double salary, int accessLvl) throws EmptyInputException, NonPositiveInputException, WrongFormatException {
		super(base);
		setSalary(salary);
		setAccessLvl(accessLvl);
		setPermissions(accessLvl);
	}
	
	public Employee(User base, double salary, int accessLvl, Map<Role, Boolean> permissions) throws Exception {
		this(base, salary, accessLvl);
		setPermissions(permissions);
	}
		
	public double getSalary() {
		return salary;
	}
		
	public void setSalary(double salary) throws EmptyInputException, NonPositiveInputException {
		if(salary <= 0)
			throw new NonPositiveInputException("salary");
		
		this.salary = salary;
	}
	
	public int getAccessLvl() {
		return accessLvl;
	}
	
	public void setAccessLvl(int accessLvl) throws WrongFormatException {
		if(accessLvl < 1 || accessLvl > 3)
			throw new WrongFormatException("access level", "1 - Librarian; 2 - Manager; 3 - Admin");
		
		this.accessLvl = accessLvl;
	}
	
	public void setPermissions(int accessLvl) {
		Set<Role> rolePermissions;
		HashMap<Role, Boolean> permissionStatus = new HashMap<>();
		
		switch(accessLvl) {
			case 3: rolePermissions = Role.getAdminPermissions(); break;
			case 2: rolePermissions = Role.getManagerPermissions(); break;
			default: rolePermissions = Role.getLibrarianPermissions(); break;
		}
		
		for(Role permission: rolePermissions)
			permissionStatus.put(permission, true);
		
		this.permissions = permissionStatus;
	}
	
	public void setPermissions(Map<Role, Boolean> permissionStatuses) throws IncorrectPermissionsException {
		if(!permissions.keySet().containsAll(permissionStatuses.keySet()))
			throw new IncorrectPermissionsException("Permissions given do not match access level.");
			
		for(Map.Entry<Role, Boolean> permission: permissionStatuses.entrySet())
			permissions.put(permission.getKey(), permission.getValue());
	}	
	
	public Map<Role, Boolean> getPermissions() {
		return Collections.unmodifiableMap(permissions);
	}
	
	@Override
	public String toString() {
		String role;
		
		switch(accessLvl) {
			case 1: role = "Librarian"; break;
			case 2: role = "Manager"; break;
			default: role = "Administrator";
		}
		
		return String.format("%s (%s) [%s]", getUsername(), getFullName(), role);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Employee))
			return false;
		
		Employee model = (Employee) o;
		
		return getId() == model.getId() && getAccessLvl() == model.getAccessLvl() &&
				getPermissions().equals(model.getPermissions()) && getEmail().equals(model.getEmail()) &&
				getHashedPassword().equals(model.getHashedPassword());
	}
	
	@Override
	public int hashCode() {
		return getId() + getAccessLvl() + getEmail().hashCode();
	}
	
	@Override
	public int compareTo(Employee o) {
		return getUsername().compareTo(o.getUsername());
	}
}
