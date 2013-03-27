package mapDrawer.exceptions;

@SuppressWarnings("serial")
public class AreaIsNotWithinDenmarkException extends Exception {
		public AreaIsNotWithinDenmarkException(String message) {
			super(message);
		}
		public AreaIsNotWithinDenmarkException(String message, Throwable throwable) {
			super(message, throwable);
		}

}
