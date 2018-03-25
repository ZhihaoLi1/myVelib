package core.rentals;

import java.time.LocalDateTime;

import core.bike.Bike;
import core.card.CardVisitor;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;

/**
 * Contains information of a rental Bike, date of rent, and user who rented it.
 * Is a concrete visitor for the visitor pattern used to calculate the price of
 * a rental.
 * 
 * @author animato
 *
 */
public class BikeRental implements Rental {

	private final Bike bike;
	private final LocalDateTime rentDate;
	private LocalDateTime returnDate;
	private int timeSpent; // in minutes

	public BikeRental(Bike bike, LocalDateTime rentDate) {
		this.bike = bike;
		this.rentDate = rentDate;
		this.returnDate = null;
	}

	// Passes information to visitor to calculate final cost.
	@Override
	public double accept(CardVisitor visitor) throws InvalidBikeException, InvalidDatesException {
		return visitor.visit(this);
	}

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

	@Override
	public String toString() {
		return "BikeRental: [Bike: " + bike + ", rentDate: " + rentDate + ", returnDate: " + returnDate
				+ ", timeSpent: " + timeSpent + "]";
	}

	public int getTimeSpent() {
		return timeSpent;
	}

	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}
}