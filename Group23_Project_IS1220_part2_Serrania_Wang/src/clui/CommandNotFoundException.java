package clui;

public class CommandNotFoundException extends Exception {
	
	CommandNotFoundException() {
	}
	

	@Override
	public String getMessage() {
		return "The command you entered is not found.";
	}
}
