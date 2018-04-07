package clui;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import core.Network;
import utils.DateParser;

public class CLUIThread extends Thread implements Observer {
	private HashMap<String, Network> networks = new HashMap<String, Network>();
	
	private String helpMessage = "\n =========== Help =========== \n"
			+ "setup <network name> <dateTime> \n"
			+ "setup <myvelibnetworkname> <dateTime> <nstations> <nslots> <sidearea> <nbikes> \n"
			+ "addUser <network name> <user name> <typeOfCard> \n"
			+ "offline <network name> <stationId> \n"
			+ "online <network name> <stationId> \n"
			+ "rentBike <network name> <timeOfRental> <userId> <stationId> <bikeType> \n"
			+ "returnBike <network name> <timeOfRental> <userId> <stationId> \n"
			+ "displayStation <network name> <stationId> \n"
			+ "displayUser <network name> <userId> \n"
			+ "sortStation <network name> <sortPolicy> \n"
			+ "display <network name> \n"
			+ "planRide <network name> <sourceX> <sourceY> <destinationX> <destinationY> <userId> <policy> <bikeType>";
	
	private String setupUsage = "\n =========== setup usage =========== \n" +
			"option 1: \n" +
			"setup <network name> <dateTime> \n"+ 
			"Example: setup myVelib 01/01/2000T00:00:00 \n" +
			"This creates a myVelib network with given name and \n" + 
			"consisting of 10 stations each of which has \n" +
			"10 parking slots and such that stations are arranged \n" +
			"on a square grid whose of side 4km and initially populated \n" +
			"with a 75% bikes randomly distributed over the 10 stations \n" +
			"option 2: \n" + 
			"setup <myvelibnetworkname> <dateTime> <nstations> <nslots> <sidearea> <nbikes> \n" +
			"Example: setup myVelib 01/01/2000T00:00:00 10 10 4 75 \n" +
			"This creates a myVelib network with given name and consisting of <nstations> stations \n" +
			"each of which has <nslots> parking slots and such that stations \n" + 
			"are arranged on a square grid whose of side <sidearea>km and initially populated \n" +
			"with a <nbikes> bikes randomly distributed over the stations \n";
	
	private String addUserUsage = "\n =========== addUser usage =========== \n"
			+ "addUser <network name> <user name> <typeOfCard> \n"
			+ "Example: addUser myVelib John NO_CARD \n"
			+ "Adds John to the myVelib Network and he does not have a card. \n"
			+ "The different card types are: NO_CARD, VLIBRE_CARD, VMAX_CARD. \n";
	
	private String offlineUsage = "\n =========== offline usage =========== \n"
			+ "offline <network name> <stationId> \n"
			+ "Example: offline myVelib 1 \n"
			+ "Set a station to offline \n";

	private String onlineUsage = "\n =========== online usage =========== \n"
			+ "online <network name> <stationId> \n"
			+ "Example: online myVelib 1 \n"
			+ "Set a station to online \n";

	private String rentBikeUsage = "\n =========== rentBike usage =========== \n"
			+ "rentBike <network name> <timeOfRental> <userId> <stationId> <bikeType> \n"
			+ "Example: rentBike myVelib 01/01/2000T00:00:00 1 1 ELEC \n"
			+ "Rent a bike from a station in given network for user <userId>. \n"
			+ "Types of Bike: ELEC, MECH \n";
	
	private String returnBikeUsage = "\n =========== returnBike usage =========== \n"
			+ "returnBike <network name> <timeOfRental> <userId> <stationId> \n"
			+ "Example: returnBike myVelib 02/01/2000T00:00:00 1 1 \n"
			+ "Return a bike to a station in given network for user <userId>. \n";
	
	private String displayStationUsage = "\n =========== displayStation usage =========== \n"
			+ "displayStation <network name> <stationId> \n"
			+ "Displays the statistics of a station \n";
	
	private String displayUserUsage = "\n =========== displayUser usage =========== \n"
			+ "displayUser <network name> <userId> \n"
			+ "Displays the statistics of a user \n";
	
	private String sortStationUsage = "\n =========== displayUser usage =========== \n"
			+ "sortStation <network name> <sortPolicy> \n"
			+ "Example: sortStation myVelib LEAST_OCCUPIED \n"
			+ "Types of policies: LEAST_OCCUPIED, MOST_USED";
	
	private String displayUsage = "\n =========== display usage =========== \n"
			+ "display <network name> \n"
			+ "Displays the details of the network \n";
	
	private String runtestUsage = "\n =========== display usage =========== \n"
			+ "runtest <scenario filepath> \n"
			+ "Runs the test scenario. \n";

	private String deleteNetworkUsage = "\n =========== deleteNetwork usage =========== \n"
			+ "deleteNetwork <network name> \n"
			+ "Delete network from CLUI \n";

