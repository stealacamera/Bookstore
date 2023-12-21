package Bookstore.Bookstore.commons.exceptions;

public class NonSequentialDatesException extends Exception {
	private static final long serialVersionUID = -2604009172911201016L;

	public NonSequentialDatesException() {
		super("Starting date should follow ending date");
	}
}
