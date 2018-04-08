package core.station;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import core.bike.Bike;
import core.rentals.BikeRental;
import utils.Point;

/**
 * Represents a station. A station is a place on the map where a user can find
 * parking slots (which may or may not have a bike), and a terminal used to rent
 * and return bikes.
 * 
 * @author matto
 *
 */
public abstract class Station extends Observable {
	private final int id;

	// time credit added to card to a bike is returned to this station.
	private final int bonusTimeCreditOnReturn;

	// A station has multiple parking slots
	private ArrayList<ParkingSlot> parkingSlots = new ArrayList<ParkingSlot>();

	private Point coordinates;
	private Boolean online;

	// Observers
	private Set<Observer> observers = new HashSet<Observer>();

	// Statistics
	private StationStats stats;

	/**
	 * Create a station with the given number of parking slots, coordinates and
	 * online status
	 * 
	 * @param numberOfParkingSlots
	 *            the number of parking slots of the station
	 * @param coordinates
	 *            the coordinates of the station
	 * @param online
	 *            the initial online status of the station
	 * @param bonusTimeCreditOnReturn
	 *            the amount of time credit the station gives when returning a bike
	 */
	public Station(int numberOfParkingSlots, Point coordinates, Boolean online, int bonusTimeCreditOnReturn) {
		super();
		this.id = StationIDGenerator.getInstance().getNextIDNumber();
		for (int i = 0; i < numberOfParkingSlots; i++) {
			this.parkingSlots.add(new ParkingSlot());
		}
		this.coordinates = coordinates;
		this.online = online;
		this.bonusTimeCreditOnReturn = bonusTimeCreditOnReturn;
		this.stats = new StationStats(this);
	}

	// Core methods

	/**
	 * Gives the number of bikes in the station of the given bikeType
	 * 
	 * @param bikeType
	 *            - the type of bike we want to check
	 * @return int - the number of bikes in the station of the given bikeType
	 */
	public int getNumberOfBikes(String bikeType) {
		int t = 0;
		for (int i = 0; i < parkingSlots.size(); i++) {
			ParkingSlot ps = parkingSlots.get(i);
			if (ps.hasBike() && ps.getBike().getType() == bikeType && ps.isWorking()) {
				t += 1;
			}
		}
		return t;
	}

	/**
	 * Rents a bike of the given bike type at a given time
	 * 
	 * @param bikeType
	 * @return the rented bike, or null if no bike was found
	 */
	public Bike rentBike(String bikeType, LocalDateTime date) throws BikeNotFoundException, OfflineStationException {
		// verify if station is online
		if (!this.online)
			throw new OfflineStationException(this.id);

		// find appropriate bike in station;
		Bike b = null;

		for (ParkingSlot ps : this.getParkingSlots()) {
			if (ps.isWorking() && ps.hasBike() && ps.getBike().getType().equals(bikeType.toUpperCase())) {
				try {
					synchronized (ps) {
						b = ps.getBike();
						ps.emptyBike(date);
					}
					// increment station statistics
					this.getStats().incrementTotalRentals();
					break;
				} catch (OccupiedParkingSlotException e) {
					continue;
				}

			}
		}
		// If no bike was found, throw BikeNotFoundException
		if (b == null) {
			throw new BikeNotFoundException(this.id, bikeType);
		}
		return b;
	}

	/**
	 * Returns a bike at a given time The returning of the bike is different given
	 * the station type (take into consideration the timeCredit or not), so this is
	 * an abstract method
	 * 
	 * @param bikeRental
	 *            the bike which is returned
	 * @param date
	 *            the date at which the bike is returned
	 * @throws FullStationException
	 *             when station is full
	 */
	public void returnBike(BikeRental bikeRental, LocalDateTime date)
			throws FullStationException, OfflineStationException {
		if (!this.online)
			throw new OfflineStationException(this.id);
		if (this.isFull())
			throw new FullStationException(this);
		// loop over stations and place bike the first empty slot
		addBike(bikeRental.getBike(), date);

	};

	// Observer pattern methods

	/**
	 * Add user to the list of observers of the station
	 * 
	 * @param o
	 *            the observer user to add
	 */
	@Override
	public void addObserver(Observer o) {
		if (this.observers.add(o)) {
			// System.out.println(o.toString() + " is observing station S" + this.id);
		} else {
			// System.out.println( o.toString() + " is already observing this station S" +
			// this.id);
		}
	}