	private String planRideUsage = "\n =========== planRide usage =========== \n"
			+ "planRide <network name> <sourceX> <sourceY> <destinationX> <destinationY> <userId> <policy> <bikeType>\n"
			+ "planRide myVelib 1.0 1.0 3.0 3.0 1 FASTEST MECH \n"
			+ "The types of ridePlan policies are: FASTEST, SHORTEST, PREFER_PLUS, AVOID_PLUS, PRESERVE_UNIFORMITY \n"
			+ "This command sets a ride plan for the user and he/she is notified when the destination stations goes offline or become unavailable. \n";
	
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
		Network.reset();
		if (args.length == 2) {
			String name = args[0];
			try {
				LocalDateTime creationDate = DateParser.parse(args[1]);
				// verify name is not already taken 
				if (hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " already exists.");
				Network n = new Network(name, 10, 10, 4, 0.75, 0.5, 0.5, creationDate);
				// add this clui to the observers of network 
				n.addObserver(this);
				networks.put(name, n);
			} catch(DateTimeParseException e) {
				throw new IncorrectArgumentException("DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
			}
			
		} else {
			// <nstations> <nslots> <sidearea> <nbikes>
			String name = args[0];
			if (hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " already exists.");
			LocalDateTime creationDate = DateParser.parse(args[1]);
			int nstations = Integer.parseInt(args[2]);
			int nslots = Integer.parseInt(args[3]);
			double sidearea = Double.parseDouble(args[4]);
			int nbikes = Integer.parseInt(args[5]);
			// FIXME ? not percentage but number ? 
			double percentageOfBikes = (float) nbikes / (nslots*nstations);
			if (percentageOfBikes > 1) throw new IncorrectArgumentException("Total number of bikes exceeds the total number of slots");
			// By default 50% of plus stations, 50% of elec bikes
			System.out.println("TESTTESTSET: " + percentageOfBikes);
			Network n = new Network(name , nstations, nslots, sidearea, percentageOfBikes, 0.5, 0.5, creationDate);
			// add this clui to the observers of network 
			n.addObserver(this);
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
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
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
			return networks.get(name).toString();
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}	
	
	/**
	 * 
	 * @param args <name>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String deleteNetwork(String[] args) throws IncorrectArgumentException {
		if (args.length == 1) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
			networks.remove(name);
			return "Sucessfully removed network " + args[0] + ".";
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}

	/**
	 * 
	 * @param args <name> <sourceX> <sourceY> <destinationX> <destinationY> <userId> <policy> <bikeType>
	 * @return
	 * @throws IncorrectArgumentException
	 */
	public String planRide(String[] args) throws IncorrectArgumentException {
		if (args.length == 8) {
			String name = args[0];
			if (!hasNetwork(name)) throw new IncorrectArgumentException("Network " + name + " does not exist.");
			try {
				double sourceX = Double.parseDouble(args[1]);
				double sourceY = Double.parseDouble(args[2]);
				double destinationX = Double.parseDouble(args[3]);
				double destinationY = Double.parseDouble(args[4]);
				int userId = Integer.parseInt(args[5]);
				String policy = args[6];
				String bikeType = args[7];
				return networks.get(name).planRide(sourceX, sourceY, destinationX, destinationY, userId, policy, bikeType);
			} catch(NumberFormatException e) {
				throw new IncorrectArgumentException("Arguments incorrectly entered, please refer to usage below.");
			}
		}
		throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}
	
	/**
	 * Parses the user input and calls the correct method to execute the command
	 * @param userInput
	 * @return
	 */
	public String parseUserInput(String userInput) {
		String message="";
		Commands command; 
		
		String[] inputs = userInput.split(" ");
		

		String commandRaw = inputs[0];
	    try {
	    		command = Commands.valueOf(commandRaw);
	    } catch (IllegalArgumentException ex) {  
	        	return "Invalid Command. Type help to have a comprenhensive overview of the different commands";
	    }
	    
	    if (inputs.length < 2 && command != Commands.help) {
			return "All commands requires network to be specified. Type help for the arguments required for each command.";
		}

		String[] arguments = java.util.Arrays.copyOfRange(inputs, 1, inputs.length);

	    switch (command) {
	    case runtest:
	    		RunCommandsFromFile.run(arguments[0], this);
	    		break;
	    case help:
	    		message = helpMessage;
	    		break;
	    case setup:
	    		try {
	    			message = setup(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + setupUsage;
	    		}
	    		break;
	    		
	    case addUser:
	    		try {
	    			message = addUser(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + addUserUsage; // TODO write up addUser usage
	    		}
	    		break;
	    
	    case offline:
	    		try {
	    			message = offline(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + offlineUsage;
	    		} 
	    		break;
	    	
	    	
	    case online:
	    		try {
	    			message = online(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + onlineUsage;
	    		}
	    		break;
	    	
	    case rentBike:
	    		try {
	    			message = rentBike(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + rentBikeUsage;
	    		}
	    		break;
	    	
	    case returnBike:
	    		try {
	    			message = returnBike(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + returnBikeUsage;
	    		}
	    		break;

	    case displayStation:
	    		try {
	    			message = displayStation(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + displayStationUsage;
	    		}
	    		break;

	    case displayUser:
	    		try {
	    			message = displayUser(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + displayUserUsage;
	    		}
	    		break;

	    case sortStation:
	    		try {
	    			message = sortStation(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + sortStationUsage;
	    		}
	    		break;

	    case display:
	    		try {
	    			message = display(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + displayUsage;
	    		}
	    		break;
	    
	    case deleteNetwork:
	    		try {
	    			message = deleteNetwork(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + deleteNetworkUsage;
	    		}
	    		break;
	    		
	    case planRide:
	    		try {
	    			message = planRide(arguments);
	    		} catch (IncorrectArgumentException e) {
	    			message = e.getMessage() + planRideUsage;
	    		}
	    		break;

	    }
	    return message;
	}
	
	@Override
	public void run() {
		System.out.println("Welcome to MyVelib. Please enter your command");
		System.out.println("Setting up an inital network named myVelib... It has 5 users, 10 stations.");
		RunCommandsFromFile.run("src/eval/my_velib.ini", this);
		System.out.println("============= Setup Complete ===========");
		String userInput = "";
		Scanner reader = new Scanner(System.in);  // Reading from System.in
		while(!userInput.equals("stop")) {
			userInput = reader.nextLine(); // Scans for user input
			System.out.println(parseUserInput(userInput));
		}
		reader.close();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Network && arg instanceof String) {
			System.out.println("======Notification========\n" + arg);
		}
	}
}
