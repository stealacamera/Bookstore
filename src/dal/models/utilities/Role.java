package dal.models.utilities;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Role implements Serializable {
	CREATE_BILL("Create bill"),
	MANAGE_BOOKS("Manage books"),
	ADD_NEW_CATEGORY("Add new category"),
	GET_BOOK_STATS("Get book statistics"),
	GET_REVENUE_STATS("Get store revenue statistics"),
	GET_LIBR_PERFORMANCE("Get librarians' performance"),
	MANAGE_EMPLOYEES("Manage employees");
	
	private final String text;
	
	Role(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
	public static Set<Role> getLibrarianPermissions() {
		return new HashSet<Role>(Arrays.asList(CREATE_BILL));
	}
	
	public static Set<Role> getManagerPermissions() {
		return new HashSet<Role>(Arrays.asList(MANAGE_BOOKS, ADD_NEW_CATEGORY, 
				GET_BOOK_STATS, GET_LIBR_PERFORMANCE));
	}
	
	public static Set<Role> getAdminPermissions() {
		Set<Role> adminPermissions = new HashSet<>();
		adminPermissions.addAll(getLibrarianPermissions());
		adminPermissions.addAll(getManagerPermissions());
		
		adminPermissions.add(GET_REVENUE_STATS);
		adminPermissions.add(MANAGE_EMPLOYEES);
		
		return adminPermissions;
	}
}
