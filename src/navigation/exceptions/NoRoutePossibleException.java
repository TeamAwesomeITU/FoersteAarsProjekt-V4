package navigation.exceptions;

/**
 * This exception is thrown when it is not possible to find a route
 *
 */
public class NoRoutePossibleException extends Exception {
	public NoRoutePossibleException(String message) {
		super(message);
	}
	public NoRoutePossibleException(String message, Throwable throwable) {
		super(message, throwable);
	}


}
