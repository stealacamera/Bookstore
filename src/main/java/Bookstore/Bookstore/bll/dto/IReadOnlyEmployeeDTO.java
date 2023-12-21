package Bookstore.Bookstore.bll.dto;

import java.util.Map;

import Bookstore.Bookstore.dal.models.utils.Role;

public interface IReadOnlyEmployeeDTO extends IReadOnlyUserDTO {
	double getSalary();
	int getAccessLvl();
	Map<Role, Boolean> getPermissions();
}
