package clui;

public class NetworkAlreadyExistsException extends Exception{
	private String name;
	NetworkAlreadyExistsException(String name){
		this.name = name;
	}
	
	@Override
	public String getMessage() {
		return "Network " + name + " already exists";
	}
	
}
