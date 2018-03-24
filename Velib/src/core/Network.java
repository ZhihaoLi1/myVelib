package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.point.Point;
import core.rentals.BikeRental;
import core.ridePlan.AvoidPlusPlan;
import core.ridePlan.FastestPlan;
import core.ridePlan.PreferPlusPlan;
import core.ridePlan.PreserveUniformityPlan;
import core.ridePlan.RidePlan;
import core.ridePlan.ShortestPlan;
import core.station.InvalidStationTypeException;
import core.station.Station;
import core.station.StationFactory;
import core.station.StationType;

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
	 * FIXME: I think the number of stations is the same for each Station in a
	 * network
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
	public Network(String name, int numberOfStations, ArrayList<Integer> numberOfParkingSlotsPerStation, double side,
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
					s = stationFactory.createStation(StationType.PLUS, numberOfParkingSlotsPerStation.get(i),
							coordinates, true);
				} else {
					s = stationFactory.createStation(StationType.STANDARD, numberOfParkingSlotsPerStation.get(i),
							coordinates, true);
				}
			} catch (InvalidStationTypeException e) {
				e.printStackTrace();
			}

			try {
				this.addStation(s);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// create a bike , add it to a random station
		// Some are mech, some are elec
		Set<Integer> keys = this.stations.keySet();

		System.out.println(keys.toString());

		/**
		 * Create and place bikes into stations notEmptyStations is the list of stations
		 * that are not full Iterate over total number of bikes and assign a bike to a
		 * random station that is not full Each time a station is full, remove it from
		 * the list
		 */
		int totalNumberOfParkingSlots = numberOfParkingSlotsPerStation.stream().mapToInt(Integer::intValue).sum();
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

	public Network() {
	}

	public RidePlan planRide(Point source, Point destination, User user, PolicyName policy, BikeType bikeType) {
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
				throw new Exception("policy not found");
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
	 * @param userId
	 * @param stationId
	 * @param bikeType
	 * @throws Exception
	 *             if user already has a bike rental, if station is offline, if no
	 *             appropriate bike is found in the station.
	 */
	public String rentBike(int userId, int stationId, String bikeType) {
		// find user
		User user = users.get(userId);
		// find station
		Station s = stations.get(stationId);
		if (user == null || s == null)
			throw new NullPointerException("No user or station found given Id.");
		// ensure that only one user can access the station at the same time
		BikeType bt = BikeType.valueOf(bikeType);

		Bike b = null;
		synchronized (user) {
			// verify if user does not already have a rental
			if (user.getBikeRental() != null)
				return user.getName() + " still has a bike rental, he cannot rent another bike."; 
			synchronized (s) {
				b = s.rentBike(bt, LocalDateTime.now());
				// if no bike is found (either station is offline or there are no bikes)
				if (b == null)
					return "No bike found of type " + bikeType + " in station " + stationId;
				// normally not suppose to happen with previous verification
				try {
					user.setBikeRental(new BikeRental(b, LocalDateTime.now()));
				} catch (OngoingBikeRentalException e) {
					return user.getName() + " still has a bike rental, he cannot rent another bike."; 
				}
				return user.getName() + "has rented a bike !";
			}
		}
	}

	/**
	 * 
	 * @param userId
	 * @param stationId
	 * @param returnDate
	 * @throws Exception
	 *             when station is full
	 */
	public String returnBike(int userId, int stationId, LocalDateTime returnDate, int timeSpent) throws Exception {
		// find user
		User user = users.get(userId);
		// find station
		Station s = stations.get(stationId);
		if (user == null)
			return "No user found with id "+ userId;
		if (s == null)
			return "No station found with id "+ stationId;
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
				s.returnBike(br, returnDate);
				// increment station statistics
				s.getStats().incrementTotalReturns();
			}
			// add time credit if return station is a plus station
			user.getCard().addTimeCredit(s.getBonusTimeCreditOnReturn());
			user.getStats().addTotalTimeCredits(s.getBonusTimeCreditOnReturn());

			// calculate price
			double price = user.getCard().visit(br);
			user.getStats().addTotalCharges(price);

			user.getStats().incrementTotalRides();
			user.getStats().setTotalTimeSpent(br.getTimeSpent());
			// FIXME: Forgot to reset the user's bikeRental?

			return user.getName() + " should pay " + price + "for this ride. Thank you for choosing MyVelib, have a wonderful day!" ;
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
		return "Station with id " + station.getId() + " is full and ride plan for " + user.getName() + " is cancelled. Please create a new one";
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

	/**
	 * 
	 * @param station
	 * @throws NullPointerException
	 *             if station is null
	 */
	public void addStation(Station station) throws NullPointerException {
		if (station == null) {
			throw new NullPointerException("Station is null in addStation");
		}
		// verify that the coordinates of station is within the network.
		this.stations.put(station.getId(), station);
	}
	
	public void addUser(User user) throws NullPointerException {
		if (user == null)
			throw new NullPointerException("User is null in addUser");
		this.users.put(user.getId(), user);
	}

}
