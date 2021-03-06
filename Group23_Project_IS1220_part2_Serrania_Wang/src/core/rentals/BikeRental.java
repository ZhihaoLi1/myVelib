package core.rentals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import core.bike.Bike;
import core.card.CardVisitor;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;

/**
 * Contains information about a bike rental (Bike and rent date). <br>
 * Other information is added to it when the bike rental ends (date of return,
 * time spent, price of the ride, timeCredit used and timeCredit added) <br>
 * Concrete visitor for the visitor pattern used to calculate the price of a rental.
 * 
 * @see Rental
 * @author animato
 *
 */
public class BikeRental implements Rental {

	private final Bike bike; // Not null by design
	private final LocalDateTime rentDate;
	private LocalDateTime returnDate;
	private long timeSpent; // in minutes, inferred from returnDate and rentDate
	private double price; // price of the rental, in euros
	private int timeCreditAdded; // time credit added when returning to the station
	private int timeCreditUsed; // time credit used to pay part of the rental, in minutes

	// Constructor

	public BikeRental(Bike bike, LocalDateTime rentDate) throws IllegalArgumentException {
		if (bike == null) {
			throw new IllegalArgumentException("Impossible to create a BikeRental without a bike");
		}
		this.bike = bike;
		this.rentDate = rentDate;
		// At start, returnDate is not set. It will be set when the bike is returned.
		// Same for timeSpent, price and timeCreditUsed.
	}

	// Core methods

	/**
	 * Passes itself to the visitor so that its cost can be calculated.
	 * 
	 * @param visitor
	 *            The CardVisitor which will be used to compute costs
	 * @return the price of the course (after time credit is deducted)
	 * @throws InvalidBikeException
	 * @throws InvalidDatesException
	 */
	@Override
	public double accept(CardVisitor visitor) throws InvalidBikeException, InvalidDatesException {
		return visitor.visit(this);
	}

	// Getters / Setters

	public Bike getBike() {
		return bike;
	}

	public LocalDateTime getRentDate() {
		return rentDate;
	}

	public LocalDateTime getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDateTime returnDate) {
		this.returnDate = returnDate;
	}

	public long getTimeSpent() {
		if (timeSpent <= 0) {
			calculateTimeSpent();
		}
		return timeSpent;
	}

	public void calculateTimeSpent() {
		if (returnDate != null && rentDate != null) {
			this.timeSpent = rentDate.until(returnDate, ChronoUnit.MINUTES);
		}
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getTimeCreditUsed() {
		return this.timeCreditUsed;
	}

	public void setTimeCreditUsed(int timeCreditUsed) {
		this.timeCreditUsed = timeCreditUsed;
	}
	
	public int getTimeCreditAdded() {
		return timeCreditAdded;
	}

	public void setTimeCreditAdded(int timeCreditAdded) {
		this.timeCreditAdded = timeCreditAdded;
	}

	// Equality check methods

	@Override
	public boolean equals(Object o) {
		if (o instanceof BikeRental) {
			BikeRental other = (BikeRental) o;
			return this.getBike().equals(other.getBike()) && this.getRentDate().equals(other.getRentDate())
			// Needed because returnDate could not be set yet
					&& ((this.getReturnDate() == null && other.getReturnDate() == null)
							|| this.getReturnDate().equals(other.getReturnDate()))
					&& this.getPrice() == other.getPrice() && this.getTimeSpent() == other.getTimeSpent()
					&& this.getTimeCreditUsed() == other.getTimeCreditUsed();
		}
		return false;
	}

	@Override
	public String toString() {
		String s = "BikeRental [Bike: " + bike + ", rentDate: " + rentDate;
		if (returnDate != null) {
			s += ", returnDate: " + returnDate;
			s += ", timeSpent: " + timeSpent;
			s += ", price: " + price;
			s += ", timeCreditUsed: " + timeCreditUsed;
		}
		s+= "]";
		return s;
	}
}