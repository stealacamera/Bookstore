package Bookstore.Bookstore.startup;

import Bookstore.Bookstore.bll.dto.EmployeeDTO;
import Bookstore.Bookstore.bll.dto.IReadOnlyEmployeeDTO;

public class Session {
	private static IReadOnlyEmployeeDTO currentUser;
	
	public static void setCurrentUser(EmployeeDTO employee) {
		currentUser = new EmployeeDTO(employee);
	}
	
	public static IReadOnlyEmployeeDTO getCurrentUser() {
		return currentUser;
	}
	
	public static void clear() {
		currentUser = null;
	}
}