package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;

import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.point.Point;
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
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.station.stationSort.InvalidSortingPolicyException;
import core.station.stationSort.LeastOccupiedSort;
import core.station.stationSort.MostUsedSort;
import user.User;

/**
 * Network of MyVelib
 * Has stations and users 
 * They can rent and return bikes
 * @author animato
 *
 */
public class Network {

	private String name;
	private double side;
	
	// These dates are used to calculate the occupation rate for the entire network
	private LocalDateTime creationDate;
	private LocalDateTime currentDate;
	private HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
	private HashMap<Integer, User> users = new HashMap<Integer, User>();

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
	 * 
	 */
	public Network(String name, int numberOfStations, int numberOfParkingSlotsPerStation, double side,
			double percentageOfBikes, double percentageOfPlusStations, double percentageOfElecBikes, LocalDateTime creationDate) {
		this.name = name;
		this.side = side;
		this.creationDate = creationDate;
		this.currentDate = creationDate;
	 
		// Create Stations
		// some are plus stations others are standard stations
		for (int i = 0; i < numberOfStations; i++) {
			if (i < numberOfStations * percentageOfPlusStations) {
				addStation("PLUS");
			} else {
				addStation("STANDARD");
			}
		}

		/**
		 * Create and place bikes into stations notEmptyStations is the list of stations
		 * that are not full.
		 * Iterate over total number of bikes and assign a bike to a
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

	/**
	 * Creates the network (stations, parking slots and bikes) with 10 stations (10
	 * parking slots each), 4km side, 75% full of bikes <br>
	 * Half of the stations are plus stations, half of the bikes are elec bikes.
	 * 
	 * @param name
	 * 
	 */
	public Network(String name, LocalDateTime creationDate) {
		this(name, 10, 10, 4, 0.75, 0.5, 0.5, creationDate);
	}

	public Network() {
	}	
	
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param user
	 * @param policy
	 * @param bikeType
	 * @return rideplan Object
	 */
	public RidePlan createRidePlan(Point source, Point destination, User user, String policy, String bikeType) throws NoValidStationFoundException, InvalidBikeTypeException, InvalidRidePlanPolicyException {
		if (source == null || destination == null || user == null || policy == null || bikeType == null)
			throw new IllegalArgumentException("All input values of planRide must not be null");
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
	 * 
	 * @param source
	 * @param destination
	 * @param user
	 * @param policy
	 * @param bikeType
	 * @return String describing the ride plan
	 */
	public String planRide(Point source, Point destination, User user, String policy, String bikeType) {
		try {
			RidePlan rp = createRidePlan(source, destination, user, policy, bikeType);
			return "You have subscribed to the destination station of this ride plan. You will be notified if the destination station becomes unavailable. \n" + rp.toString();
		} catch (InvalidBikeTypeException e) {
			return e.getMessage();
		} catch (InvalidRidePlanPolicyException e) {
			return e.getMessage();
		} catch (NoValidStationFoundException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * 
	 * @param policy
	 * @return the sorted list of stations
	 */
	public ArrayList<Station> createStationSort(String policy) throws InvalidSortingPolicyException {
		ArrayList<Station> sortedStations = null;
		switch (policy.toUpperCase()) {
			case "MOST_USED":
				sortedStations = new MostUsedSort().sort(new ArrayList<Station>(this.getStations().values()), creationDate, currentDate);
				break;
			case "LEAST_OCCUPIED":
				sortedStations = new LeastOccupiedSort().sort(new ArrayList<Station>(this.getStations().values()), creationDate, currentDate);
				break;
			default:
				throw new InvalidSortingPolicyException(policy);
		}
		return sortedStations;
	}
	
	/**
	 * 
	 * @param policy
	 * @return String listing the sorted stations
	 */
	public String sortStation(String policy) {
		try {
			ArrayList<Station> sortedStations = createStationSort(policy);
			return "Here are the stations, in the order corresponding to the" + policy + " policy:\n" + sortedStations.toString();
		} catch (InvalidSortingPolicyException e) {
			return e.getMessage();
		}
	}

	public Bike rentBike(User user, Station station, String bikeType, LocalDateTime rentalDate) throws Exception{
		Bike b = null;
		synchronized (user) {
			// verify if user does not already have a rental
			if (user.getBikeRental() != null)
				throw new Exception(user.getName() + " still has a bike rental, he cannot rent another bike.");
			synchronized (station) {
				b = station.rentBike(bikeType, rentalDate);
				// if no bike is found (either station is offline or there are no bikes)
				if (b == null)
					throw new Exception("No bike found of type " + bikeType + " in station " + station.getId());
				// normally not suppose to happen with previous verification
				try {
					user.setBikeRental(new BikeRental(b, rentalDate));
				} catch (OngoingBikeRentalException e) {
					throw new Exception(user.getName() + " still has a bike rental, he cannot rent another bike.");
				}
				this.currentDate = rentalDate;
				return b;
			}
		}
	}
	
	/**
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
			if (b != null)
				return user.getName() + " has sucessfully rented a bike at " + rentalDate + ".\n";
			throw new Exception("No bike found to rent");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return e.getMessage();
		}
	}

	/**
	 * 
	 * @param userId
	 * @param stationId
	 * @param returnDate
	 * @return String (either an error message or a confirmation message)
	 */
	public String returnBike(int userId, int stationId, LocalDateTime returnDate, int timeSpent) {
		// find user
		User user = users.get(userId);
		// find station
		Station s = stations.get(stationId);
		if (user == null)
			return "No user found with id " + userId;
		if (s == null)
			return "No station found with id " + stationId;
		// the same user cannot return more than one bike at the same time.
		synchronized (user) {
			// make sure user has a rental
			BikeRental br = user.getBikeRental();
			if (br == null)
				return "User is not renting a bike, he cannot return a bike";
			br.setReturnDate(returnDate);
			br.setTimeSpent(br.getTimeSpent() + timeSpent);
			// 2 users cannot return a bike at the same time at a specific station
			synchronized (s) {
				try {
					// if user completes ride plan (station that he is returning the bike to is the same as the destination station in ride plan)
					// then the user's ride plan is set to null
					if (user.getRidePlan() != null && s.equals(user.getRidePlan().getDestinationStation())) {
						user.resetRidePlan();						
					}

					s.returnBike(br, returnDate);
					// reset user bike rental
					user.resetBikeRental();
				} catch (FullStationException e) {
					return e.getMessage();
				}
				// increment station statistics
				s.getStats().incrementTotalReturns();
			}
			// add time credit if return station is a plus station
			user.getCard().addTimeCredit(s.getBonusTimeCreditOnReturn());
			user.getStats().addTotalTimeCredits(s.getBonusTimeCreditOnReturn());
			
			// calculate price
			double price;
			try {
				price = user.getCard().visit(br);
			} catch (InvalidBikeException e) {
				return e.getMessage();
			} catch (InvalidDatesException e) {
				return e.getMessage();
			}
			user.getStats().addTotalCharges(price);

			user.getStats().incrementTotalRides();
			user.getStats().addTotalTimeSpent(br.getTimeSpent());

			this.currentDate = returnDate;
			return user.getName() + " should pay " + price
					+ " euros for this ride that lasted "+ timeSpent +" minutes. Thank you for choosing MyVelib, have a wonderful day!";
		}
	}

	/**
	 * Display statistics of a station
	 * 
	 * @param stationId
	 * @return
	 */
	public String displayStation(int stationId) {
		Station s = this.stations.get(stationId);
		if (s == null)
			return "No station found for id " + stationId;
		String res = s.displayStats();
		try {
			res += "\n" + s.displayOccupationRate(creationDate, currentDate);
		} catch (IllegalArgumentException e) {
			res += "\n" + e.getMessage();
		}
		return res;
	}

	/**
	 * Display user statistics
	 * 
	 * @param userId
	 * @return
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
	 * @return
	 */
	public String setOffline(int stationId) {
		Station s = this.stations.get(stationId);
		if (s == null)
			return "No station found for id " + stationId;
		s.setOnline(false);
		return "Station " + stationId + " is set to offline.";
	}

	/**
	 * Set station to online
	 * 
	 * @param stationId
	 * @return
	 */
	public String setOnline(int stationId) {
		Station s = this.stations.get(stationId);
		if (s == null)
			return "No station found for id " + stationId;
		s.setOnline(true);
		return "Station " + stationId + " is set to online.";
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

	public void setStations(HashMap<Integer, Station> stations) {
		this.stations = stations;
	}

	public HashMap<Integer, User> getUsers() {
		return users;
	}

	public String addStation(String type) {
		StationFactory stationFactory = new StationFactory();

		double x = ThreadLocalRandom.current().nextDouble(0, side);
		double y = ThreadLocalRandom.current().nextDouble(0, side);
		Point coordinates = new Point(x, y);

		try {
			Station station = stationFactory.createStation(type, 10, coordinates, true);
			this.createStation(station);
			return "Station " + station.getId() + " was created with " + station.getParkingSlots().size()
					+ " parking slots, at point " + station.getCoordinates() + ", with online status "
					+ station.getOnline() + ".";
		} catch (InvalidStationTypeException e) {
			return e.getMessage();
		}
	}

	public String addStation(String type, double x, double y, int numberOfParkingSlots, boolean online) {
		StationFactory stationFactory = new StationFactory();

		if (x < 0 || x > side || y < 0 || y > side) {
			return "Coordinates out of bounds";
		}
		Point coordinates = new Point(x, y);

		try {
			Station station = stationFactory.createStation(type, numberOfParkingSlots, coordinates, online);
			this.createStation(station);
			return "Station " + station.getId() + " was created with " + station.getParkingSlots().size()
					+ " parking slots, at point " + station.getCoordinates() + ", with online status "
					+ station.getOnline() + ".";
		} catch (InvalidStationTypeException e) {
			return e.getMessage();
		}
	}

	public void createStation(Station station) throws IllegalArgumentException {
		if (station == null) {
			throw new IllegalArgumentException("Station is null in addStation");
		}
		// verify that the coordinates of station is within the network.
		this.stations.put(station.getId(), station);
	}

	public String addUser(String name, String cardType) {
		CardVisitorFactory cardFactory = new CardVisitorFactory();

		try {
			CardVisitor card = cardFactory.createCard(cardType);
			User user = new User(name, card);
			this.createUser(user);
			return "User " + user.getName() + " (id: " + user.getId() + ") was added with card of type: " + cardType
					+ ".";
		} catch (InvalidCardTypeException e) {
			return e.getMessage();
		}

	}

	public void createUser(User user) {
		if (user == null) {
			throw new IllegalArgumentException("User is null in createUser");
		}
		this.users.put(user.getId(), user);
	}

}
