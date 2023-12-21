package Bookstore.Bookstore.dal.models.utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import Bookstore.Bookstore.commons.exceptions.EmptyInputException;
import Bookstore.Bookstore.commons.exceptions.WrongFormatException;

public class CustomDate implements Serializable {
	private static final long serialVersionUID = -1352994897167226152L;
	
	public static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	private LocalDate date;
	
	// Creates instance of current date
	public CustomDate() {
		this.date = LocalDate.now();
	}
	
	public CustomDate(String date) throws DateTimeParseException, WrongFormatException {
		setDate(date);
	}
	
	public CustomDate(LocalDate date) throws EmptyInputException {
		setDate(date);
	}
	
	public LocalDate getDate() {
		return date;
	}
	
	public void setDate(LocalDate date) throws EmptyInputException {
		if(date == null)
			throw new EmptyInputException("date");
		
		this.date = date;
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
	
	public static boolean isInDate(LocalDate startDate, LocalDate endDate, LocalDate date) {
		if(startDate.isAfter(endDate))
			return false;
		
		if((date.isAfter(startDate) || date.isEqual(startDate)) && (date.isBefore(endDate) || date.isEqual(endDate)))
			return true;
		
		return false;
	}
	
	public static String format(LocalDate date) {
		return date.format(dateFormat);
	}
	
	@Override
	public String toString() {
		return date.format(dateFormat);
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof CustomDate))
			return false;
		
		return ((CustomDate) o).getDate().equals(getDate());
	}
	
	@Override
	public int hashCode() {
		return getDate().hashCode();
	}
}
