package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.User;
import core.point.Point;
import core.station.Station;

/**
 * source and destination stations are chosen so that the total time of
 * the trip, including the walking part of the trip is minimal, assuming that the average
 * walking speed is 4 Km/h while the average bicycle-riding speed is 15 Km/h, for
 * mechanical bikes, and 20 Km/h for electrical bikes
 * @author animato
 *
 */
public class FastestPlan implements RidePlanStrategy{

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String bikeType, HashMap<Integer, Station> stations) throws Exception {
		double walkingSpeed = 4; // km/h
		double bikeSpeed = 0;
		if(bikeType == "elec") {
			bikeSpeed = 20;
		} else if (bikeType == "mech") {
			bikeSpeed = 15;
		}
		if (stations.isEmpty()) throw new Exception("No stations are found.");
		
		Station sourceStation = null;
		Station destStation = null;
		Double minimumTime = Double.MAX_VALUE; //hours
		
		// different possible pairs 
		for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
			// source station
		    Station s1 = entry.getValue();
		    if(bikeType == "elec") {
			    if(!s1.hasElecBike()) continue;
		    } else if(bikeType == "mech") {
			    if(!s1.hasMechBike()) continue;
		    }
			for (Map.Entry<Integer, Station> entry2 : stations.entrySet()) {
				// dest Station 				
			    Station s2 = entry2.getValue();
			    if (!s2.equals(s1) && !s2.isFull()) {
				    double totalTime = 0;
				    totalTime += s1.getCoordinates().distance(source)/walkingSpeed;
				    totalTime += s1.getCoordinates().distance(s2.getCoordinates())/bikeSpeed;
				    totalTime += s2.getCoordinates().distance(destination)/walkingSpeed;
				    
				    if (totalTime < minimumTime) {
				    		sourceStation = s1;
				    		destStation = s2;
				    		minimumTime = totalTime;
				    }
			    }
			}
		}
		
		if (sourceStation == null || destStation == null) {
			throw new Exception("No appropriate stations found !");
		}
		return new RidePlan(source, destination, sourceStation, destStation, "fastest", bikeType);
	}

}
