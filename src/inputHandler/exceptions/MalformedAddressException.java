package inputHandler.exceptions;

/**
 * This exception is thrown when a user enters something, which cannot be interpreted as an address.
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