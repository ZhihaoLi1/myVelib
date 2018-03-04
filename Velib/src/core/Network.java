package core;

import java.util.HashMap;

import core.point.Point;
import core.rentals.BikeRental;
import core.ridePlan.RidePlan;
import core.ridePlan.RidePlanStrategy;
import core.station.Station;

public class Network implements RidePlanStrategy {
	private HashMap<Integer, Station> stations; 
	private HashMap<Integer, User> users;
	private HashMap<User, BikeRental> userRentals;
	private HashMap<User, RidePlan> userRidePlans;

	public Network(){
		
	}
	
	@Override
	public RidePlan planRide(Point source, Point destination, User user) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
