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
 * @author matto
 */
public class VMaxCardVisitor extends CardWithTimeCreditVisitor {

	public VMaxCardVisitor() {
		super();
	}

	/**
	 * Calculates the price of a bike rental.
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
			throw new InvalidDatesException(rental.getRentDate(), rental.getReturnDate());
		}
		long nMinutes = Duration.between(rental.getRentDate(), rental.getReturnDate()).toMinutes();

		if (bike instanceof MechBike || bike instanceof ElecBike) {
			// Check if we can lower the price using the time credit
			try {
				if ((nMinutes / 60 > 0) && (nMinutes % 60) <= getTimeCredit()) {
					removeTimeCredit((int) (nMinutes % 60));
					nMinutes -= (nMinutes % 60);
				}
				while ((nMinutes / 60 > 0 && getTimeCredit() >= 60)) {
					removeTimeCredit(60);
					nMinutes -= 60;
				}
			} catch (NegativeTimeCreditGivenException e) {
				;
			} catch (NegativeTimeCreditLeftException e) {
				;
			}
			if (nMinutes <= 60) {
				return 0;
			}
			return (nMinutes - 60) / 60 + ((nMinutes % 60 == 0) ? 0 : 1);
		} else {
			throw new InvalidBikeException(bike);
		}
	}
}
