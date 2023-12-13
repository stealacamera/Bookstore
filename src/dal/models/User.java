package dal.models;

import java.io.Serializable;

import exceptions.EmptyInputException;
import exceptions.WrongFormatException;
import dal.models.utilities.CustomDate;
import utils.Identity;

public class User implements Serializable {
	private static final long serialVersionUID = 8719237595683164604L;
	private static volatile int idOrder = 1;
	
	private final int id;
	private String username, fullName, email, hashedPassword, phoneNum;
	private CustomDate birthdate;
	
	private User(int id, String username, String fullName, String email, String phoneNum, CustomDate birthdate) throws EmptyInputException, WrongFormatException {
		this.id = id;
		setUsername(username);
		setFullName(fullName);
		setEmail(email);
		setPhoneNum(phoneNum);
		setBirthdate(birthdate);
	}
	
	public User(String username, String fullName, String email, String password, String phoneNum, CustomDate birthdate) throws EmptyInputException, WrongFormatException {
		this(setIncrementalId(), username, fullName, email, phoneNum, birthdate);
		setPassword(password);
	}
	
	// If id is passed, we are cloning
	public User(int id, String username, String fullName, String email, String hashedPassword, String phoneNum, CustomDate birthdate) throws EmptyInputException, WrongFormatException {
		this(id, username, fullName, email, phoneNum, birthdate);
		setHashedPassword(hashedPassword);
	}
	
	public User(User model) throws EmptyInputException, WrongFormatException {
		this(model.getId(), model.getUsername(), model.getFullName(), model.getEmail(), model.getHashedPassword(), model.getPhoneNum(), model.getBirthdate());
	}
	
	private static int setIncrementalId() { return User.idOrder++; }
	
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
		else if(username.length() < 5 || username.length() > 25)
			throw new WrongFormatException("username", "5-25 characters");
		
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) throws EmptyInputException, WrongFormatException {
		if(fullName == null || fullName.isBlank())
			throw new EmptyInputException("name");
		else if(!fullName.matches("[a-zA-Z]+\\s[a-zA-Z]+"))
			throw new WrongFormatException("full name", "(first name) (last name)");

		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) throws EmptyInputException, WrongFormatException {
		if(email == null || email.isBlank())
			throw new EmptyInputException("email");
		else if(!email.matches("\\w{2,}@gmail\\.com"))
			throw new WrongFormatException("email", "(2 characters)@gmail.com");
		
		this.email = email;	
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
		else if(!password.matches("\\w+.{6,}\\w+"))
			throw new WrongFormatException("password", "at least 8 characters (digits, lowercase letters, uppercase letters)");

		this.hashedPassword = Identity.hashPassword(password);
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) throws EmptyInputException, WrongFormatException {
		if(phoneNum == null || phoneNum.isBlank())
			throw new EmptyInputException("phone number");
		else if(!phoneNum.matches("06[789]\\s\\d{3}\\s\\d{4}"))
			throw new WrongFormatException("phone number", "06X XXX XXXX");

		this.phoneNum = phoneNum;
	}

	public CustomDate getBirthdate() {
		try {
			return new CustomDate(birthdate.getDate());
		} catch (EmptyInputException e) {
			// Error won't be thrown because date is always instantiated
			e.printStackTrace();
			return null;
		}
	}

	public void setBirthdate(String birthdate) throws EmptyInputException, WrongFormatException {
		if(birthdate == null || birthdate.isBlank())
			throw new EmptyInputException("birthdate");
		
		this.birthdate = new CustomDate(birthdate);
	}
	
	public void setBirthdate(CustomDate birthdate) throws EmptyInputException {
		if(birthdate == null)
			throw new EmptyInputException("birthdate");
		
		this.birthdate = new CustomDate(birthdate.getDate());
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User) {
			User temp = (User) obj;
			return temp.getUsername().equals(username) || temp.getEmail().equals(email);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return email.hashCode();
	}
}
