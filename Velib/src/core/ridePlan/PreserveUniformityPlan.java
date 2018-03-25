package core.ridePlan;

import java.util.HashMap;

import core.BikeType;
import core.Network;
import core.PolicyName;
import core.User;
import core.point.Point;
import core.station.Station;

/**
 * with this policy the choice of the source and destination station is affected
 * by the number of available bikes (at source station) and free slots (at
 * destination). Specifically, let s0 be the closest station to the starting
 * location with at least one available bike of the wanted kind, and sd be the
 * station closest to the destination location with at least one free parking
 * slot. Then if a station s0' whose distance from the starting location is no
 * more than 105% the distance of s0 from the start location has a larger number
 * of bikes (of the wanted kind) than those available at s0 it should be
 * selected in place of s0. Similarly if a station s0d (whose distance from the
 * destination location is at most 105% of the distance of sd from the
 * destination location) has a larger number of free parking slots than sd it
 * should be selected as the destination station in place of sd
 * 
 * @author animato
 *
 */
public class PreserveUniformityPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, BikeType bikeType,
			Network n) throws Exception {

		HashMap<Integer,Station> stations = n.getStations();

		// find the closest stations
		Station sourceStation = null;
		Station destStation = null;

		double minimumSourceDistance = Double.MAX_VALUE;
		double minimumDestDistance = Double.MAX_VALUE;

		for (Station s : stations.values()) {
			if (!s.getOnline())
				continue;
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
			throw new NoValidStationFoundException(n, PolicyName.PRESERVE_UNIFORMITY);
		}

		Station fullerSourceStation = null;
		Station emptierDestStation = null;

		double maxSourceSize = sourceStation.getNumberOfBikes(bikeType);
		double minDestSize = destStation.getNumberOfBikes(bikeType);

		// find closest fuller source station and closest emptier destination station
		for (Station s : stations.values()) {
			double sourceDistance = s.getCoordinates().distance(sourceStation.getCoordinates());
			double destinationDistance = s.getCoordinates().distance(destStation.getCoordinates());
			int numberOfBikes = s.getNumberOfBikes(bikeType);
			if (sourceDistance < 1.05 * minimumSourceDistance && numberOfBikes > maxSourceSize) {
				fullerSourceStation = s;
				maxSourceSize = s.getNumberOfBikes(bikeType);
			}

			if (destinationDistance < 1.05 * minimumDestDistance && numberOfBikes < minDestSize) {
				emptierDestStation = s;
				minDestSize = numberOfBikes;
			}
		}

		if (fullerSourceStation != null)
			sourceStation = fullerSourceStation;
		if (emptierDestStation != null)
			destStation = emptierDestStation;

		return new RidePlan(source, destination, sourceStation, destStation, PolicyName.PRESERVE_UNIFORMITY, bikeType,
				n);
	}

}
