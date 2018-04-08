package clui;

public class IncorrectArgumentException extends Exception {

	private String message;
	
	public IncorrectArgumentException(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
		
	}
}
