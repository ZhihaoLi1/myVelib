package core;

import java.util.Observable;
import java.util.Observer;

import core.card.CardVisitor;
import core.card.NoCardVisitor;
import core.point.Point;
import core.rentals.BikeRental;
import core.ridePlan.RidePlan;
import core.station.IDGenerator;
import core.station.Station;

// FIXME: Javadoc
public class User implements Observer {
	private final int id;
	private String name;
	private Point coordinates;
	private CardVisitor card;
	private BikeRental bikeRental;
	private RidePlan ridePlan;
	private UserStats stats;

	public User(String name) {
		this(name, null, new NoCardVisitor());
	}

	public User(String name, CardVisitor card) {
		this(name, null, card);
	}

	public User(String name, Point coordinates, CardVisitor card) {
		id = IDGenerator.getInstance().getNextIDNumber();
		this.coordinates = coordinates;
		this.card = card;
		this.name = name;
		this.stats = new UserStats();
	}

	/**
	 * Notify user if given station is no longer online / no more parking slots left
	 * user then tells the network send notification to CLI
	 */
	// FIXME: I don't think we need Object arg. Moreover, this should be reversed:
	// if (o instanceof Station) { ... }, else do nothing.
	// no exception should be thrown
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof Station))
			throw new IllegalArgumentException("user update needs station as observable input.");
		Station s = (Station) o;
		this.ridePlan.getNetwork().notifyStationFull(s);
		// remove from list of observers in station
		s.deleteObserver(this);
		// reset ride plan
		this.ridePlan = null;
	}

	/**
	 * Display statistics of user
	 */
	public void displayBalance() {

	}

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

	public void setBikeRental(BikeRental bikeRental) throws Exception {
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
		this.ridePlan = ridePlan;
	}

	public int getId() {
		return id;
	}

	public UserStats getStats() {
		return stats;
	}

	public void setStats(UserStats stats) {
		this.stats = stats;
	}

}
