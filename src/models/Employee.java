package models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

import exceptions.EmptyInputException;
import exceptions.ExistingObjectException;
import exceptions.NonPositiveInputException;
import exceptions.WrongFormatException;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.helpers.CustomDate;
import models.helpers.ListIO;
import models.helpers.Role;

public class Employee implements Serializable, Comparable<Employee> {
	private static final long serialVersionUID = 7532508037401596791L;
	public static final String FILE_NAME = "employees.dat";
	private static ObservableList<Employee> employees;
	
	private String username, fullName, email, password, phoneNum;
	private double salary;
	private int accessLvl;
	
	private CustomDate birthdate;
	private HashMap<Role, Boolean> permissions;
	
	private Employee() {};
	
	public Employee(File newUserFile) throws FileNotFoundException, EmptyInputException, WrongFormatException, NonPositiveInputException, InputMismatchException {
		try(Scanner scan = new Scanner(newUserFile)) {			
			setUsername(scan.nextLine());
			setFullName(scan.nextLine());
			setEmail(scan.nextLine());
			
			setPassword(scan.nextLine());
			setBirthdate(scan.nextLine());
			setPhoneNum(scan.nextLine());
			setSalary(scan.nextDouble());
			
			setAccessLvl(scan.nextInt());
			setPermissions(this.accessLvl);
		} catch(InputMismatchException ex) {
			throw new WrongFormatException("input", "character input for text fields, numeric input for price fields");
		} finally {
			if(!newUserFile.getName().contains("base"))
				newUserFile.delete();
		}
	}
	
	//Class methods
	public static void setList() {
		if(new File(FILE_NAME).length() == 0) {
			try {
				employees = FXCollections.observableArrayList();
				add(new Employee(new File("baseAdmin.txt")));
				add(new Employee(new File("baseManager.txt")));
				add(new Employee(new File("baseLibrarian.txt")));
				
				ListIO.writeToFile(FILE_NAME, new ArrayList<>(getAll()));
			} catch(Exception ex) {
				System.out.println(ex);
			}
		} else {
			ArrayList<Employee> currentList = ListIO.readFromFile(FILE_NAME);
			employees = currentList == null ? FXCollections.observableArrayList() : FXCollections.observableArrayList(currentList);
		}
	}
	
	public static ObservableList<Employee> getAll() {
		return new ReadOnlyListWrapper<>(employees);
	}
	
	public static Employee get(int index) throws IndexOutOfBoundsException {
		return employees.get(index);
	}
	
	//Adds employees on alphabetical order based on username
	public static boolean add(Employee employee) throws ExistingObjectException {
		if(employees.contains(employee))
			throw new ExistingObjectException("username, email");
		
		if(employee != null) {
			int index = employees.isEmpty() ? 0 : findAlphabeticIndex(employee);
			employees.add(index, employee);
			return true;
		}
		
		return false;
	}
		
	public static void remove(int index) throws IndexOutOfBoundsException {
		employees.remove(index);
	}
		
	public static void reposition(int currentIndex, Employee employee) {
		employees.remove(currentIndex);
		employees.add(findAlphabeticIndex(employee), employee);
	}
	
	public static int findIndex(String username) throws EmptyInputException, WrongFormatException {
		Employee temp = new Employee();
		temp.setUsername(username);
		return employees.indexOf(temp);
	}
	
	private static int findAlphabeticIndex(Employee employee) {
		int index = Collections.binarySearch(employees, employee);
			
		if(index < 0)
			index = (index * -1) - 1;
		
		return index;
	}
	
	//Getters & setters
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) throws EmptyInputException, WrongFormatException {
		if(username.isBlank())
			throw new EmptyInputException("username");
		
		if(username.length() < 5)
			throw new WrongFormatException("username", "5 characters or more");
		
		this.username = username;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) throws EmptyInputException, WrongFormatException {
		if(fullName.isBlank())
			throw new EmptyInputException("name");
		
		if(fullName.matches("[a-zA-Z]{2,}\\s[a-zA-Z]+"))
			this.fullName = fullName;
		else
			throw new WrongFormatException("full name", "(first name) (last name)");
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) throws EmptyInputException, WrongFormatException {
		if(email.isBlank())
			throw new EmptyInputException("email");
		
		if(email.matches("\\w{2,}@gmail\\.com"))
			this.email = email;
		else
			throw new WrongFormatException("email", "(2 characters)@gmail.com");
	}
	
	public boolean isCorrectPassword(String password) {
		return this.password.equals(password);
	}
	
	public void setPassword(String password) throws EmptyInputException, WrongFormatException {
		if(password.isBlank())
			throw new EmptyInputException("password");
		
		if(password.matches("\\w+.{6,}\\w+"))
			this.password = password;
		else
			throw new WrongFormatException("password", "at least 8 characters (digits, lowercase letters, uppercase letters)");
	}
	
	public LocalDate getBirthdate() {
		return birthdate.getDate();
	}
	
	public void setBirthdate(String birthdate) throws EmptyInputException, WrongFormatException {
		if(birthdate.isBlank())
			throw new EmptyInputException("birthdate");
		
		this.birthdate = new CustomDate(birthdate);
	}
	
	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) throws EmptyInputException, WrongFormatException {
		if(phoneNum.isBlank())
			throw new EmptyInputException("phone number");
		
		if(phoneNum.matches("06[789]\\s\\d{3}\\s\\d{3}"))
			this.phoneNum = phoneNum;
		else
			throw new WrongFormatException("phone number", "06X XXX XXX");
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
	
	public void setPermissions(boolean[] permissionStatus) {
		int i = 0;
		
		for(Role permission: this.permissions.keySet())
			this.permissions.put(permission, permissionStatus[i++]);
	}
	
	public HashMap<Role, Boolean> getPermissions() {
		return permissions;
	}
	
	@Override
	public String toString() {
		String role;
		
		switch(accessLvl) {
			case 1: role = "Librarian"; break;
			case 2: role = "Manager"; break;
			default: role = "Administrator";
		}
		
		return this.username + " (" + this.fullName + ") (" + role + ")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Employee) {
			Employee temp = (Employee) obj;
			return temp.getUsername().equals(username) || temp.getEmail().equals(email);
		}
		
		return false;
	}

	@Override
	public int compareTo(Employee obj) {
		return getUsername().compareToIgnoreCase(obj.getUsername());
	}
}
