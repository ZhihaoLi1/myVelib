package core.ridePlan;

import java.util.HashMap;

import core.User;
import core.point.Point;
import core.station.Station;

public interface RidePlanStrategy {
	public RidePlan planRide(Point source, Point destination, User user, String policy, String bikeType, HashMap<Integer, Station> stations) throws Exception;
}
