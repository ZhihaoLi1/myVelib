package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.User;
import core.point.Point;
import core.station.Station;

public class FastestPlan implements RidePlanStrategy{

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String bikeType, HashMap<Integer, Station> stations) throws Exception {
		double walkingSpeed = 4; // km/h
		double mechBikeSpeed = 15;
		double elecBikeSpeed = 20;
		
		Station sourceStation = null;
		Station destStation = null;
		double minimumTime = 1000000000; //hours
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
				    double totalTime = 0;
				    totalTime += s1.getCoordinates().distance(source)/walkingSpeed;
				    if(bikeType == "elec") {
					    totalTime += s1.getCoordinates().distance(s2.getCoordinates())/elecBikeSpeed;
				    } else if (bikeType == "mech"){
					    totalTime += s1.getCoordinates().distance(s2.getCoordinates())/mechBikeSpeed;
				    }
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
		return new RidePlan(source, destination, sourceStation, destStation, "shortest", bikeType);
	}

}
