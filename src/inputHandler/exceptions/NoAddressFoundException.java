package inputHandler.exceptions;

/**
 * This exception is thrown when no addresses are found
 */
@SuppressWarnings("serial")
public class NoAddressFoundException extends Exception {
	public NoAddressFoundException(String message) {
		super(message);
	}
	public NoAddressFoundException(String message, Throwable throwable) {
		super(message, throwable);
	}
}