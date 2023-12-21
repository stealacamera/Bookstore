package Bookstore.Bookstore.commons.exceptions;

public class WrongFormatException extends Exception {
	private static final long serialVersionUID = -4473612480226944299L;

	public WrongFormatException(String inputFieldName, String correctFormat) {
		super("Incorrect " + inputFieldName + " format: Correct format is " + correctFormat);
	}
}
