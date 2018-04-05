package core.rentals;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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
	private long timeSpent; // in minutes
	private double price; // price of the rental, in euros
	private int timeCreditUsed; // time credit used to pay part of the rental, in minutes

	public BikeRental(Bike bike, LocalDateTime rentDate) {
		this.bike = bike;
		this.rentDate = rentDate;
		this.returnDate = null;
	}

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
}