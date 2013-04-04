package inputHandler.exceptions;

/**
 * This exception is thrown when a user attempts enters bad input
 */
@SuppressWarnings("serial")
public class MalformedAdressException extends Exception {
	public MalformedAdressException(String message) {
		super(message);
	}
	public MalformedAdressException(String message, Throwable throwable) {
		super(message, throwable);
	}
}