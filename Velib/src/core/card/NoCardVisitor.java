package core.card;

import java.time.Duration;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.rentals.BikeRental;

/**
 * Implementation of CardVisitor for users without any card.
 * 
 * @author matto
 */
public class NoCardVisitor implements CardVisitor {

	public NoCardVisitor(){
		super();
	}
	
	/**
	 * Calculates the price of the bike rental passed as a parameter.
	 * <br>
	 * Without a card, the price is as follows:
	 * <br>
	 * - 1€/hour for mechanical bikes
	 * <br>
	 * - 2€/hour for electrical bikes
	 * 
	 * @param rental - the BikeRental whose price is being calculated
	 * @return (double) the price of the rental
	 * @throws InvalidBikeTypeException if an unidentified type of bike (or null) is given
	 * @throws InvalidDatesException if invalid rent of return dates are given
	 */
	@Override
	public double visit(BikeRental rental) throws InvalidBikeTypeException, InvalidDatesException {
		Bike bike = rental.getBike();
		if (rental.getRentDate() == null || rental.getReturnDate() == null) {
			throw new InvalidDatesException(rental.getRentDate(), rental.getReturnDate());
		}
		long nMinutes = Duration.between(rental.getRentDate(), rental.getReturnDate()).toMinutes();
		if (bike instanceof MechBike) {
			return nMinutes / 60 + ((nMinutes % 60 == 0) ? 0 : 1);
		} else if (bike instanceof ElecBike) {
			return 2 * (nMinutes / 60 + ((nMinutes % 60 == 0) ? 0 : 1));
		} else {
			throw new InvalidBikeException(bike);
		}
	}
	
	// Getters / Setters
	public int getTimeCredit() {
		return 0;
	}
	
	@Override
	public void addTimeCredit(int timeCredit) {
		// do nothing
	}
}
