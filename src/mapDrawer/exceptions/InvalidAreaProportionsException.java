package mapDrawer.exceptions;

@SuppressWarnings("serial")
public class InvalidAreaProportionsException extends Exception {
	public InvalidAreaProportionsException(String message) {
		super(message);
	}
	public InvalidAreaProportionsException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
