package Bookstore.Bookstore.bll.dto;

import java.time.LocalDate;

public class UserDTO implements IReadOnlyUserDTO {
	private int id;
	private String username, fullName, email, password, hashedPassword, phoneNum;
	private LocalDate birthdate;
	
	public UserDTO() {
		setId(0);
	}
	
	private UserDTO(String username, String fullName, String email, String phoneNum, LocalDate birthdate) {
		setUsername(username);
		setFullName(fullName);
		setEmail(email);
		setPhoneNum(phoneNum);
		setBirthdate(birthdate);
	}
	
	public UserDTO(String username, String fullName, String email, String password, String phoneNum, LocalDate birthdate) {
		this(username, fullName, email, phoneNum, birthdate);
		setId(0);
		setPassword(password);
		
	}
	
	public UserDTO(int id, String username, String fullName, String email, String hashedPassword, String phoneNum, LocalDate birthdate) {
		this(username, fullName, email, phoneNum, birthdate);
		setId(id);
		setHashedPassword(hashedPassword);
	}
	
	public UserDTO(UserDTO model) {
		this(model.getId(), model.getUsername(), model.getFullName(), model.getEmail(), model.getHashedPassword(), model.getPhoneNum(), model.getBirthdate());
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	@Override
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	@Override
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String getPassword() {
		return password;
	}	
	@Override
	public String getHashedPassword() {
		return hashedPassword;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	@Override
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	@Override
	public LocalDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(LocalDate birthdate) {
		this.birthdate = birthdate;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof UserDTO))
			return false;
		
		UserDTO model = (UserDTO) o;
		
		return getId() == model.getId() && getUsername().equals(model.getUsername()) &&
				getFullName().equals(model.getFullName()) && getEmail().equals(model.getEmail()) &&
				(getHashedPassword().equals(model.getHashedPassword()) || getPassword().equals(model.getPassword()));
	}
	
	@Override
	public int hashCode() {
		return getId() + getEmail().hashCode() + getUsername().hashCode();
	}
}
