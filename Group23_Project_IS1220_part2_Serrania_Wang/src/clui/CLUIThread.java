package clui;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import core.Network;
import utils.DateParser;

/**
 * A thread that runs commands from the command line user interface.
 * 
 * @author animato
 *
 */
public class CLUIThread extends Thread implements Observer {
	private HashMap<String, Network> networks = new HashMap<String, Network>();

	public final static String helpMessage = "\n =========== Help =========== \n" 
			+ "Setup commands:\n"
			+ "  setup <networkName> <dateTime> \n"
			+ "  setup <networkName> <dateTime> <nstations> <nslots> <sidearea> <nbikes> \n"
			+ "  addUser <networkName> <userName> <typeOfCard> \n"
			+ "  listNetworks\n" 
			+ "  deleteNetwork <networkName>\n" 
			+ "  reset\n" 
			+ "\nRent & Return commands:\n"
			+ "  rentBike <networkName> <timeOfRental> <userId> <stationId> <bikeType> \n"
			+ "  returnBike <networkName> <timeOfReturn> <userId> <stationId> \n"
			+ "\nRide planning commandhe:\n"
			+ "  planRide <networkName> <sourceX> <sourceY> <destinationX> <destinationY> <userId> <policy> <bikeType>\n"
			+ "\nStatus change commands:\n"
			+ "  offline <networkName> <stationId> \n" 
			+ "  online <networkName> <stationId> \n"
			+ "\nStats commands:\n"
			+ "  displayStation <networkName> <stationId> \n" 
			+ "  displayUser <networkName> <userId> \n"
			+ "  display <networkName> \n"
			+ "  sortStation <networkName> <sortPolicy> \n"
			+ "\nTest command:\n"
			+ "  runtest <scenarioFilePath>\n" 
			+ "  help\n" 
			+ "  help <commandName>\n"
			+ "\nType help <commandName> to get details about a command.";

	public final static String setupUsage = "\n =========== setup usage =========== \n" + "Option 1: \n"
			+ "setup <networkName> <dateTime> \n\n" + "Example: setup myVelib 01/01/2000T00:00:00 \n"
			+ "This creates a myVelib network with given name and \n" + "consisting of 10 stations each of which has \n"
			+ "10 parking slots and such that stations are arranged \n"
			+ "on a square grid whose of side 4km and initially populated \n"
			+ "with 75 bikes randomly distributed over the 10 stations.\n"
			+ "Half of the stations are plus stations, half are standard stations.\n"
			+ "Half of the bikes are elec bikes, half are mech bikes.\n\n" + "Option 2: \n"
			+ "setup <myvelibnetworkname> <dateTime> <nstations> <nslots> <sidearea> <nbikes> \n\n"
			+ "Example: setup myVelib 01/01/2000T00:00:00 10 10 4 75 \n"
			+ "This creates a myVelib network with given name and consisting of <nstations> stations \n"
			+ "each of which has <nslots> parking slots and such that stations \n"
			+ "are arranged on a square grid whose of side <sidearea>km and initially populated \n"
			+ "with <nbikes> bikes randomly distributed over the stations \n"
			+ "Half of the stations are plus stations, half are standard stations.\n"
			+ "Half of the bikes are elec bikes, half are mech bikes.\n\n";

	public final static String resetUsage = "\n =========== reset usage =========== \n" + "reset \n\n"
			+ "Resets CLUI to a clean state (without any network)\n";

	public final static String addUserUsage = "\n =========== addUser usage =========== \n"
			+ "addUser <networkName> <userName> <typeOfCard> \n\n" + "Example: addUser myVelib John NO_CARD \n"
			+ "Adds John to the myVelib Network and he does not have a card. \n"
			+ "The different card types are: NO_CARD, VLIBRE_CARD, VMAX_CARD. \n";

	public final static String offlineUsage = "\n =========== offline usage =========== \n"
			+ "offline <networkName> <stationId> \n\n" + "Example: offline myVelib 1 \n"
			+ "Set a station to offline \n";

	public final static String onlineUsage = "\n =========== online usage =========== \n"
			+ "online <networkName> <stationId> \n\n" + "Example: online myVelib 1 \n" + "Set a station to online \n";

	public final static String rentBikeUsage = "\n =========== rentBike usage =========== \n"
			+ "rentBike <networkName> <timeOfRental> <userId> <stationId> <bikeType> \n\n"
			+ "Example: rentBike myVelib 01/01/2000T00:00:00 1 1 ELEC \n"
			+ "Rent a bike from a station in given network for user <userId>. \n" + "Types of Bike: ELEC, MECH \n";

