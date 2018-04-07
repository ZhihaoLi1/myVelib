package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ThreadLocalRandom;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;

import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.rentals.BikeRental;
import core.rentals.OngoingBikeRentalException;
import core.ridePlan.AvoidPlusPlan;
import core.ridePlan.FastestPlan;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.PreferPlusPlan;
import core.ridePlan.PreserveUniformityPlan;
import core.ridePlan.RidePlan;
import core.ridePlan.ShortestPlan;
import core.station.BikeNotFoundException;
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.InvalidTimeSpanException;
import core.station.OfflineStationException;
import core.station.ParkingSlotIDGenerator;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationIDGenerator;
import core.station.stationSort.InvalidSortingPolicyException;
import core.station.stationSort.LeastOccupiedSort;
import core.station.stationSort.MostUsedSort;
import core.user.BikeRentalNotFoundException;
import core.user.User;
import core.user.UserIDGenerator;
import utils.Point;

/**
 * Network of MyVelib Has stations and users They can rent and return bikes All
 * methods that are going to be called by the CLUI returns a string for the CLUI
 * to display.
 * 
 * @author animato
 *
 */
public class Network extends Observable {

	private String name;
	private double side = 10;

	// These dates are used to calculate the occupation rate for the entire network
	private LocalDateTime creationDate;
	private LocalDateTime currentDate;

	private HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
	private HashMap<Integer, User> users = new HashMap<Integer, User>();
	
	private ArrayList<Observer> observers = new ArrayList<Observer>();
	
	/**
	 * Creates the network (stations, parking slots and bikes)
	 * 
	 * @param name
	 * @param numberOfStations
	 * @param numberOfParkingSlotsPerStation
	 * @param side
	 *            in km
	 * @param percentageOfBikes
	 * @param percentageOfPlusStations
	 * @param percentageOfElecBikes
	 * @param creationDate
	 * 
	 */
	public Network(String name, int numberOfStations, int numberOfParkingSlotsPerStation, double side,
			double percentageOfBikes, double percentageOfPlusStations, double percentageOfElecBikes,
			LocalDateTime creationDate) {
		this.name = name;
		this.side = side;
		this.creationDate = creationDate;
		this.currentDate = creationDate;

		// Create Stations
		// some are plus stations others are standard stations
		for (int i = 0; i < numberOfStations; i++) {
			if (i < numberOfStations * percentageOfPlusStations) {
				addStation("PLUS", numberOfParkingSlotsPerStation);
			} else {
				addStation("STANDARD", numberOfParkingSlotsPerStation);
			}
		}

		/**
		 * Create and place bikes into stations notEmptyStations is the list of stations
		 * that are not full. Iterate over total number of bikes and assign a bike to a
		 * random station that is not full Each time a station is full, remove it from
		 * the list
		 */
		int totalNumberOfParkingSlots = numberOfParkingSlotsPerStation * numberOfStations;
		int totalNumberOfBikes = (int) (totalNumberOfParkingSlots * percentageOfBikes);
		ArrayList<Station> notEmptyStations = new ArrayList<Station>(this.stations.values());

		BikeFactory bikeFactory = new BikeFactory();

		for (int i = 0; i < totalNumberOfBikes; i++) {
			int randomNum = ThreadLocalRandom.current().nextInt(0, notEmptyStations.size());
			try {
				if (i < totalNumberOfBikes * percentageOfElecBikes) {
					notEmptyStations.get(randomNum).addBike(bikeFactory.createBike("ELEC"), creationDate);
				} else {
					notEmptyStations.get(randomNum).addBike(bikeFactory.createBike("MECH"), creationDate);
				}
			} catch (InvalidBikeTypeException e) {
				e.printStackTrace();
			}

			if (notEmptyStations.get(randomNum).isFull()) {
				notEmptyStations.remove(randomNum);
			}
		}
	}

	// Core functions - UI Interface
	// These functions are the ones called by UI packages

