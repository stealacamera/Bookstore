package bll.dto;

import models.utilities.CustomDate;

public class UserDTO {
	private int id;
	private String username, fullName, email, password, hashedPassword, phoneNum;
	private CustomDate birthdate;
	
	public UserDTO() {
		setId(0);
	}
	
	public UserDTO(String username, String fullName, String email, String password, String phoneNum, CustomDate birthdate) {
		setId(0);
		setUsername(username);
		setFullName(fullName);
		setEmail(email);
		setPassword(password);
		setPhoneNum(phoneNum);
		setBirthdate(birthdate);
	}
	
	public UserDTO(int id, String username, String fullName, String email, String hashedPassword, String phoneNum, CustomDate birthdate) {
		setId(id);
		setUsername(username);
		setFullName(fullName);
		setEmail(email);
		setHashedPassword(hashedPassword);
		setPhoneNum(phoneNum);
		setBirthdate(birthdate);
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}	
	public String getHashedPassword() {
		return hashedPassword;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}
	public String getPhoneNum() {
		return phoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}
	public CustomDate getBirthdate() {
		return birthdate;
	}
	public void setBirthdate(CustomDate birthdate) {
		this.birthdate = birthdate;
	}
	
	
}
