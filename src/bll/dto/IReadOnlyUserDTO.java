package bll.dto;

import java.time.LocalDate;

public interface IReadOnlyUserDTO {
	int getId();
	String getUsername();
	String getFullName();
	String getEmail();
	String getPassword();
	String getHashedPassword();
	String getPhoneNum();
	LocalDate getBirthdate();
}