	/**
	 * Creates a ride plan for a user, given the source, destination coordinates as
	 * well as the type of bike the user wants and the policy they want to follow,
	 * and returns a message depending on what happened (success or error).
	 * 
	 * @param sourceX
	 * @param sourceY
	 * @param destinationX
	 * @param destinationY
	 * @param userId
	 * @param policy
	 * @param bikeType
	 * @return String describing the ride plan, or the error that happened.
	 */
	public String planRide(double sourceX, double sourceY, double destinationX, double destinationY, int userId,
			String policy, String bikeType) {
		if (policy == null) {
			return "A policy must be given to sort stations";
		}
		
		if (sourceX < 0 || sourceX > side || sourceY < 0 || sourceY > side) {
			return "Source coordinates out of bounds";
		}
		Point source = new Point(sourceX, sourceY);

		if (destinationX < 0 || destinationX > side || destinationY < 0 || destinationY > side) {
			return "Destination coordinates out of bounds";
		}
		Point destination = new Point(destinationX, destinationY);
		
		User user = users.get(userId);
		if (user == null)
			return "No user found with id " + userId + ".";

		try {
			String s = "";
			if (user.getRidePlan() != null) {
				s += "The previous ride plan was removed.\n";
			}
			RidePlan rp = createRidePlan(source, destination, user, policy, bikeType);
			return s + "You have subscribed to the destination station of this ride plan. You will be notified if the destination station becomes unavailable. Your ride plan is:\n"
					+ rp.toString();
		} catch (InvalidBikeTypeException e) {
			return e.getMessage();
		} catch (InvalidRidePlanPolicyException e) {
			return e.getMessage();
		} catch (NoValidStationFoundException e) {
			return e.getMessage();
		}
	}

