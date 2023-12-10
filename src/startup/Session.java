package startup;

import bll.dto.EmployeeDTO;

public class Session {
	private static EmployeeDTO currentUser;
	
	public static void setCurrentUser(EmployeeDTO employee) {
		currentUser = employee;
	}
	
	public static EmployeeDTO getCurrentUser() {
		return currentUser;
	}
}