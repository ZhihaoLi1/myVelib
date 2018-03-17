package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.BikeType;
import core.PolicyName;
import core.User;
import core.point.Point;
import core.station.PlusStation;
import core.station.Station;
/**
 * the destination station should be a “plus” station 
 * (given a “plus” station no further away than 10% of the distance of the closest
 * station to the destination location exists). If no such a “plus” station exists then this
 * policy behaves normally ()
 * @author animato
 *
 */
public class PreferPlusPlan implements RidePlanStrategy{

	@Override
	public RidePlan planRide(Point source, Point destination, User user, BikeType bikeType, HashMap<Integer, Station> stations) throws Exception {
		Station sourceStation = null;
		Station destStation = null;
		
		if (stations.isEmpty()) throw new Exception("No stations are found.");

		double minimumSourceDistance = Double.MAX_VALUE; 
		double minimumDestDistance = Double.MAX_VALUE; 
		
		for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
		    Station s = entry.getValue();
		    double sourceDistance = s.getCoordinates().distance(source);
		    double destinationDistance = s.getCoordinates().distance(destination);
		    if (sourceDistance < minimumSourceDistance && s.hasCorrectBikeType(bikeType)) {
	    				sourceStation = s;
	    				minimumSourceDistance = sourceDistance;
		    }
		    if (destinationDistance < minimumDestDistance  && !s.isFull()) {
    				destStation = s;
    				minimumDestDistance = destinationDistance;
		    }
		}

		if (sourceStation == null || destStation == null) {
			throw new Exception("No appropriate stations found !");
		}

		// find plus station for destination 
		double minimumPlusDestDistance = 0.1*minimumDestDistance;
		Station plusDestStation = null;
		if(!(destStation instanceof PlusStation)) {
			for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
			    Station s = entry.getValue();
			    if(!(s instanceof PlusStation)) continue;
			    double distance = s.getCoordinates().distance(destStation.getCoordinates());
			    if (distance < minimumPlusDestDistance) {
			     	minimumPlusDestDistance = distance;
			     	plusDestStation = s;
			    }
			}
		}
		
		if (plusDestStation == null) {
			return new RidePlan(source, destination, sourceStation, destStation, PolicyName.PREFER_PLUS, bikeType);
		} else {
			return new RidePlan(source, destination, sourceStation, plusDestStation, PolicyName.PREFER_PLUS, bikeType);
		}
	}

}
