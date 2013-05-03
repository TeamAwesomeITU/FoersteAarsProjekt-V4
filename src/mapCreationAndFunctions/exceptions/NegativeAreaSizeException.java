package mapCreationAndFunctions.exceptions;

/**
 * This exception is thrown when the program attempts to create an AreaToDraw with a negative side length or height.
 */
@SuppressWarnings("serial")
public class NegativeAreaSizeException extends Exception {
		public NegativeAreaSizeException(String message) {
			super(message);
		}
		public NegativeAreaSizeException(String message, Throwable throwable) {
			super(message, throwable);
		}

}
