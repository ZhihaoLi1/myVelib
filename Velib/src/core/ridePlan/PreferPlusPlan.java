package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.Network;
import core.bike.BikeType;
import core.point.Point;
import core.station.PlusStation;
import core.station.Station;
import user.User;

/**
 * the destination station should be a “plus” station (given a “plus” station no
 * further away than 10% of the distance of the closest station to the
 * destination location exists). If no such a “plus” station exists then this
 * policy behaves normally ()
 * 
 * @author animato
 *
 */
public class PreferPlusPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, BikeType bikeType,
			Network n) throws Exception {
		HashMap<Integer,Station> stations = n.getStations();

		Station sourceStation = null;
		Station destStation = null;

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
			if (destinationDistance < minimumDestDistance && !s.isFull()) {
				destStation = s;
				minimumDestDistance = destinationDistance;
			}
		}

		if (sourceStation == null || destStation == null) {
			throw new NoValidStationFoundException(n, RidePlanPolicyName.PREFER_PLUS);
		}

		// find plus station for destination
		double minimumPlusDestDistance = 0.1 * minimumDestDistance;
		Station plusDestStation = null;
		if (!(destStation instanceof PlusStation)) {
			for (Map.Entry<Integer, Station> entry : stations.entrySet()) {
				Station s = entry.getValue();
				if (!(s instanceof PlusStation))
					continue;
				double distance = s.getCoordinates().distance(destStation.getCoordinates());
				if (distance < minimumPlusDestDistance) {
					minimumPlusDestDistance = distance;
					plusDestStation = s;
				}
			}
		}

		if (plusDestStation == null) {
			return new RidePlan(source, destination, sourceStation, destStation, RidePlanPolicyName.PREFER_PLUS, bikeType, n);
		} else {
			return new RidePlan(source, destination, sourceStation, plusDestStation, RidePlanPolicyName.PREFER_PLUS, bikeType,
					n);
		}
	}

}
