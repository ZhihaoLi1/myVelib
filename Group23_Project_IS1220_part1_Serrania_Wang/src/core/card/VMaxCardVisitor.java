package core.card;

import java.time.Duration;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.rentals.BikeRental;

/**
 * Implementation of CardVisitor for users with a VMax card. <br>
 * Allows calculation of the price of a bike rental for this type of card. <br>
 * With a VMax card, the price is as follows: <br>
 * - 0€ for the first hour then 1€/hour for any bike <br>
 * Before returning the price to pay, we try to use the card's time credit as
 * much as possible to lower the price.
 * 
 * Implements CardVisitor <br>
 * Extends CardWithTimeCreditVisitor
 * 
 * @see CardVisitor
 * @see CardWithTimeCreditVisitor
 * @author matto
 */
public class VMaxCardVisitor extends CardWithTimeCreditVisitor implements CardVisitor {

	// Constructor

	protected VMaxCardVisitor() {
		super();
	}

	// Core methods

	/**
	 * Calculates the price of a bike rental and updates the bike rental with the
	 * price and time credit used.
	 * 
	 * @param rental
	 *            the BikeRental whose price is being calculated
	 * @return the price of the ride
	 * @throws InvalidBikeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent of return dates are given
	 */
	@Override
	public double visit(BikeRental rental) throws InvalidBikeException, InvalidDatesException {
		Bike bike = rental.getBike();
		if (rental.getRentDate() == null || rental.getReturnDate() == null || rental.getTimeSpent() < 0) {
			throw new InvalidDatesException(rental);
		}
		long nMinutes = Duration.between(rental.getRentDate(), rental.getReturnDate()).toMinutes();
		
		int timeCreditUsed = 0;
		int remainingTimeCredit = getTimeCredit();

		if (bike instanceof MechBike || bike instanceof ElecBike) {
			// Check if we can lower the price using the time credit
			if ((nMinutes / 60.0 > 1) && (nMinutes % 60) <= remainingTimeCredit) {
				remainingTimeCredit -= ((int) (nMinutes % 60));
				timeCreditUsed += (int) (nMinutes % 60);
				nMinutes -= (nMinutes % 60);
			}
			while ((nMinutes / 60.0 > 1 && remainingTimeCredit >= 60)) {
				remainingTimeCredit -= 60;
				timeCreditUsed += 60;
				nMinutes -= 60;
			}
			rental.setTimeCreditUsed(timeCreditUsed);
			
			if (nMinutes <= 60) {
				rental.setPrice(0);
				return rental.getPrice();
			}
			rental.setPrice((nMinutes - 60) / 60 + ((nMinutes % 60 == 0) ? 0 : 1));
			return rental.getPrice();
		} else {
			throw new InvalidBikeException(rental);
		}
	}
}
