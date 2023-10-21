package exceptions;

public class NonPositiveInputException extends Exception{
	private static final long serialVersionUID = 4717646050785728131L;

	public NonPositiveInputException(String inputFieldName) {
		super("Incorrect " + inputFieldName + ": Please enter a positive number for the field");
	}
}