	/**
	 * Sorts the stations of the network according to a given policy, and then
	 * returns a String representing the ordered list of stations (or an error
	 * message).
	 * 
	 * @param policy
	 * @return String listing the sorted stations
	 */
	public String sortStation(String policy) {
		if (policy == null) {
			return "A policy must be given to sort stations";
		}
		try {
			ArrayList<Station> sortedStations = createStationSort(policy);
			return "Here are the stations, in the order corresponding to the" + policy.toLowerCase() + " policy:\n"
					+ sortedStations.toString();
		} catch (InvalidSortingPolicyException e) {
			return e.getMessage();
		} catch (InvalidTimeSpanException e) {
			return e.getMessage();
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}

	/**
	 * Add a new user with a specific card type and random coordinates to the
	 * network.
	 * 
	 * @param name
	 * @param cardType
	 * @return a String message saying if the adding happened, or if an error
	 *         happened
	 */
	public String addUser(String name, String cardType) {
		CardVisitorFactory cardFactory = new CardVisitorFactory();

		double x = ThreadLocalRandom.current().nextDouble(0, side);
		double y = ThreadLocalRandom.current().nextDouble(0, side);
		Point coordinates = new Point(x, y);

		try {
			CardVisitor card = cardFactory.createCard(cardType);
			User user = new User(name, coordinates, card);
			this.addUser(user);
			return "User " + user.getName() + " (id: " + user.getId() + ") was added with card of type: " + cardType.toLowerCase()
					+ ".";
		} catch (InvalidCardTypeException e) {
			return e.getMessage();
		}
	}

	/**
	 * Add a new station (with random coordinates) to the network.
	 * 
	 * @param type
	 * @param numberOfParkingSlots
	 * 
	 * @return a String message saying if the adding happened, or if an error
	 *         happened
	 */
	public String addStation(String type, int numberOfParkingSlots) {
		StationFactory stationFactory = new StationFactory();

		double x = ThreadLocalRandom.current().nextDouble(0, side);
		double y = ThreadLocalRandom.current().nextDouble(0, side);
		Point coordinates = new Point(x, y);

		try {
			Station station = stationFactory.createStation(type, numberOfParkingSlots, coordinates, true);
			this.addStation(station);
			return "Station " + station.getId() + " was created with " + station.getParkingSlots().size()
					+ " parking slots, at point " + station.getCoordinates() + ", with online status "
					+ station.getOnline() + ".";
		} catch (InvalidStationTypeException e) {
			return e.getMessage();
		}
	}

	/**
	 * Add a new station to the network.
	 * 
	 * @param type
	 * @param x
	 *            the x coordinate of the station
	 * @param y
	 *            the y coordinate of the station
	 * @param numberOfParkingSlots
	 * @param online
	 * @return a String message saying if the adding happened, or if an error
	 *         happened
	 */
	public String addStation(String type, double x, double y, int numberOfParkingSlots, boolean online) {
		StationFactory stationFactory = new StationFactory();

		if (x < 0 || x > side || y < 0 || y > side) {
			return "Coordinates out of bounds";
		}
		Point coordinates = new Point(x, y);

		try {
			Station station = stationFactory.createStation(type, numberOfParkingSlots, coordinates, online);
			this.addStation(station);
			return "Station " + station.getId() + " was created with " + station.getParkingSlots().size()
					+ " parking slots, at point " + station.getCoordinates() + ", with online status "
					+ station.getOnline() + ".";
		} catch (InvalidStationTypeException e) {
			return e.getMessage();
		}
	}

	/**
	 * Rents a bike, and returns a String describing Rents the bike and returns the
	 * corresponding message to be passed on to the CLUI
	 * 
	 * @param userId
	 * @param stationId
	 * @param bikeType
	 * @return String (either an error message or a confirmation message)
	 */
	public String rentBike(int userId, int stationId, String bikeType, LocalDateTime rentalDate) {
		// find user
		User user = users.get(userId);
		// find station
		Station s = stations.get(stationId);
		if (user == null)
			return "No user found with id " + userId;
		if (s == null)
			return "No station found with id " + stationId;
		try {
			Bike b = rentBike(user, s, bikeType, rentalDate);
			return user.getName() + " has sucessfully rented a " + b.getType().toLowerCase() + " bike from station " + s.getId()
					+ " at " + rentalDate + ".";
		} catch (OngoingBikeRentalException e) {
			return e.getMessage();
		} catch (OfflineStationException e) {
			return e.getMessage();
		} catch (BikeNotFoundException e) {
			return e.getMessage();
		}
	}

	/**
	 * Returns the bike of a user and returns the corresponding message to be passed
	 * on to the CLUI
	 * 
	 * @param userId
	 * @param stationId
	 * @param returnDate
	 * @return String (either an error message or a confirmation message)
	 */
	public String returnBike(int userId, int stationId, LocalDateTime returnDate) {
		// find user
		User user = users.get(userId);
		// find station
		Station station = stations.get(stationId);
		if (user == null)
			return "No user found with id " + userId;
		if (station == null)
			return "No station found with id " + stationId;
		try {
			BikeRental br = returnBike(user, station, returnDate);
			return user.getName() + " should pay " + br.getPrice() + " euros for this ride which lasted "
					+ br.getTimeSpent() + " minutes. (" + br.getTimeCreditUsed() + " minutes of time credit used)."
					+ " Thank you for choosing MyVelib, have a wonderful day!";
		} catch (InvalidDatesException e) {
			return e.getMessage();
		} catch (InvalidBikeException e) {
			return e.getMessage();
		} catch (BikeRentalNotFoundException e) {
			return e.getMessage();
		} catch (FullStationException e) {
			return e.getMessage();
		} catch (OfflineStationException e) {
			return e.getMessage();
		}
	}

	/**
	 * Display statistics of a station
	 * 
	 * @param stationId
	 * @return a String representing the stats of a station
	 */
	public String displayStation(int stationId) {
		Station s = this.stations.get(stationId);
		if (s == null)
			return "No station found for id " + stationId;
		String res = s.displayStats();
		try {
			res += "\n" + s.displayOccupationRate(creationDate, currentDate);
		} catch (InvalidTimeSpanException e) {
			res += "\n" + e.getMessage();
		}
		return res;
	}

	/**
	 * Display user statistics
	 * 
	 * @param userId
	 * @return a string representing the stats of a user
	 */
	public String displayUser(int userId) {
		User u = this.users.get(userId);
		if (u == null)
			return "No user found for id " + userId;
		return u.displayStats();
	}

	/**
	 * Set station to offline
	 * 
	 * @param stationId
	 * @return a string message about the performed action
	 */
	public String setOffline(int stationId) {
		Station s = this.stations.get(stationId);
		if (s == null)
			return "No station found for id " + stationId;
		if (s.getOnline() == false) {
			return "Station " + stationId + " is already offline.";
		}
		s.setOnline(false);
		return "Station " + stationId + " is set to offline.";
	}

	/**
	 * Set station to online
	 * 
	 * @param stationId
	 *            the station
	 * @return a string message about the performed action
	 */
	public String setOnline(int stationId) {
		Station s = this.stations.get(stationId);
		if (s == null)
			return "No station found for id " + stationId;
		if (s.getOnline() == true) {
			return "Station " + stationId + " is already online.";
		}
		s.setOnline(true);
		return "Station " + stationId + " is set to online.";
	}

	// Core methods - Internal methods
	//
	/**
	 * Add station to network
	 * 
	 * @param station
	 *            the station to add
	 * @throws IllegalArgumentException
	 *             when the station is null
	 */
	public void addStation(Station station) throws IllegalArgumentException {
		if (station == null) {
			throw new IllegalArgumentException("Station given is null in addStation.");
		}
		// verify that the coordinates of station is within the network.
		this.stations.put(station.getId(), station);
	}

	/**
	 * Add user to network
	 * 
	 * @param user
	 *            the user to add
	 * @throws IllegalArgumentException
	 *             when the user is null
	 */
	public void addUser(User user) throws IllegalArgumentException {
		if (user == null) {
			throw new IllegalArgumentException("User given is null in addUser.");
		}
		this.users.put(user.getId(), user);
	}

	/**
	 * Creates a ride plan for a user, given the source, destination coordinates as
	 * well as the type of bike the user wants and the policy they want to follow.
	 * 
	 * @param source
	 * @param destination
	 * @param user
	 * @param policy
	 * @param bikeType
	 * @return rideplan Object
	 * @throws NoValidStationFoundException
	 * @throws InvalidBikeTypeException
	 * @throws InvalidRidePlanPolicyException
	 */
	public RidePlan createRidePlan(Point source, Point destination, User user, String policy, String bikeType)
			throws NoValidStationFoundException, InvalidBikeTypeException, InvalidRidePlanPolicyException {
		if (source == null || destination == null || user == null || policy == null || bikeType == null)
			throw new IllegalArgumentException("All input values of createRidePlan must not be null");
		
		RidePlan rp = null;
		switch (policy.toUpperCase()) {
		case "SHORTEST":
			rp = new ShortestPlan().planRide(source, destination, user, bikeType, this);
			break;
		case "FASTEST":
			rp = new FastestPlan().planRide(source, destination, user, bikeType, this);
			break;
		case "AVOID_PLUS":
			rp = new AvoidPlusPlan().planRide(source, destination, user, bikeType, this);
			break;
		case "PREFER_PLUS":
			rp = new PreferPlusPlan().planRide(source, destination, user, bikeType, this);
			break;
		case "PRESERVE_UNIFORMITY":
			rp = new PreserveUniformityPlan().planRide(source, destination, user, bikeType, this);
			break;
		default:
			throw new InvalidRidePlanPolicyException(policy);
		}
		// add user to list of observers in concerned destination stations
		rp.getDestinationStation().addObserver(user);
		user.setRidePlan(rp);
		return rp;
	}

	/**
	 * Sorts the stations of the network according to a given policy.
	 * 
	 * @param policy
	 * @return the sorted list of stations
	 */
	public ArrayList<Station> createStationSort(String policy) throws InvalidSortingPolicyException, InvalidTimeSpanException {
		if (policy == null)
			throw new IllegalArgumentException("All input values of createStationSort must not be null");
		ArrayList<Station> sortedStations = null;
		switch (policy.toUpperCase()) {
		case "MOST_USED":
			sortedStations = new MostUsedSort().sort(new ArrayList<Station>(this.getStations().values()), creationDate,
					currentDate);
			break;
		case "LEAST_OCCUPIED":
			sortedStations = new LeastOccupiedSort().sort(new ArrayList<Station>(this.getStations().values()),
					creationDate, currentDate);
			break;
		default:
			throw new InvalidSortingPolicyException(policy);
		}
		return sortedStations;
	}

	/**
	 * Completes the operation for renting a bike, returns the bike that is rented
	 * 
	 * @param user
	 * @param station
	 * @param bikeType
	 * @param rentalDate
	 * @return the bike that was rented
	 * @throws OngoingBikeRentalException
	 *             if the user already has a bikeRental
	 * @throws BikeNotFoundException
	 *             if the station has no bikes of type bikeType left
	 */
	public Bike rentBike(User user, Station station, String bikeType, LocalDateTime rentalDate)
			throws OngoingBikeRentalException, OfflineStationException, BikeNotFoundException {
		Bike b = null;
		synchronized (user) {
			// verify if user does not already have a rental
			if (user.getBikeRental() != null)
				throw new OngoingBikeRentalException(user);
			synchronized (station) {
				// If no bike is found (either station is offline or there are no bikes), an
				// exception will be thrown here
				b = station.rentBike(bikeType, rentalDate);
				user.setBikeRental(new BikeRental(b, rentalDate));
				
				// Update the current date
				this.currentDate = rentalDate;
				return b;
			}
		}
	}

	/**
	 * Returns the bike of a user to the given station at the given time
	 * 
	 * @param user
	 * @param station
	 * @param returnDate
	 * @return String (either an error message or a confirmation message)
	 */
	public BikeRental returnBike(User user, Station station, LocalDateTime returnDate)
			throws BikeRentalNotFoundException, FullStationException, OfflineStationException, InvalidBikeException,
			InvalidDatesException {
		// the same user cannot return more than one bike at the same time.
		synchronized (user) {
			// make sure user has a rental
			BikeRental br = user.getBikeRental();
			if (br == null)
				throw new BikeRentalNotFoundException(user.getId());
			br.setReturnDate(returnDate);
			// 2 users cannot return a bike at the same time at a specific station
			synchronized (station) {
				// If station is offline, will throw OfflineStationException; if station is
				// full, will throw FullStationException
				station.returnBike(br, returnDate);

				int timeCreditAdded = user.getCard().addTimeCredit(station.getBonusTimeCreditOnReturn());
				// Calculate the price of the ride. Throws InvalidBikeException or
				// InvalidDatesException if the calculation couldn't be performed
				try {
					user.getCard().visit(br);
				} catch (InvalidBikeException | InvalidDatesException e) {
					// As the operation couldn't be performed, rollback to the previous time credit
					// status
					user.getCard().removeTimeCredit(timeCreditAdded);
					throw e;
				}

				// Add the amount of credits added to the total time credits stat of
				// the user.
				user.getStats().addTotalTimeCredits(timeCreditAdded);
				// Now remove the time credit from the user's card
				user.getCard().removeTimeCredit(br.getTimeCreditUsed());
				
				// if user completes ride plan (station that he is returning the bike to is the
				// same as the destination station in ride plan)
				// then the user's ride plan is set to null
				if (user.getRidePlan() != null && station.equals(user.getRidePlan().getDestinationStation())) {
					user.setRidePlan(null);
				}

				// increment station statistics
				station.getStats().incrementTotalReturns();
			}

			// Update the user's stats
			user.getStats().addTotalCharges(br.getPrice());
			user.getStats().incrementTotalRides();
			user.getStats().addTotalTimeSpent(br.getTimeSpent());

			// reset user bike rental
			user.resetBikeRental();
			
			// Update the current date
			this.currentDate = returnDate;

			return br;
		}
	}

	/**
	 * Send to CLI that station is full, and a ridePlan is cancelled
	 * 
	 * @param user
	 * @param station
	 * @return
	 */
	public String notifyStationFull(User user, Station station) {
		return "Station with id " + station.getId() + " is full and ride plan for " + user.getName()
				+ " is cancelled. Please create a new one";
	}
	
	/**
	 * Reset ID generators
	 * 
	 * @return
	 */
	public static String reset() {
		StationIDGenerator.getInstance().reset();
		UserIDGenerator.getInstance().reset();
		ParkingSlotIDGenerator.getInstance().reset();
		return "Sucessfully reset ID generators";
	}

	// Getters / Setters
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getSide() {
		return side;
	}

	public void setSide(double side) {
		this.side = side;
	}

	public HashMap<Integer, Station> getStations() {
		return stations;
	}

	public ArrayList<Integer> getStationIds() {
		return new ArrayList<Integer>(stations.keySet());
	}

	public HashMap<Integer, User> getUsers() {
		return users;
	}

	public ArrayList<Integer> getUserIds() {
		return new ArrayList<Integer>(users.keySet());
	}

	@Override
	public String toString() {
		String s = "Network " + name + ":";
		s += "\nCreated: " + creationDate;
		s += "\nCurrent date: " + currentDate;
		s += "\nSide: " + side + "km";
		s += "\n\n--------------------";
		s += "\nUsers: ";
		for (User user : users.values()) {
			s += "\n\n" + user.toString();
		}
		s += "\n\n--------------------";
		s += "\nStations: ";
		for (Station station : stations.values()) {
			s += "\n\n" + station.toString();
		}
		return s;
	}
	
	@Override
	public void addObserver(Observer o ) {
		if (!this.observers.contains(o)) {
			this.observers.add(o);
		}
	}
	
	/**
	 * Called user update is called. 
	 */
	public void notifyObservers(String message) {
		for (Observer o: this.observers) {
			o.update(this, message);
		}
	}
}
