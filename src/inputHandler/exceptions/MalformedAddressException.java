package inputHandler.exceptions;

/**
 * This exception is thrown when a user attempts enters bad input
 */
@SuppressWarnings("serial")
public class MalformedAddressException extends Exception {
	public MalformedAddressException(String message) {
		super(message);
	}
	public MalformedAddressException(String message, Throwable throwable) {
		super(message, throwable);
	}
}