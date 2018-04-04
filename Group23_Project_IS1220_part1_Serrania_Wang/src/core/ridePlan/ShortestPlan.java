package core.ridePlan;

import java.util.HashMap;

import core.Network;
import core.station.Station;
import core.user.User;
import utils.Point;

/**
 * source and destination stations are chosen so that the total distance of the
 * trip, including the walking distance (to reach the source station from the
 * starting point and to reach the destination point from the destination
 * station) is minimal. tl;dr = Total distance between source and source station
 * and destination station and destination is the shortest
 * 
 * @author animato
 *
 */
public class ShortestPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, String bikeType,
			Network n) throws NoValidStationFoundException {
		HashMap<Integer,Station> stations = n.getStations();

		Station sourceStation = null;
		Station destStation = null;

		double minimumDistance = Double.MAX_VALUE;

		// different possible pairs
		for (Station s1 : stations.values()) {
			// source station
			if (!s1.hasCorrectBikeType(bikeType) || !s1.getOnline())
				continue;
			for (Station s2 : stations.values()) {
				if (!s2.getOnline() || s2.equals(s1) || s2.isFull())
					continue;
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
		if (sourceStation == null || destStation == null) {
			throw new NoValidStationFoundException(n, "SHORTEST");
		}
		return new RidePlan(source, destination, sourceStation, destStation, "SHORTEST", bikeType, n);
	}
}
