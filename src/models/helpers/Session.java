package models.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import models.Employee;

public class Session {
	private static Employee currentUser;
	
	public static void setCurrentUser(int userIndex) {
		currentUser = Employee.get(userIndex);
	}
	
	public static Employee getCurrentUser() {
		return new Employee(currentUser);
	}
	
	public static String getUsername() {
		return currentUser.getUsername();
	}
	
	public static int getAccessLvl() {
		return currentUser.getAccessLvl();
	}
	
	public static Map<Role, Boolean> getUserPermissionStatus() {
		return Collections.unmodifiableMap(currentUser.getPermissions());
	}
	
	public static boolean changePassword(String currentPassword, String newPassword) throws Exception {
		if(currentUser.isCorrectPassword(currentPassword)) {
			currentUser.setPassword(newPassword);
			ListIO.writeToFile(Employee.FILE_NAME, new ArrayList<>(Employee.getAll()));
			
			return true;
		}
		
		return false;
	}
}
