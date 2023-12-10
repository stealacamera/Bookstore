package models;

import java.io.Serializable;

import exceptions.EmptyInputException;
import exceptions.WrongFormatException;
import models.utilities.CustomDate;
import utils.Identity;

public class User implements Serializable {
	private static final long serialVersionUID = 8719237595683164604L;
	private static int idOrder = 1;
	
	private final int id;
	private String username, fullName, email, hashedPassword, phoneNum;
	private CustomDate birthdate;
		
	// If id is passed, we are cloning
	public User(int id, String username, String fullName, String email, String hashedPassword, String phoneNum, CustomDate birthdate) throws EmptyInputException, WrongFormatException {
		this.id = id;
		setUsername(username);
		setFullName(fullName);
		setEmail(email);
		setPhoneNum(phoneNum);
		setBirthdate(birthdate);
		setHashedPassword(hashedPassword);
	}
	
	public User(String username, String fullName, String email, String password, String phoneNum, CustomDate birthdate) throws EmptyInputException, WrongFormatException {
		id = idOrder++;
		setUsername(username);
		setFullName(fullName);
		setEmail(email);
		setPhoneNum(phoneNum);
		setBirthdate(birthdate);
		setPassword(password);
	}
	
	public User(User model) throws EmptyInputException, WrongFormatException {
		this(model.getId(), model.getUsername(), model.getFullName(), model.getEmail(), model.getHashedPassword(), model.getPhoneNum(), model.getBirthdate());
	}
	
	public boolean isCorrectPassword(String password) {
		return this.hashedPassword.equals(Identity.hashPassword(password));
	}
		
	public int getId() {
		return id;
	}
 	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) throws EmptyInputException, WrongFormatException {
		if(username == null || username.isBlank())
			throw new EmptyInputException("username");
		
		if(username.length() < 5)
			throw new WrongFormatException("username", "5 characters or more");
		
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) throws EmptyInputException, WrongFormatException {
		if(fullName == null || fullName.isBlank())
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
		if(email == null || email.isBlank())
			throw new EmptyInputException("email");
		
		if(email.matches("\\w{2,}@gmail\\.com"))
			this.email = email;
		else
			throw new WrongFormatException("email", "(2 characters)@gmail.com");
	}

	public String getHashedPassword() {
		return hashedPassword;
	}

	public void setHashedPassword(String hashedPassword) throws EmptyInputException {
		if(hashedPassword == null || hashedPassword.isBlank())
			throw new EmptyInputException("hashed password");
		
		this.hashedPassword = hashedPassword;
	}
	
	public void setPassword(String password) throws EmptyInputException, WrongFormatException {
		if(password == null || password.isBlank())
			throw new EmptyInputException("password");
		
		if(password.matches("\\w+.{6,}\\w+"))
			this.hashedPassword = Identity.hashPassword(password);
		else
			throw new WrongFormatException("password", "at least 8 characters (digits, lowercase letters, uppercase letters)");
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) throws EmptyInputException, WrongFormatException {
		if(phoneNum == null || phoneNum.isBlank())
			throw new EmptyInputException("phone number");
		
		if(phoneNum.matches("06[789]\\s\\d{3}\\s\\d{3}"))
			this.phoneNum = phoneNum;
		else
			throw new WrongFormatException("phone number", "06X XXX XXX");
	}

	public CustomDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(String birthdate) throws EmptyInputException, WrongFormatException {
		if(birthdate == null || birthdate.isBlank())
			throw new EmptyInputException("birthdate");
		
		this.birthdate = new CustomDate(birthdate);
	}
	
	public void setBirthdate(CustomDate birthdate) throws EmptyInputException {
		if(birthdate == null)
			throw new EmptyInputException("birthdate");
		
		this.birthdate = birthdate;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User) {
			User temp = (User) obj;
			return temp.getUsername().equals(username) || temp.getEmail().equals(email);
		}
		
		return false;
	}
	
	public int hashCode() {
		return email.hashCode();
	}
}
