package startup;

import bll.dto.EmployeeDTO;
import bll.dto.IReadOnlyEmployeeDTO;

public class Session {
	private static IReadOnlyEmployeeDTO currentUser;
	
	public static void setCurrentUser(EmployeeDTO employee) {
		currentUser = new EmployeeDTO(employee);
	}
	
	public static IReadOnlyEmployeeDTO getCurrentUser() {
		return currentUser;
	}
}