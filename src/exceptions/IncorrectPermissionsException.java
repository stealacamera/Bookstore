package exceptions;

public class IncorrectPermissionsException extends Exception {
	private static final long serialVersionUID = -3561224362115372934L;

	public IncorrectPermissionsException(String message) {
		super(message);
	}
}
