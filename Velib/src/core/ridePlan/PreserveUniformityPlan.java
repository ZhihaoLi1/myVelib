package core.ridePlan;

import java.util.HashMap;

import core.User;
import core.point.Point;
import core.station.Station;

/**
 * avoid “plus” stations: with this policy the destination station cannot be a “plus” station
 * @author animato
 *
 */
public class PreserveUniformityPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String bikeType, HashMap<Integer, Station> stations) {
		// TODO Auto-generated method stub
		return null;
	}

}
