package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.User;
import core.point.Point;
import core.station.Station;

/**
 * source and destination stations are chosen so that the total distance
 * of the trip, including the walking distance (to reach the source station from the
 * starting point and to reach the destination point from the destination station) is
 * minimal.
 * tl;dr = Total distance between source -> source station -> destination station -> destination is the shortest
 * @author animato
 *
 */
public class ShortestPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String bikeType, HashMap<Integer, Station> stations) throws Exception {
		
		Station sourceStation = null;
		Station destStation = null;
		
		if (stations.isEmpty()) throw new Exception("No stations are found.");

		double minimumDistance = Double.MAX_VALUE; 
		
		// different possible pairs 
		for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
			// source station
		    Station s1 = entry.getValue();
		    if(!s1.hasCorrectBikeType(bikeType)) continue;
		    for (Map.Entry<Integer, Station> entry2 : stations.entrySet()) {
				// dest Station 				
			    Station s2 = entry2.getValue();
			    if (!s2.equals(s1) && !s2.isFull()) {
				    double totalDistance = 0;
				    totalDistance += s1.getCoordinates().distance(source);
				    totalDistance += s1.getCoordinates().distance(s2.getCoordinates());
				    totalDistance += s2.getCoordinates().distance(destination);
				    
				    if (totalDistance < minimumDistance) {
				    		sourceStation = s1;
				    		destStation = s2;
				    		minimumDistance = totalDistance;
				    }
			    }
			}
		}
		if (sourceStation == null || destStation == null) {
			throw new Exception("No appropriate stations found !");
		}
		return new RidePlan(source, destination, sourceStation, destStation, "shortest", bikeType);
	}
}
