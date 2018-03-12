package core.card;

import java.time.Duration;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.rentals.BikeRental;

public class VMaxCardVisitor extends CardWithTimeCreditVisitor {	

	public VMaxCardVisitor(){
		super();
	}

	@Override
	/**
	 * Calculates the price of the bike rental passed as a parameter.
	 * <br>
	 * With a VMax card, the price is as follows:
	 * <br>
	 * - 0€ for the first hour then 1€/hour for any bike
	 * <br>
	 * Before returning the price to pay, we try to use the card's time credit as much as possible to lower the price.
	 * @param rental - the BikeRental whose price is being calculated
	 * @return (double) the price of the rental
	 * @throws InvalidBikeTypeException if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException if invalid rent of return dates are given
	 */
	public double visit(BikeRental rental) throws InvalidBikeTypeException, InvalidDatesException {
		Bike bike = rental.getBike();
		if (rental.getRentDate() == null || rental.getReturnDate() == null) {
			throw new InvalidDatesException(rental.getRentDate(), rental.getReturnDate());
		}
		long nMinutes = Duration.between(rental.getRentDate(), rental.getReturnDate()).toMinutes();
		
		if (bike instanceof MechBike || bike instanceof ElecBike) {
			// Check if we can lower the price using the time credit
			try {
				while ((nMinutes / 60 > 0) && (nMinutes % 60) <= getTimeCredit()) {
					removeTimeCredit((int) (nMinutes % 60));
					nMinutes -= (nMinutes % 60);
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
			throw new InvalidBikeTypeException(bike);
		}
	}	
}
