package mapCreationAndFunctions.exceptions;

/**
 * This exception is thrown when the program tries to create a new AreaToDraw, whose proportions does not match those of the entire map of Denmark
 * Helps preventing distortion of the drawn map.
 */
@SuppressWarnings("serial")
public class InvalidAreaProportionsException extends Exception {
	public InvalidAreaProportionsException(String message) {
		super(message);
	}
	public InvalidAreaProportionsException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
