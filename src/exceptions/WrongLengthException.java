package exceptions;

public class WrongLengthException extends Exception {
	private static final long serialVersionUID = 3044653159427324031L;

	public WrongLengthException(String fieldName, int maxLength) {
		super("Incorrect length: " + fieldName + " should be " + maxLength + " characters or less");
	}
}
