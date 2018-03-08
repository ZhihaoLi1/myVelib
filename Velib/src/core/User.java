package core;

import java.util.Observable;
import java.util.Observer;

import core.card.Card;
import core.card.NoCard;
import core.point.Point;
import core.station.IDGenerator;

public class User implements Observer {
	private final int id; 
	private String name; 
	private Point coordinates;
	private Card card;
	
	// Statistics of user 
	private int totalRides;
	private int totalTimeCredits;
	private int totalCharges;
	private int totalTimeSpent; // in minutes
	
	public User(String name) {
		id = IDGenerator.getInstance().getNextIDNumber();
		this.card = new NoCard();
	}
	
	public User(String name, Card card) {
		id = IDGenerator.getInstance().getNextIDNumber();
		this.card = card;
	}

	public User(String name, Point coordinates, Card card) {
		id = IDGenerator.getInstance().getNextIDNumber();
		this.coordinates = coordinates;
		this.card = card;
	}

	/**
	 * Notify user if given station is no longer online / no more parking slots left
	 */
	@Override
	public void update(Observable o, Object arg) {
	
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

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
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
	
}