	public final static String returnBikeUsage = "\n =========== returnBike usage =========== \n"
			+ "returnBike <networkName> <timeOfRental> <userId> <stationId> \n\n"
			+ "Example: returnBike myVelib 02/01/2000T00:00:00 1 1 \n"
			+ "Return a bike to a station in given network for user <userId>. \n";

	public final static String displayStationUsage = "\n =========== displayStation usage =========== \n"
			+ "displayStation <networkName> <stationId> \n\n" + "Displays the statistics of a station \n";

	public final static String displayUserUsage = "\n =========== displayUser usage =========== \n"
			+ "displayUser <networkName> <userId> \n\n" + "Displays the statistics of a user \n";

	public final static String sortStationUsage = "\n =========== displayUser usage =========== \n"
			+ "sortStation <networkName> <sortPolicy> \n\n" + "Example: sortStation myVelib LEAST_OCCUPIED \n"
			+ "Types of policies: LEAST_OCCUPIED, MOST_USED";

	public final static String displayUsage = "\n =========== display usage =========== \n"
			+ "display <networkName> \n\n" + "Displays the details of the network \n";

	public final static String runtestUsage = "\n =========== display usage =========== \n"
			+ "runtest <scenarioFilePath> \n\n" + "Runs the given test scenario. \n"
			+ "Initially available test scenarios are: failRentReturnScenario.txt, planRideScenario.txt,\n"
			+ "statisticsScenario.txt, triggerNotificationScenario.txt, failingCommandsScenario.txt\n";

	public final static String deleteNetworkUsage = "\n =========== deleteNetwork usage =========== \n"
			+ "deleteNetwork <networkName> \n\n" + "Delete network from CLUI \n";

	public final static String listNetworksUsage = "\n =========== listNetworks usage =========== \n"
			+ "listNetworks \n\n" + "List all networks within the CLUI \n";

	public final static String planRideUsage = "\n =========== planRide usage =========== \n"
			+ "planRide <networkName> <sourceX> <sourceY> <destinationX> <destinationY> <userId> <policy> <bikeType>\n\n"
			+ "planRide myVelib 1.0 1.0 3.0 3.0 1 FASTEST MECH \n"
			+ "The types of ridePlan policies are: FASTEST, SHORTEST, PREFER_PLUS, AVOID_PLUS, PRESERVE_UNIFORMITY \n"
			+ "This command sets a ride plan for the user and they are notified when the destination stations goes offline or becomes unavailable. \n";

