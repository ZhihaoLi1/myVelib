package core.card;

import java.time.Duration;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.rentals.BikeRental;

/**
 * Implementation of CardVisitor for users with a VLibreCard. <br>
 * Allows calculation of the price of a bike rental for this type of card. <br>
 * With a VLibre card, the price is as follows: <br>
 * - 0€ for the first hour then 1€/hour for mechanical bikes <br>
 * - 1€ for the first hour then 2€/hour for electrical bikes <br>
 * Before returning the price to pay, we try to use the card's time credit as
 * much as possible to lower the price. <br>
 * 
 * Implements CardVisitor <br>
 * Extends CardWithTimeCreditVisitor
 * 
 * @see CardVisitor
 * @see CardWithTimeCreditVisitor
 * @author matto
 */
public class VLibreCardVisitor extends CardWithTimeCreditVisitor implements CardVisitor {

	public VLibreCardVisitor() {
		super();
	}

	/**
	 * Calculates the price of the bike rental passed as a parameter. <br>
	 * With a VLibre card, the price is as follows: <br>
	 * - 0€ for the first hour then 1€/hour for mechanical bikes <br>
	 * - 1€ for the first hour then 2€/hour for electrical bikes Before returning
	 * the price to pay, we try to use the card's time credit as much as possible to
	 * lower the price.
	 * 
	 * @param rental
	 *            - the BikeRental whose price is being calculated
	 * @return (double) the price of the rental
	 * @throws InvalidBikeTypeException
	 *             if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException
	 *             if invalid rent of return dates are given
	 */
	@Override
	public double visit(BikeRental rental) throws InvalidBikeException, InvalidDatesException {
		Bike bike = rental.getBike();
		if (rental.getRentDate() == null || rental.getReturnDate() == null) {
			throw new InvalidDatesException(rental);
		}
		long nMinutes = Duration.between(rental.getRentDate(), rental.getReturnDate()).toMinutes();

		if (bike instanceof MechBike) {
			// Check if we can lower the price using the time credit
			if ((nMinutes / 60 > 0) && (nMinutes % 60) <= getTimeCredit()) {
				removeTimeCredit((int) (nMinutes % 60));
				nMinutes -= (nMinutes % 60);
			}
			while ((nMinutes / 60 > 0 && getTimeCredit() >= 60)) {
				removeTimeCredit(60);
				nMinutes -= 60;
			}
			if (nMinutes <= 60) {
				return 0;
			}
			return (nMinutes - 60) / 60 + ((nMinutes % 60 == 0) ? 0 : 1);

		} else if (bike instanceof ElecBike) {
			// Check if we can lower the price using the time credit
			if ((nMinutes % 60) <= getTimeCredit()) {
				removeTimeCredit((int) (nMinutes % 60));
				nMinutes -= (nMinutes % 60);
			}
			while ((nMinutes > 0 && getTimeCredit() >= 60)) {
				removeTimeCredit(60);
				nMinutes -= 60;
			}
			if (nMinutes == 0) {
				return 0;
			}
			if (nMinutes <= 60) {
				return 1;
			}
			return 1 + 2 * ((nMinutes - 60) / 60 + ((nMinutes % 60 == 0) ? 0 : 1));

		} else {
			throw new InvalidBikeException(rental);
		}
	}
}
