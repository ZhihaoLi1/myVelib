package core.ridePlan;

import core.point.Point;
import core.station.Station;

public interface RidePlanStrategy {
	public RidePlan planRide(Point source, Point destination, User user);
}
