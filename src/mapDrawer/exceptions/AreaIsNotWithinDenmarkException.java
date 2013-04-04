package mapDrawer.exceptions;

/**
 * This exception is thrown when the programs attempts to create an AreaToDraw, which is not within the minimum and maximum coordinates of the map of Denmark, as given as static fields in AreaToDraw.
 */
@SuppressWarnings("serial")
public class AreaIsNotWithinDenmarkException extends Exception {
		public AreaIsNotWithinDenmarkException(String message) {
			super(message);
		}
		public AreaIsNotWithinDenmarkException(String message, Throwable throwable) {
			super(message, throwable);
		}

}
