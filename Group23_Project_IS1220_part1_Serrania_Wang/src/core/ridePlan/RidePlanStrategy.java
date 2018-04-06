package core.ridePlan;

import core.Network;
import core.bike.InvalidBikeTypeException;
import core.user.User;
import utils.Point;

/**
 * RidePlan interface
 * 
 * Interfaces for classes that implement ride planning, which returns a ride
 * plan given the input parameters.
 * 
 * @author animato
 *
 */
public interface RidePlanStrategy {
	/**
	 * Plans a ride on the network n from source point to destination point, for a
	 * given user, with a given bike type.
	 * 
	 * @param source
	 *            the source point of the ride plan
	 * @param destination
	 *            the destination point of the ride plan
	 * @param user
	 *            the user for which the plan is calculated
	 * @param bikeType
	 *            the type of bike the user wants to use
	 * @param n
	 *            the network on which the calculation is made
	 * @return the ride plan for the user
	 * @throws NoValidStationFoundException
	 *             if no source / destination station could be found
	 * @throws InvalidBikeTypeException
	 *             when the bikeType given is not recognized by the system
	 * @throws IllegalArgumentException
	 *             when one of the arguments is null
	 */
	public RidePlan planRide(Point source, Point destination, User user, String bikeType, Network n)
			throws NoValidStationFoundException, InvalidBikeTypeException;
}
