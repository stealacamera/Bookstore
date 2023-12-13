package bll.dto;

import java.util.Map;

import dal.models.utilities.Role;

public interface IReadOnlyEmployeeDTO extends IReadOnlyUserDTO {
	double getSalary();
	int getAccessLvl();
	Map<Role, Boolean> getPermissions();
}
