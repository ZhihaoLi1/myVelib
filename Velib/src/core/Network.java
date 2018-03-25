package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.BikeType;
import core.bike.InvalidBikeTypeException;
<<<<<<< 50ce15556ef464f8fe96877ce7aba401c1d978fb
<<<<<<< 838e9392ba86d0faea2b2b848531a4815dcbb09c
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;
=======
import core.card.CardType;
import core.card.CardVisitorFactory;
>>>>>>> Feat(Card): Add CardVisitorFactory
=======
import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
>>>>>>> Refactor(Card): Use CardVisitorFactory
import core.point.Point;
import core.rentals.BikeRental;
import core.rentals.OngoingBikeRentalException;
import core.ridePlan.AvoidPlusPlan;
import core.ridePlan.FastestPlan;
import core.ridePlan.RidePlanPolicyName;
import core.ridePlan.PreferPlusPlan;
import core.ridePlan.PreserveUniformityPlan;
import core.ridePlan.RidePlan;
import core.ridePlan.ShortestPlan;
import core.station.FullStationException;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationType;
import user.User;

// FIXME: Javadoc
public class Network {

	private String name;
	private double side;
	private HashMap<Integer, Station> stations = new HashMap<Integer, Station>();
	private HashMap<Integer, User> users = new HashMap<Integer, User>();
	private HashMap<User, BikeRental> userRentals = new HashMap<User, BikeRental>();
	private HashMap<User, RidePlan> userRidePlans = new HashMap<User, RidePlan>();

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
			double percentageOfBikes, double percentageOfPlusStations, double percentageOfElecBikes) {
		this.name = name;
		this.side = side;

		// Create Stations
		// some are plus stations others are standard stations
		for (int i = 0; i < numberOfStations; i++) {
			double x = ThreadLocalRandom.current().nextDouble(0, side);
			double y = ThreadLocalRandom.current().nextDouble(0, side);
			Point coordinates = new Point(x, y);

			StationFactory stationFactory = new StationFactory();
			Station s = null;
			try {
				if (i < numberOfStations * percentageOfPlusStations) {
					s = stationFactory.createStation(StationType.PLUS, numberOfParkingSlotsPerStation, coordinates,
							true);
				} else {
					s = stationFactory.createStation(StationType.STANDARD, numberOfParkingSlotsPerStation, coordinates,
							true);
				}
			} catch (InvalidStationTypeException e) {
				e.printStackTrace();
			}

			try {
				this.createStation(s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
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
					notEmptyStations.get(randomNum).addBike(bikeFactory.createBike(BikeType.ELEC), LocalDateTime.now());
				} else {
					notEmptyStations.get(randomNum).addBike(bikeFactory.createBike(BikeType.MECH), LocalDateTime.now());
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
	public Network(String name) {
		this(name, 10, 10, 4, 0.75, 0.5, 0.5);
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
	public RidePlan createRidePlan(Point source, Point destination, User user, RidePlanPolicyName policy, String bikeType) {
		if (source == null || destination == null || user == null || policy == null || bikeType == null)
			throw new NullPointerException("All input values of planRide must not be null");
		RidePlan rp = null;
		try {
			switch (policy) {
			case SHORTEST:
				rp = new ShortestPlan().planRide(source, destination, user, bikeType, this);
				break;
			case FASTEST:
				rp = new FastestPlan().planRide(source, destination, user, bikeType, this);
				break;
			case AVOID_PLUS:
				rp = new AvoidPlusPlan().planRide(source, destination, user, bikeType, this);
				break;
			case PREFER_PLUS:
				rp = new PreferPlusPlan().planRide(source, destination, user, bikeType, this);
				break;
			case PRESERVE_UNIFORMITY:
				rp = new PreserveUniformityPlan().planRide(source, destination, user, bikeType, this);
				break;
			default:

			}
		} catch (Exception e) {
			e.printStackTrace();
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
		RidePlanPolicyName p = RidePlanPolicyName.valueOf(policy);
		BikeType bt = BikeType.valueOf(bikeType);
		RidePlan rp = createRidePlan(source, destination, user, p, bt);
		return rp.toString();
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

		// ensure that only one user can access the station at the same time
		BikeType bt = BikeType.valueOf(bikeType);

		Bike b = null;
		synchronized (user) {
			// verify if user does not already have a rental
			if (user.getBikeRental() != null)
				return user.getName() + " still has a bike rental, he cannot rent another bike.";
			synchronized (s) {
				b = s.rentBike(bt, rentalDate);
				// if no bike is found (either station is offline or there are no bikes)
				if (b == null)
					return "No bike found of type " + bikeType + " in station " + stationId;
				// normally not suppose to happen with previous verification
				try {
					user.setBikeRental(new BikeRental(b, rentalDate));
				} catch (OngoingBikeRentalException e) {
					return user.getName() + " still has a bike rental, he cannot rent another bike.";
				}
				return user.getName() + " has rented a bike !";
			}
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
			br.setTimeSpent(timeSpent);
			// 2 users cannot return a bike at the same time at a specific station
			synchronized (s) {
				try {
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
			user.getStats().setTotalTimeSpent(br.getTimeSpent());

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
		return s.displayStats();
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

	public HashMap<User, BikeRental> getUserRentals() {
		return userRentals;
	}

	public HashMap<User, RidePlan> getUserRidePlans() {
		return userRidePlans;
	}


	public String addStation(String type) throws NullPointerException {
		StationFactory stationFactory = new StationFactory();

		try {
			Station station = stationFactory.createStation(type);
			this.createStation(station);
			return "Station " + station.getId() + " was created.";
		} catch (InvalidStationTypeException e) {
			return e.getMessage();
		}
		// verify that the coordinates of station is within the network.
		
	}
	
	public void createStation(Station station) {
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
