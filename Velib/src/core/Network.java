package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.point.Point;
import core.rentals.BikeRental;
import core.ridePlan.RidePlan;
import core.ridePlan.RidePlanStrategy;
import core.station.PlusStation;
import core.station.StandardStation;
import core.station.Station;

public class Network implements RidePlanStrategy {
	
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

	public void addStation(Station station) throws Exception {
		if(station == null) {
			throw new Exception("Station is null in addStation");
		}
		// verify that the coordinates of station is within the network. 
		this.stations.put(station.getId(), station);
	}
		
	@Override
	public RidePlan planRide(Point source, Point destination, User user, String policy, String bikeType) {
		// TODO Auto-generated method stub
		return null;
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
