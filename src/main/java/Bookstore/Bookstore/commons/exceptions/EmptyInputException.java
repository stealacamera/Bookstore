package Bookstore.Bookstore.commons.exceptions;

public class EmptyInputException extends Exception {
	private static final long serialVersionUID = -4626767327728000241L;

	public EmptyInputException(String inputFieldName) {
		super("Input fields cannot be empty: Please enter a value for " + inputFieldName);
	}
}
