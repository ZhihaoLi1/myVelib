package core.user;

import java.util.Observable;
import java.util.Observer;

import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.rentals.BikeRental;
import core.rentals.OngoingBikeRentalException;
import core.ridePlan.RidePlan;
import core.station.Station;
import utils.Point;

/**
 * User of myVelib Network. A user can observe a station when it has a planned
 * ride whose destination station is this station
 * 
 * @see Observer
 * @author animato
 *
 */
public class User implements Observer {
	private final int id;
	private String name;
	private Point coordinates;
	private CardVisitor card;
	private BikeRental bikeRental;
	private RidePlan ridePlan;
	private UserStats stats;

	// Constructor

	/**
	 * Create a User with a name, given coordinates and card
	 * 
	 * @param name
	 *            the name of the user
	 * @param coordinates
	 * @param card
	 */

	public User(String name, Point coordinates, CardVisitor card) {
		id = UserIDGenerator.getInstance().getNextIDNumber();
		this.coordinates = coordinates;
		this.card = card;
		this.name = name;
		this.stats = new UserStats();
	}

	// Core methods

	/**
	 * Notify user if given station is no longer online / no more parking slots left
	 * user then tells the network send notification to CLI
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof Station))
			throw new IllegalArgumentException("user update needs station as observable input.");
		Station s = (Station) o;
		this.ridePlan.getNetwork().notifyStationFull(this, s);
		this.getRidePlan().getNetwork().notifyObservers("Station with id " + s.getId() + " is full and ride plan for "
				+ this.getName() + " is cancelled. Please create a new one");
		// reset ride plan
		setRidePlan(null);
	}

	/**
	 * Display statistics of user
	 * 
	 * @return the String representation of the user's stats
	 */
	public String displayStats() {
		return "Stats of " + name + ": \n" + stats.toString();
	}

	// Getters / Setters

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public CardVisitor getCard() {
		return card;
	}

	public void setCard(CardVisitor card) {
		this.card = card;
	}

	public BikeRental getBikeRental() {
		return bikeRental;
	}

	public void setBikeRental(BikeRental bikeRental) throws OngoingBikeRentalException {
		if (this.bikeRental != null)
			throw new OngoingBikeRentalException(this);
		this.bikeRental = bikeRental;
	}

	public void resetBikeRental() {
		this.bikeRental = null;
	}

	public RidePlan getRidePlan() {
		return ridePlan;
	}

	public void setRidePlan(RidePlan ridePlan) {
		if (this.ridePlan != null) {
			this.ridePlan.getDestinationStation().deleteObserver(this);
		}
		this.ridePlan = ridePlan;
	}

	public int getId() {
		return id;
	}

	public UserStats getStats() {
		return stats;
	}

	// Equality check methods

	@Override
	public boolean equals(Object o) {
		if (o instanceof User) {
			User other = (User) o;
			return this.getId() == other.getId();
		}
		return false;
	}

	@Override
	public String toString() {
		String bikeRentalString;
		if (bikeRental == null)
			bikeRentalString = "No bike rental";
		else
			bikeRentalString = bikeRental.toString();

		String ridePlanString;
		if (ridePlan == null)
			ridePlanString = "No ride plan";
		else
			ridePlanString = ridePlan.toString();

		return "User [Id: " + id + "\nName: " + name + "\nCard: " + card + "\nCoordinates: " + coordinates
				+ "\nBike rental: " + bikeRentalString + "\nRide plan: " + ridePlanString + "]";
	}

}
