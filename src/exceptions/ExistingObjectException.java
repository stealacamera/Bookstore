package exceptions;

public class ExistingObjectException extends Exception {
	private static final long serialVersionUID = -8968158098314658079L;
	
	public ExistingObjectException() {
		this(null);
	}
	
	public ExistingObjectException(String elements) {
		super("Element with these details" + (elements == null ? " " : " (" + elements + ") ") + "already exists.");
	}
}
