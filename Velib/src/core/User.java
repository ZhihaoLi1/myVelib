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

public class User implements Observer {
	private final int id; 
	private String name; 
	private Point coordinates;
	private CardVisitor card;
	private BikeRental bikeRental;
	private RidePlan ridePlan;
	// Statistics of user 
	private int totalRides;
	private int totalTimeCredits;
	private int totalCharges;
	private int totalTimeSpent; // in minutes
	
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
	}

	/**
	 * Notify user if given station is no longer online / no more parking slots left
	 * user then tells the network send notification to CLI
	 */
	@Override
	public void update(Observable o, Object arg) {
		if (!(o instanceof Station)) throw new IllegalArgumentException("user update needs station as observable input.");
		this.ridePlan.getNetwork().notifyStationFull((Station) o);
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

	public int getTotalRides() {
		return totalRides;
	}

	public void incrementTotalRides() {
		this.totalRides += 1;
	}

	public int getTotalTimeCredits() {
		return totalTimeCredits;
	}

	public void addTotalTimeCredits(int timeCredits) {
		this.totalTimeCredits += timeCredits;
	}

	public int getTotalCharges() {
		return totalCharges;
	}

	public void addTotalCharges(int charges) {
		this.totalCharges += charges;
	}

	public int getTotalTimeSpent() {
		return totalTimeSpent;
	}

	public void setTotalTimeSpent(int totalTimeSpent) {
		this.totalTimeSpent = totalTimeSpent;
	}

	public BikeRental getBikeRental() {
		return bikeRental;
	}

	public void setBikeRental(BikeRental bikeRental) throws Exception {
		if (this.bikeRental != null) throw new Exception("User already has a ongoing bike rental");
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
	
}
