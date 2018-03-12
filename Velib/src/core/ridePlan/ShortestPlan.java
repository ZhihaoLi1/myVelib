package core.ridePlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import core.User;
import core.point.Point;
import core.station.Station;

/**
 * Total distance between source -> source station -> destination station -> destination is the shortest
 * @author animato
 *
 */
public class ShortestPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String policy, String bikeType, HashMap<Integer, Station> stations) throws Exception {
		
		Station sourceStation = null;
		Station destStation = null;
		double minimumDistance = 1000000000; // FIXME
		// different possible pairs 
		for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
			// source station
		    Integer stationId1 = entry.getKey();
		    Station s1 = entry.getValue();
		    if(bikeType == "elec") {
			    if(!s1.hasElecBike()) continue;
		    } else if(bikeType == "mech") {
			    if(!s1.hasMechBike()) continue;
		    }
			for (Map.Entry<Integer, Station> entry2 : stations.entrySet()) {
				// dest Station 				
			    Integer stationId2 = entry2.getKey();
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
