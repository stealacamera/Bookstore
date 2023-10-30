package models.helpers;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import exceptions.WrongFormatException;

public class CustomDate implements Serializable {
	private static final long serialVersionUID = -1352994897167226152L;
	public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	private LocalDate date;
	
	public CustomDate() {
		this.date = LocalDate.now();
	}
	
	public CustomDate(String date) throws DateTimeParseException, WrongFormatException {
		setDate(date);
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(String date) throws DateTimeParseException, WrongFormatException {
		if(date.matches("\\d{2}/\\d{2}/\\d{4}")) {
			try {
				this.date = LocalDate.parse(date, dateFormat);
			} catch(DateTimeParseException ex) {
				throw ex;
			}
			
		} else
			throw new WrongFormatException("date", "DD/MM/YYYY (eg.: 21/03/1998)");
	}
	
	public static boolean inDate(LocalDate startDate, LocalDate endDate, LocalDate date) {
		if(date.compareTo(startDate) >= 0) {
			if(endDate != null)
				return date.compareTo(endDate) <= 0 ? true : false;
				
			return true;
		}
		
		return false;
	}
	
	public static String format(LocalDate date) {
		return date.format(dateFormat);
	}
	
	@Override
	public String toString() {
		return date.format(dateFormat);
	}
}