	/**
	 * Remove user to the list of observers of the station
	 * 
	 * @param o
	 *            the observer to remove
	 */
	@Override
	public void deleteObserver(Observer o) {
		if (this.observers.remove(o)) {
			// System.out.println("Observer" + o.toString() + "stopped observing this
			// station" + this.id);
		} else {
			// System.out.println("User is not observing this station S" + this.id);
		}
	}

	/**
	 * Notify all observers
	 */
	@Override
	public void notifyObservers() {
		Set<Observer> copyObservers = new HashSet<Observer>(observers);
		for (Observer observer : copyObservers) {
			observer.update(this, null);
		}
	}

	// Display methods

	/**
	 * Calculate occupation rate over a given time period.
	 * 
	 * @param startDate
	 * @param endDate
	 */
	private double computeOccupationRate(LocalDateTime startDate, LocalDateTime endDate)
			throws InvalidTimeSpanException {
		return stats.getOccupationRate(startDate, endDate);
	}

	/**
	 * Gives a String representation of the statistics of the occupation rate over a
	 * given time period
	 * 
	 * @param startDate
	 * @param endDate
	 */
	public String displayOccupationRate(LocalDateTime startDate, LocalDateTime endDate)
			throws InvalidTimeSpanException {
		return "Occupation rate between " + startDate + " and " + endDate + ": "
				+ computeOccupationRate(startDate, endDate);
	}

	/**
	 * Gives a String representation of the statistics of the station (total returns
	 * and rentals)
	 * 
	 * @return a String representing the stats
	 */
	public String displayStats() {
		return "Stats of Station " + this.id + ":\n" + stats.toString();
	}

	// Getters / Setters

	public int getId() {
		return id;
	}

	public ArrayList<ParkingSlot> getParkingSlots() {
		return parkingSlots;
	}

	public Point getCoordinates() {
		return coordinates;
	}

	public Boolean getOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		if (!online)
			notifyObservers();
		this.online = online;
	}

	public Set<Observer> getObservers() {
		return observers;
	}

	public int getBonusTimeCreditOnReturn() {
		return bonusTimeCreditOnReturn;
	}

	public StationStats getStats() {
		return stats;
	}

	/**
	 * Adds a bike to the first empty slot that it finds
	 * 
	 * @param b
	 *            the bike to add
	 * @param date
	 *            the date at which the bike is added
	 * @return true if bike was added, false if not
	 */
	public boolean addBike(Bike b, LocalDateTime date) {
		synchronized (this) {
			for (int i = 0; i < parkingSlots.size(); i++) {
				try {
					ParkingSlot ps = parkingSlots.get(i);
					synchronized (ps) {
						// Throws OccupiedParkingSlotException if bike couldn't be set
						ps.setBike(b, date);
						// If the station is full after adding the bike, notification should be sent to
						// users
						if (isFull()) {
							notifyObservers();
						}
						return true;
					}
				} catch (OccupiedParkingSlotException e) {
					continue;
				}
			}
			return false;
		}
	}

	/**
	 * Tells if the station is full
	 * 
	 * @return boolean - true if station is full, false if not
	 */
	public Boolean isFull() {
		for (ParkingSlot ps : parkingSlots) {
			if (ps.isWorking() && !ps.hasBike()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tells if station has a bike of the given type
	 * 
	 * @param bikeType
	 *            the type of bike we want to check
	 * @return true if a bike of type bikeType is present, false if not
	 */
	public boolean hasCorrectBikeType(String bikeType) {
		for (ParkingSlot ps : parkingSlots) {
			if (ps.hasBike() && ps.getBike().getType().equals(bikeType.toUpperCase()) && ps.isWorking()) {
				return true;
			}
		}
		return false;
	}

	// Equality check methods

	@Override
	public boolean equals(Object o) {
		if (o instanceof Station) {
			Station s = (Station) o;
			if (s.getId() == this.id) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		String s = "Station [Id: " + this.id + "\n";
		s += "Parking Slots: " + this.parkingSlots.toString() + "\n";
		s += "Coordinates: " + this.coordinates.toString() + "\n";
		s += "Online: " + this.online + "]";
		return s;

	}
}
