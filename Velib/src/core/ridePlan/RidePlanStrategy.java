package core.ridePlan;

import java.util.HashMap;

import core.BikeType;
import core.User;
import core.point.Point;
import core.station.Station;

public interface RidePlanStrategy {
	public RidePlan planRide(Point source, Point destination, User user, BikeType bikeType, HashMap<Integer, Station> stations) throws Exception;
}
