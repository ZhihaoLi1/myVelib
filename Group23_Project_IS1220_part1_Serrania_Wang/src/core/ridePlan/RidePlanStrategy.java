package core.ridePlan;

import core.Network;
import core.bike.InvalidBikeTypeException;
import core.user.User;
import core.utils.Point;


/**
 * RidePlan interface
 * 
 * Different classes that implement RidePlanStrategy returns a ride plan given the input parameters. 
 * 
 * @author animato
 *
 */
public interface RidePlanStrategy {
	/**
	 * 
	 * @param source
	 * @param destination
	 * @param user
	 * @param bikeType
	 * @param n
	 * @return RidePlan
	 * @throws NoValidStationFoundException
	 * @throws InvalidBikeTypeException
	 */
	public RidePlan planRide(Point source, Point destination, User user, String bikeType,
			Network n) throws NoValidStationFoundException, InvalidBikeTypeException;
}
