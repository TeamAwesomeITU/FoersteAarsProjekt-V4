package mapDrawer.exceptions;

@SuppressWarnings("serial")
public class AreaNegativeSizeException extends Exception {
		public AreaNegativeSizeException(String message) {
			super(message);
		}
		public AreaNegativeSizeException(String message, Throwable throwable) {
			super(message, throwable);
		}

}
