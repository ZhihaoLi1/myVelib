package core.ridePlan;

import core.Network;
import core.bike.InvalidBikeTypeException;
import core.station.Station;
import utils.Point;

/**
 * A copy of the results of a planned ride by the network
 * 
 */
public class RidePlan {
	private Point source;
	private Point destination;
	private Station sourceStation;
	private Station destinationStation;
	private String policy;
	private String bikeType;
	private Network network;

	// Constructor

	public RidePlan(Point source, Point destination, Station sourceStation, Station destinationStation, String policy,
			String bikeType, Network network) {
		super();
		this.source = source;
		this.destination = destination;
		this.sourceStation = sourceStation;
		this.destinationStation = destinationStation;
		this.policy = policy;
		this.bikeType = bikeType;
		this.network = network;
	}

	// Core methods

	/**
	 * Calculates the time it takes to go from the source to the destination while
	 * following the ride plan.
	 * 
	 * @return the time (in hours) it takes to get from the source to the
	 *         destination
	 * @throws InvalidBikeTypeException
	 *             if the bikeType of the rental is not recognized by the system
	 */
	public int approximateTime() throws InvalidBikeTypeException {
		double walkingSpeed = 4; // km/h
		double bikeSpeed = 0;
		switch (bikeType) {
		case "ELEC":
			bikeSpeed = 20;
			break;
		case "MECH":
			bikeSpeed = 15;
			break;
		default:
			throw new InvalidBikeTypeException(bikeType);
		}

		double totalTime = 0;
		totalTime += sourceStation.getCoordinates().distance(source) / walkingSpeed;
		totalTime += sourceStation.getCoordinates().distance(destinationStation.getCoordinates()) / bikeSpeed;
		totalTime += destinationStation.getCoordinates().distance(destination) / walkingSpeed;

		return (int) Math.floor(totalTime * 60);
	}

	// Getters / Setters

	public Point getSource() {
		return source;
	}

	public void setSource(Point source) {
		this.source = source;
	}

	public Point getDestination() {
		return destination;
	}

	public void setDestination(Point destination) {
		this.destination = destination;
	}

	public Station getSourceStation() {
		return sourceStation;
	}

	public void setSourceStation(Station sourceStation) {
		this.sourceStation = sourceStation;
	}

	public Station getDestinationStation() {
		return destinationStation;
	}

	public void setDestinationStation(Station destinationStation) {
		this.destinationStation = destinationStation;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getBikeType() {
		return bikeType;
	}

	public void setBikeType(String bikeType) {
		this.bikeType = bikeType;
	}

	public Network getNetwork() {
		return network;
	}

	public void setNetwork(Network network) {
		this.network = network;
	}

	// Equality check methods

	@Override
	public boolean equals(Object o) {
		if (o instanceof RidePlan) {
			RidePlan rp = (RidePlan) o;
			if (this.source.equals(rp.getSource()) && this.destination.equals(rp.getDestination())
					&& this.sourceStation.equals(rp.getSourceStation())
					&& this.destinationStation.equals(rp.getDestinationStation())
					&& this.network.equals(rp.getNetwork()) && this.policy.equals(rp.policy)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String rp = "Source : " + source.toString() + "\n";
		rp += "Destination : " + destination.toString() + "\n";
		rp += "Source station : " + sourceStation.getCoordinates().toString() + "\n";
		rp += "Source station Id: " + sourceStation.getId() + "\n";
		rp += "Destination station: " + destinationStation.getCoordinates().toString() + "\n";
		rp += "Destination station Id: " + destinationStation.getId() + "\n";
		rp += "Policy : " + policy.toString().toLowerCase() + "\n";
		return rp;
	}
}