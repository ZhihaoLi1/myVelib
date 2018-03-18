package core;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.point.Point;
import core.rentals.BikeRental;
import core.ridePlan.AvoidPlusPlan;
import core.ridePlan.FastestPlan;
import core.ridePlan.PreferPlusPlan;
import core.ridePlan.PreserveUniformityPlan;
import core.ridePlan.RidePlan;
import core.ridePlan.RidePlanStrategy;
import core.ridePlan.ShortestPlan;
import core.station.ParkingSlot;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;

public class Network {
	
	private String name;
	private double side;
	private HashMap<Integer, Station> stations = new HashMap<Integer, Station>(); 
	private HashMap<Integer, User> users = new HashMap<Integer, User>() ;
	private HashMap<User, BikeRental> userRentals = new HashMap<User, BikeRental>();
	private HashMap<User, RidePlan> userRidePlans = new HashMap<User, RidePlan>();
	
	/**
	 * Creates the network 
	 * => stations, parking slots and bikes
	 * 
	 * @param name
	 * @param numberOfStations
	 * @param numberOfParkingSlotsPerStation
	 * @param side in km
	 * @param percentageOfBikes
	 * @param percentageOfPlusStations
	 * @param percentageOfElecBikes
	 * @throws Exception 
	 */
	public Network(String name, int numberOfStations, ArrayList<Integer> numberOfParkingSlotsPerStation, double side, 
			double percentageOfBikes, double percentageOfPlusStations, double percentageOfElecBikes ) {
		this.name = name;
		this.side = side;
		
		// Create Stations
		// some are plus stations others are standard stations
		for (int i = 0; i < numberOfStations; i++) {
			double x = ThreadLocalRandom.current().nextDouble(0, side);
			double y = ThreadLocalRandom.current().nextDouble(0, side);
			Point coordinates = new Point(x, y);
			Station s;
			if (i < numberOfStations*percentageOfPlusStations) {
				s = new PlusStation(numberOfParkingSlotsPerStation.get(i), coordinates);
			} else {
				s = new StandardStation(numberOfParkingSlotsPerStation.get(i), coordinates);
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

		int totalNumberOfParkingSlots = numberOfParkingSlotsPerStation.stream().mapToInt(Integer::intValue).sum();
		int totalNumberOfBikes = (int) (totalNumberOfParkingSlots*percentageOfBikes);
		for (Station station : this.stations.values()) {
			for (int i=0; i<station.getParkingSlots().size(); i++ ) {
				// TODO FINISH SETUP
			}
		}
	}
		
	public Network() {
	}

	/**
	 * 
	 * @param station
	 * @throws NullPointerException if station is null
	 */
	public void addStation(Station station) throws NullPointerException {
		if(station == null) {
			throw new NullPointerException("Station is null in addStation");
		}
		// verify that the coordinates of station is within the network. 
		this.stations.put(station.getId(), station);
	}

	public void addUser(User user) throws NullPointerException {
		if(user == null) throw new NullPointerException("User is null in addUser");
		this.users.put(user.getId(), user);
	}
	public RidePlan planRide(Point source, Point destination, User user, PolicyName policy, BikeType bikeType) {
		if(source == null || destination == null || user == null || policy == null || bikeType == null) 
			throw new NullPointerException("All input values of planRide must not be null");
		RidePlan rp=null;
		try {
			switch(policy) {
			case SHORTEST:
				rp = new ShortestPlan().planRide(source, destination, user, bikeType, stations);
				break;
			case FASTEST:
				rp = new FastestPlan().planRide(source, destination, user, bikeType, stations);
				break;
			case AVOID_PLUS:
				rp = new AvoidPlusPlan().planRide(source, destination, user, bikeType, stations);
				break;
			case PREFER_PLUS:
				rp = new PreferPlusPlan().planRide(source, destination, user, bikeType, stations);
				break;
			case PRESERVE_UNIFORMITY:
				rp = new PreserveUniformityPlan().planRide(source, destination, user, bikeType, stations);
				break;
			default:
				throw new Exception("policy not found");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		// add user to list of observers in concerned destination stations 
		rp.getDestinationStation().addObserver(user);
		return rp;	
	}

	/**
	 * 
	 * @param userId
	 * @param stationId
	 * @param bikeType
	 * @throws Exception if user already has a bike rental, if station is offline, if no appropriate bike is found in the station.
	 */
	public void rent(int userId, int stationId, String bikeType) throws Exception {
		// find user 
		User user = users.get(userId);
		// find station
		Station s = stations.get(stationId);
		if (user == null || s == null) throw new NullPointerException("No user or station found given Id.");
		// ensure that only one user can access the station at the same time
		BikeType bt = BikeType.valueOf(bikeType);
		
		Bike b = null;
		synchronized(user) {
			// verify if user does not already have a rental 
			if (user.getBikeRental() != null) throw new Exception("User already has an ongoing bike rental! ");
			synchronized(s) {
				b = s.rentBike(bt);
				// if no bike is found
				if (b == null) throw new Exception("Bike of the correct kind is not found in station");
				user.setBikeRental(new BikeRental(b, LocalDateTime.now()));
			}
		}
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

	public void setUsers(HashMap<Integer, User> users) {
		this.users = users;
	}

	public HashMap<User, BikeRental> getUserRentals() {
		return userRentals;
	}

	public void setUserRentals(HashMap<User, BikeRental> userRentals) {
		this.userRentals = userRentals;
	}

	public HashMap<User, RidePlan> getUserRidePlans() {
		return userRidePlans;
	}

	public void setUserRidePlans(HashMap<User, RidePlan> userRidePlans) {
		this.userRidePlans = userRidePlans;
	}
	
}
