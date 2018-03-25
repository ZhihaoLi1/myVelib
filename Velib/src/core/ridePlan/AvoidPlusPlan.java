package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.Network;
import core.point.Point;
import core.station.PlusStation;
import core.station.Station;
import user.User;

/**
 * the source, respectively the destination, station, for a ride should as close
 * as possible to the starting, respectively to the destination, location of the
 * ride. <br>
 * the destination station cannot be a “plus” station
 * 
 * @author animato
 *
 */

public class AvoidPlusPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String bikeType, Network n) throws NoValidStationFoundException {
		
		HashMap<Integer,Station> stations = n.getStations();
		Station sourceStation = null;
		Station destStation = null;

		double minimumSourceDistance = Double.MAX_VALUE;
		double minimumDestDistance = Double.MAX_VALUE;

		for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
			Station s = entry.getValue();
			if (!s.getOnline()) continue;
			double sourceDistance = s.getCoordinates().distance(source);
			double destinationDistance = s.getCoordinates().distance(destination);
			if (sourceDistance < minimumSourceDistance && s.hasCorrectBikeType(bikeType)) {
				sourceStation = s;
				minimumSourceDistance = sourceDistance;
			}
			if (destinationDistance < minimumDestDistance && !s.isFull() && !(s instanceof PlusStation)) {
				destStation = s;
				minimumDestDistance = destinationDistance;
			}
		}

		if (sourceStation == null || destStation == null) {
			throw new NoValidStationFoundException(n, RidePlanPolicyName.AVOID_PLUS);
		}

		return new RidePlan(source, destination, sourceStation, destStation, RidePlanPolicyName.AVOID_PLUS, bikeType, n);
	}
}
