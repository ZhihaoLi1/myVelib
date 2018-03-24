package core.ridePlan;

import java.util.HashMap;
import java.util.Map;

import core.BikeType;
import core.Network;
import core.PolicyName;
import core.User;
import core.point.Point;
import core.station.Station;

/**
 * source and destination stations are chosen so that the total time of the
 * trip, including the walking part of the trip is minimal, assuming that the
 * average walking speed is 4 Km/h while the average bicycle-riding speed is 15
 * Km/h, for mechanical bikes, and 20 Km/h for electrical bikes
 * 
 * @author animato
 *
 */
// FIXME: Reorganize Javadoc

public class FastestPlan implements RidePlanStrategy {

	@Override
	public RidePlan planRide(Point source, Point destination, User user, BikeType bikeType,
			HashMap<Integer, Station> stations, Network n) throws Exception {
		double walkingSpeed = 4; // km/h
		double bikeSpeed = 0;
		switch (bikeType) {
		case ELEC:
			bikeSpeed = 20;
		case MECH:
			bikeSpeed = 15;
		}

		if (stations.isEmpty())
			throw new Exception("No stations are found.");

		Station sourceStation = null;
		Station destStation = null;
		Double minimumTime = Double.MAX_VALUE; // hours

		// different possible pairs
		for (Station s1 : stations.values()) {
			// source station
			if (!s1.getOnline() || !s1.hasCorrectBikeType(bikeType))
				continue;
			for (Station s2 : stations.values()) {
				// dest Station
				if (!s2.getOnline())
					continue;
				if (!s2.equals(s1) && !s2.isFull()) {
					double totalTime = 0;
					totalTime += s1.getCoordinates().distance(source) / walkingSpeed;
					totalTime += s1.getCoordinates().distance(s2.getCoordinates()) / bikeSpeed;
					totalTime += s2.getCoordinates().distance(destination) / walkingSpeed;

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
		return new RidePlan(source, destination, sourceStation, destStation, PolicyName.FASTEST, bikeType, n);
	}

}
