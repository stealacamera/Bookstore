package models;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import exceptions.EmptyInputException;
import exceptions.IncorrectPermissionsException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import models.utilities.Role;

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
	
	public Employee(User base, double salary, int accessLvl, boolean[] permissionStatuses) throws Exception {
		this(base, salary, accessLvl);
		setPermissionValues(permissionStatuses);
	}
	
	public Employee(User base, double salary, int accessLvl, Map<Role, Boolean> permissions) throws Exception {
		this(base, salary, accessLvl);
		setPermissions(permissions);
	}
		
	public double getSalary() {
		return salary;
	}
		
	public void setSalary(Double salary) throws EmptyInputException, NonPositiveInputException {
		if(salary == null)
			throw new EmptyInputException("salary");
		
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
			case 1: rolePermissions = Role.getLibrarianPermissions();
					break;
			case 2: rolePermissions = Role.getManagerPermissions();
					break;
			default: rolePermissions = Role.getAdminPermissions();
					break;
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
	
	public void setPermissionValues(boolean[] permissionStatuses) throws IncorrectPermissionsException {
		if(permissionStatuses.length != permissions.size())
			throw new IncorrectPermissionsException("Nr. of permissions does not match access level.");
		
		int i = 0;
		
		for(Role permission: this.permissions.keySet())
			this.permissions.put(permission, permissionStatuses[i++]);
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
		
		return getUsername() + " (" + getFullName() + ") (" + role + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int compareTo(Employee o) {
		return getUsername().compareTo(o.getUsername());
	}
}
