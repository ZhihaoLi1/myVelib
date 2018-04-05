package clui;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Scanner;

import core.Network;
import utils.DateParser;

public class CLUIThread extends Thread {
	private HashMap<String, Network> networks = new HashMap<String, Network>();
	
	private String setupUsage = "======== setup usage ===== \n" +
			"option 1: \n" +
			"setup <myvelibnetworkname> \n" +
			"This creates a myVelib network with given name and\n" + 
			"consisting of 10 stations each of which has 10 parking slots and such that stations\n" + 
			"are arranged on a square grid whose of side 4km and initially populated with a 75%\n" + 
			"bikes randomly distributed over the 10 stations" +
			"option 2: \n" + 
			"setup <myvelibnetworkname> <dateTime> <nstations> <nslots> <sidearea> <nbikes>" +
			"This creates a myVelib network with given name and\n" + 
			"consisting of <nstations> stations each of which has <nslots> parking slots and such that stations\n" + 
			"are arranged on a square grid whose of side <sidearea>km and initially populated with a <nbikes> \n" + 
			"bikes randomly distributed over the stations";
	
	public Boolean hasNetwork(String name) {
		if (networks.get(name) == null) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * @param args <name> <dateTime> or  <name> <dateTime> <nstations> <nslots> <sidearea> <nbikes>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String setup(String[] args) throws IncorrectArgumentException {
		if (args.length != 2 && args.length != 6) throw new IncorrectArgumentException("Number of arguments is incorrect.");
		if (args.length == 2) {
			String name = args[0];
			try {
				LocalDateTime creationDate = DateParser.parse(args[1]);
				// verify name is not already taken 
				if (hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " already exists.");
				Network n = new Network(name, 10, 10, 4, 0.75, 0.5, 0.5, creationDate);
				networks.put(name, n);
			} catch(DateTimeParseException e) {
				throw new IncorrectArgumentException("DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
			}
			
		} else {
			// <nstations> <nslots> <sidearea> <nbikes>
			String name = args[0];
			if (hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " already exists.");
			LocalDateTime commandDate = DateParser.parse(args[1]);
			int nstations = Integer.parseInt(args[2]);
			int nslots = Integer.parseInt(args[3]);
			double sidearea = Double.parseDouble(args[4]);
			int nbikes = Integer.parseInt(args[5]);
			// FIXME ? not percentage but number ? 
			double percentageOfBikes = nbikes / (nslots*nstations);
			if (percentageOfBikes > 1) throw new IncorrectArgumentException("Total number of bikes exceeds the total number of slots");
			// By default 50% of plus stations, 50% of elec bikes
			Network n = new Network(name , nstations, nslots, sidearea, percentageOfBikes, 0.5, 0.5, commandDate);
			networks.put(name, n);
		}
		
		return "Network " + args[0] + " has been sucessfully created."; 
	}
		
	/**
	 * 
	 * @param args <name> <username> <cardType>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String addUser(String[] args) throws IncorrectArgumentException {
		if (args.length == 3) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			return networks.get(name).addUser(args[1], args[2]);
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <stationId>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String offline(String[] args) throws IncorrectArgumentException {
		if (args.length == 2) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			try {
				return networks.get(name).setOffline(Integer.parseInt(args[1]));
			} catch (NumberFormatException e) {
				throw new IncorrectArgumentException("StationID needs to be an integer.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <stationId>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String online(String[] args) throws IncorrectArgumentException {
		if (args.length == 2) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			try {
				return networks.get(name).setOnline(Integer.parseInt(args[1]));
			} catch (NumberFormatException e) {
				throw new IncorrectArgumentException("StationID needs to be an integer.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <DateTime> <userId> <stationId> <bikeType>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String rentBike(String[] args) throws IncorrectArgumentException {
		if (args.length == 5) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			try {
				LocalDateTime rentalDate= DateParser.parse(args[1]);
				int userId = Integer.parseInt(args[2]);
				int stationId = Integer.parseInt(args[3]);
				String bikeType = args[4];
				
				return networks.get(name).rentBike(userId, stationId, bikeType, rentalDate);
			} catch (NumberFormatException e) {
				throw new IncorrectArgumentException("StationID and UserId need to be integers.");
			} catch(DateTimeParseException e) {
				throw new IncorrectArgumentException("DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <DateTime> <userId> <stationId>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String returnBike(String[] args) throws IncorrectArgumentException {
		if (args.length == 4) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			try {
				LocalDateTime returnDate= DateParser.parse(args[1]);
				int userId = Integer.parseInt(args[2]);
				int stationId = Integer.parseInt(args[3]);
				return networks.get(name).returnBike(userId, stationId, returnDate);
			} catch (NumberFormatException e) {
				throw new IncorrectArgumentException("StationID and UserId need to be integers.");
			} catch(DateTimeParseException e) {
				throw new IncorrectArgumentException("DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <stationId>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String displayStation(String[] args) throws IncorrectArgumentException {
		if (args.length == 2) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			try {
				int stationId = Integer.parseInt(args[1]);
				return networks.get(name).displayStation(stationId);
			} catch (NumberFormatException e) {
				throw new IncorrectArgumentException("StationID needs to be an integer.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <userId>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String displayUser(String[] args) throws IncorrectArgumentException {
		if (args.length == 2) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			try {
				int userId = Integer.parseInt(args[1]);
				return networks.get(name).displayUser(userId);
			} catch (NumberFormatException e) {
				throw new IncorrectArgumentException("UserID needs to be an integer.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name> <sortPolicy>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String sortStation(String[] args) throws IncorrectArgumentException {
		if (args.length == 2) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			return networks.get(name).sortStation(args[1]);
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * 
	 * @param args <name>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String display(String[] args) throws IncorrectArgumentException {
		if (args.length == 1) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + "does not exist.");
			return networks.get(name).toString();
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}	
	
	
	public String parseUserInput(String userInput) {
		String message="";
		Commands command; 
		
		String[] inputs = userInput.split(" ");

		if (inputs.length < 2) {
			return "All commands need to be followed by arguments";
		}
		
		String commandRaw = inputs[0];
		String[] arguments = java.util.Arrays.copyOfRange(inputs, 1, inputs.length);
	    try {
	    		command = Commands.valueOf(commandRaw);
	    } catch (IllegalArgumentException ex) {  
	        	return "Command that is entered does not exist.";
	    }
	    	    
	    switch (command) {
	    case setup:
	    		try {
	    			message = setup(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage(); // TODO write up setup usage
	    		}
	    		break;
	    		
	    case addUser:
	    		try {
	    			message = addUser(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage(); // TODO write up addUser usage
	    		}
	    		break;
	    
	    case offline:
	    		try {
	    			message = offline(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		} 
	    		break;
	    	
	    	
	    case online:
	    		try {
	    			message = online(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;
	    	
	    case rentBike:
	    		try {
	    			message = rentBike(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;
	    	
	    case returnBike:
	    		try {
	    			message = returnBike(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;

	    case displayStation:
	    		try {
	    			message = displayStation(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;

	    case displayUser:
	    		try {
	    			message = displayUser(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;

	    case sortStation:
	    		try {
	    			message = sortStation(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;

	    case display:
	    		try {
	    			message = display(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage();
	    		}
	    		break;
	    }
	    
	    return message;
	    
	}
	
	@Override
	public void run() {
		System.out.println("Welcome to MyVelib. Please enter your command");
		String userInput = "";
		while(!userInput.equals("stop")) {
			Scanner reader = new Scanner(System.in);  // Reading from System.in
			userInput = reader.nextLine(); // Scans for user input
			System.out.println(parseUserInput(userInput));
		}
	}
}