	/**
	 * Test if a network exists in current clui
	 * 
	 * @param name
	 * 			name of a network
	 * @return true if the name provided is the name of an existing network
	 */
	public Boolean hasNetwork(String name) {
		if (networks.get(name) == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Compares expectedLength and argumentLength and throws
	 * IncorrectArgumentException if different.
	 * 
	 * @param expectedLength
	 * 				expected length of arguments
	 * @param argumentLength
	 * 				actual length of arguments
	 * @throws IncorrectArgumentException
	 *             if expectedLength different from argumentLength
	 */
	public static void verifyArgumentLength(int expectedLength, int argumentLength) throws IncorrectArgumentException {
		if (expectedLength != argumentLength)
			throw new IncorrectArgumentException("Number of arguments is incorrect.");
	}

	/**
	 * setup a network. either default or with more options
	 * 
	 * @param args
	 *            [name] [dateTime] or [name] [dateTime] [nstations] [nslots]
	 *            [sidearea] [nbikes]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException 
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String setup(String[] args) throws IncorrectArgumentException {
		// Cannot be done with verifyArgumentLength because there are 2 possible lengths
		if (args.length != 2 && args.length != 6)
			throw new IncorrectArgumentException("Number of arguments is incorrect.");
		if (args.length == 2) {
			String name = args[0];
			try {
				LocalDateTime creationDate = DateParser.parse(args[1]);
				// verify name is not already taken
				if (hasNetwork(name))
					throw new IncorrectArgumentException("Network " + name + " already exists.");
				Network n = new Network(name, 10, 10, 4, 0.75, 0.5, 0.5, creationDate);
				// add this clui to the observers of network
				n.addObserver(this);
				networks.put(name, n);

				return "Network " + args[0] + " has been sucessfully created with " + n.getStationIds().size()
						+ " stations (ids: " + n.getStationIds() + ").";
			} catch (DateTimeParseException e) {
				throw new IncorrectArgumentException(
						"DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
			}
		} else {
			// <nstations> <nslots> <sidearea> <nbikes>
			String name = args[0];
			if (hasNetwork(name))
				throw new IncorrectArgumentException("Network " + name + " already exists.");
			LocalDateTime creationDate = DateParser.parse(args[1]);
			int nstations = Integer.parseInt(args[2]);
			int nslots = Integer.parseInt(args[3]);
			double sidearea = Double.parseDouble(args[4]);
			int nbikes = Integer.parseInt(args[5]);
			// FIXME ? not percentage but number ?
			double percentageOfBikes = (float) nbikes / (nslots * nstations);
			if (percentageOfBikes > 1)
				throw new IncorrectArgumentException("Total number of bikes exceeds the total number of slots.");
			// By default 50% of plus stations, 50% of elec bikes
			Network n = new Network(name, nstations, nslots, sidearea, percentageOfBikes, 0.5, 0.5, creationDate);
			// add this clui to the observers of network
			n.addObserver(this);
			networks.put(name, n);

			return "Network " + args[0] + " has been sucessfully created with " + n.getStationIds().size()
					+ " stations (ids: " + n.getStationIds() + ").";
		}

	}

	/**
	 * Add user to specified network
	 * 
	 * @param args
	 *            [name] [username] [cardType]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String addUser(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(3, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		return networks.get(name).addUser(args[1], args[2]);
	}

	/**
	 * set a station to offline in specified network
	 * 
	 * @param args
	 *            [name] [stationId]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String offline(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(2, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			return networks.get(name).setOffline(Integer.parseInt(args[1]));
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("StationID needs to be an integer.");
		}
	}

	/**
	 * set a station to online in specified network
	 * 
	 * @param args
	 *            [name] [stationId]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String online(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(2, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			return networks.get(name).setOnline(Integer.parseInt(args[1]));
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("StationID needs to be an integer.");
		}
	}

	/**
	 * Rent bike for a user from station at date
	 * 
	 * @param args
	 *            [name] [DateTime] [userId] [stationId] [bikeType]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String rentBike(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(5, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			LocalDateTime rentalDate = DateParser.parse(args[1]);
			int userId = Integer.parseInt(args[2]);
			int stationId = Integer.parseInt(args[3]);
			String bikeType = args[4];

			return networks.get(name).rentBike(userId, stationId, bikeType, rentalDate);
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("StationID and UserId need to be integers.");
		} catch (DateTimeParseException e) {
			throw new IncorrectArgumentException("DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
		}
	}

	/**
	 * return a bike from user to a station at given date
	 * 
	 * @param args
	 *            [name] [DateTime] [userId] [stationId]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String returnBike(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(4, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			LocalDateTime returnDate = DateParser.parse(args[1]);
			int userId = Integer.parseInt(args[2]);
			int stationId = Integer.parseInt(args[3]);
			return networks.get(name).returnBike(userId, stationId, returnDate);
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("StationID and UserId need to be integers.");
		} catch (DateTimeParseException e) {
			throw new IncorrectArgumentException("DateTime format should be like the following dd/MM/uuuuTHH:mm:ss.");
		}
	}

	/**
	 * display statistics of a station
	 * 
	 * @param args
	 *            [name] [stationId]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String displayStation(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(2, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			int stationId = Integer.parseInt(args[1]);
			return networks.get(name).displayStation(stationId);
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("StationID needs to be an integer.");
		}
	}

	/**
	 * display statistics of a user
	 * 
	 * @param args
	 *            [name] [userId]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String displayUser(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(2, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			int userId = Integer.parseInt(args[1]);
			return networks.get(name).displayUser(userId);
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("UserID needs to be an integer.");
		}
	}

	/**
	 * Display the list of stations sorted by given policy
	 * 
	 * @param args
	 *            [name] [sortPolicy]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String sortStation(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(2, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		return networks.get(name).sortStation(args[1]);
	}

	/**
	 * display all different attributes of the network
	 * 
	 * @param args
	 *            [name]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String display(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(1, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		return networks.get(name).toString();
	}

	/**
	 * delete given network from this clui
	 * 
	 * @param args
	 *            [name]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String deleteNetwork(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(1, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		networks.remove(name);
		return "Sucessfully removed network " + args[0] + ".";
	}

	/**
	 * plan a ride for a user given source and destination, as well as policy and
	 * bike type.
	 * 
	 * @param args
	 *            [name] [sourceX] [sourceY] [destinationX] [destinationY] [userId]
	 *            [policy] [bikeType]
	 * @return A message to be printed in sdtout
	 * @throws IncorrectArgumentException
	 * 				if the input arguments cannot be parsed correctly
	 */
	public String planRide(String[] args) throws IncorrectArgumentException {
		verifyArgumentLength(8, args.length);
		String name = args[0];
		if (!hasNetwork(name))
			throw new IncorrectArgumentException("Network " + name + " does not exist.");
		try {
			double sourceX = Double.parseDouble(args[1]);
			double sourceY = Double.parseDouble(args[2]);
			double destinationX = Double.parseDouble(args[3]);
			double destinationY = Double.parseDouble(args[4]);
			int userId = Integer.parseInt(args[5]);
			String policy = args[6];
			String bikeType = args[7];
			return networks.get(name).planRide(sourceX, sourceY, destinationX, destinationY, userId, policy, bikeType);
		} catch (NumberFormatException e) {
			throw new IncorrectArgumentException("Arguments incorrectly entered, please refer to usage below.");
		}
	}

	/**
	 * Delete all networks, reset ID generator
	 * @return A message to be printed in sdtout
	 * 
	 */
	public String reset() {
		Network.reset();
		this.networks = new HashMap<String, Network>();
		return "All networks deleted, reseted ID generator.";
	}

	/**
	 * 
	 * @param arguments
	 * 			array of arguments given the the command line
	 * @return A message to be printed in stdout
	 */
	public String help(String[] arguments) {
		if (arguments.length == 1) {
			Commands commandName;
			try {
				commandName = Commands.valueOf(arguments[0]);
			} catch (IllegalArgumentException ex) {
				return CLUIThread.helpMessage;
			}
			switch (commandName) {
			case reset:
				return CLUIThread.resetUsage;
			case runtest:
				return CLUIThread.runtestUsage;
			case setup:
				return CLUIThread.setupUsage;
			case addUser:
				return CLUIThread.addUserUsage;
			case offline:
				return CLUIThread.offlineUsage;
			case online:
				return CLUIThread.onlineUsage;
			case rentBike:
				return CLUIThread.rentBikeUsage;
			case returnBike:
				return CLUIThread.returnBikeUsage;
			case displayStation:
				return CLUIThread.displayStationUsage;
			case displayUser:
				return CLUIThread.displayUserUsage;
			case sortStation:
				return CLUIThread.sortStationUsage;
			case display:
				return CLUIThread.displayUsage;
			case deleteNetwork:
				return CLUIThread.deleteNetworkUsage;
			case planRide:
				return CLUIThread.planRideUsage;
			}
		}
		return CLUIThread.helpMessage;
	}

	/**
	 * Parses the user input and calls the correct method to execute the command
	 * 
	 * @param userInput
	 * 			user input in the clui
	 * @return A message to be printed in sdtout
	 */
	public String parseUserInput(String userInput) {
		String message = "";
		Commands command;

		String[] inputs = sanitizeInput(userInput);

		String commandRaw = inputs[0];
		try {
			command = Commands.valueOf(commandRaw);
		} catch (IllegalArgumentException ex) {
			return "Invalid Command. Type help to have a comprenhensive overview of the different commands";
		}

		String[] arguments = java.util.Arrays.copyOfRange(inputs, 1, inputs.length);

		switch (command) {
		case reset:
			message = this.reset();
			break;
		case runtest:
			this.reset();
			RunCommandsFromFile.run(arguments[0], this);
			break;
		case help:
			message = this.help(arguments);
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

		case listNetworks:
			message = "List of available networks:\n";
			message += String.join("\n", networks.keySet());
			break;

		}
		return message;

	}

	/**
	 * Sanitizes input : removes spaces at the beginning and at the end. removes
	 * also any special characters
	 * 
	 * @param input
	 * 			user input in the clui
	 * @return A message to be printed in sdtout
	 */
	public static String[] sanitizeInput(String input) {
		String[] arguments = input.replaceAll("[^A-Za-z0-9 .:/_]", "").trim().split(" ");
		return arguments;
	}

	/**
	 * Runs the clui until the user types in exit
	 */
	@Override
	public void run() {
		System.out.println("Setting up an inital network named myVelib... It has 5 users, 10 stations.");
		RunCommandsFromFile.run("my_velib.ini", this);
		System.out.println("============= Setup Complete ===========");
		System.out.println("\nWelcome to MyVelib. Please enter your command.");
		String userInput = "";
		Scanner reader = new Scanner(System.in); // Reading from System.in
		while (!userInput.equals("exit")) {
			System.out.print(">>> ");
			userInput = reader.nextLine(); // Scans for user input
			System.out.println(parseUserInput(userInput));
			System.out.println("\n");
		}
		reader.close();
	}

	/**
	 * Prints out notification.
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (o instanceof Network && arg instanceof String) {
			System.out.println("======Notification========\n" + arg);
		}
	}
}
