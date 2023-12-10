package bll.dto;

import java.util.Map;

import models.utilities.Role;

public class EmployeeDTO extends UserDTO {
	private Map<Role, Boolean> permissions;
	private boolean[] permissionStatuses;
	private double salary;
	private int accessLvl;
	
	public EmployeeDTO() {
		super();
	}
	
	public EmployeeDTO(UserDTO base, double salary, int accessLvl) {
		super(base);
		setSalary(salary);
		setAccessLvl(accessLvl);
	}
	
	public EmployeeDTO(UserDTO base, double salary, int accessLvl, Map<Role, Boolean> permissions) {
		this(base, salary, accessLvl);
		setPermissions(permissions);
	}
	
	public EmployeeDTO(UserDTO base, double salary, int accessLvl, boolean[] permissionStatuses) {
		this(base, salary, accessLvl);
		setPermissionStatuses(permissionStatuses);
	}
	
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	public int getAccessLvl() {
		return accessLvl;
	}
	public void setAccessLvl(int accessLvl) {
		this.accessLvl = accessLvl;
	}
	public Map<Role, Boolean> getPermissions() {
		return permissions;
	}
	public void setPermissions(Map<Role, Boolean> permissions) {
		this.permissions = permissions;
	}
	public boolean[] getPermissionStatuses() {
		return permissionStatuses;
	}
	public void setPermissionStatuses(boolean[] permissionStatuses) {
		this.permissionStatuses = permissionStatuses;
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
}
