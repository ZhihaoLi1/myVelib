package core;

import java.util.ArrayList;
import java.util.HashMap;
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
	 * @param side
	 * @param percentageOfBikes
	 * @param percentageOfPlusStations
	 * @param percentageOfElecBikes
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
			if (i < numberOfStations*percentageOfPlusStations) {
				Station s = new PlusStation(numberOfParkingSlotsPerStation.get(i), coordinates);
				this.stations.put(s.getId(), s);
			} else {
				Station s = new StandardStation(numberOfParkingSlotsPerStation.get(i), coordinates);
				this.stations.put(s.getId(), s);
			}
		}
		
		// create a bike , add it to a random station
		// Some are mech, some are elec
		int totalNumberOfParkingSlots = numberOfParkingSlotsPerStation.stream().mapToInt(Integer::intValue).sum();
		for (int i=0; i < totalNumberOfParkingSlots*percentageOfBikes; i++) {
			if (i < totalNumberOfParkingSlots*percentageOfBikes*percentageOfElecBikes) {
				Bike b = new ElecBike();
				this.stations.get(ThreadLocalRandom.current().nextInt(0, numberOfStations)).addBike(b);
			} else {
				Bike b = new MechBike();
				this.stations.get(ThreadLocalRandom.current().nextInt(0, numberOfStations)).addBike(b);
			}
		}

	}
	
	@Override
	public RidePlan planRide(Point source, Point destination, User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
