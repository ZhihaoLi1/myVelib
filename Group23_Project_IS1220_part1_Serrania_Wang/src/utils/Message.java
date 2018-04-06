package utils;

public class Message {
	private final Severity severity;
	private final String message;
	
	public Message(String severity, String message) {
		this.severity = Severity.valueOf(severity);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Severity getSeverity() {
		return severity;
	}
}
