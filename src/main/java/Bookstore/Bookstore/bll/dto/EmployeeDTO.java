package Bookstore.Bookstore.bll.dto;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import Bookstore.Bookstore.dal.models.utils.Role;

public class EmployeeDTO extends UserDTO implements IReadOnlyEmployeeDTO {
	private Map<Role, Boolean> permissions;
	private double salary;
	private int accessLvl;
	
	public EmployeeDTO() {
		super();
	}
	
	public EmployeeDTO(IReadOnlyEmployeeDTO model) {
		super(new UserDTO(
				model.getId(), model.getUsername(), 
				model.getFullName(), model.getEmail(), 
				model.getHashedPassword(), model.getPhoneNum(), model.getBirthdate()));
		
		setSalary(model.getSalary());
		setAccessLvl(model.getAccessLvl());
		setPermissions(model.getPermissions());
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
	@Override
	public double getSalary() {
		return salary;
	}
	public void setSalary(double salary) {
		this.salary = salary;
	}
	@Override
	public int getAccessLvl() {
		return accessLvl;
	}
	public void setAccessLvl(int accessLvl) {
		this.accessLvl = accessLvl;
	}
	@Override
	public Map<Role, Boolean> getPermissions() {
		return new HashMap<>(permissions);
	}
	public void setPermissions(Map<Role, Boolean> permissions) {
		this.permissions = new HashMap<>(permissions);
	}
	public void setPermissionStatuses(boolean[] permissionStatuses) {
		Set<Role> roles = getAccessLvl() == 1 ? Role.getLibrarianPermissions() : Role.getManagerPermissions();
		Map<Role, Boolean> permissions = new HashMap<>();
		int i = 0;
		
		for(Role role: roles)
			permissions.put(role, permissionStatuses[i++]);
		
		this.permissions = permissions;
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
	public boolean equals(Object o) {
		if(!(o instanceof UserDTO))
			return false;
		
		if(!(o instanceof EmployeeDTO))
			return false;
		
		EmployeeDTO model = (EmployeeDTO) o;
		
		return getId() == model.getId() && getAccessLvl() == model.getAccessLvl() &&
				getEmail().equals(model.getEmail()) &&
				(getHashedPassword().equals(model.getHashedPassword()) || getPassword().equals(model.getPassword()));
	}
	
	@Override
	public int hashCode() {
		return super.hashCode() + getAccessLvl();
	}
}
