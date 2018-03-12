package core.card;

import static org.junit.Assert.fail;

import java.time.Duration;
import java.time.LocalDateTime;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.rentals.BikeRental;
import core.utils.DateParser;

public class VLibreCardVisitor extends CardWithTimeCreditVisitor {

	public VLibreCardVisitor(){
		super();
	}

	/**
	 * Calculates the price of the bike rental passed as a parameter.
	 * <br>
	 * With a VLibre card, the price is as follows:
	 * <br>
	 * - 0€ for the first hour then 1€/hour for mechanical bikes
	 * <br>
	 * - 1€ for the first hour then 2€/hour for electrical bikes
	 * Before returning the price to pay, we try to use the card's time credit as much as possible to lower the price.
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
			
		} else if (bike instanceof ElecBike) {
			// Check if we can lower the price using the time credit
			try {
				while ((nMinutes % 60) <= getTimeCredit()) {
					removeTimeCredit((int) (nMinutes % 60));
					nMinutes -= (nMinutes % 60);
				}
			} catch (NegativeTimeCreditGivenException e) {
				;
			} catch (NegativeTimeCreditLeftException e) {
				;
			}
			if (nMinutes == 0) {
				return 0;
			}
			if (nMinutes <= 60) {
				return 1;
			}
			return 1 + 2 * ((nMinutes - 60) / 60 + ((nMinutes % 60 == 0) ? 0 : 1));
			
		} else {
			throw new InvalidBikeTypeException(bike);
		}
	}	
	
	public static void main(String[] args) {
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		try {
			vLibreCard.addTimeCredit(85);
		} catch (NegativeTimeCreditGivenException e) {
			;
		}
		Bike mBike = new ElecBike();
		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");
		BikeRental mRental = new BikeRental(mBike, rentDate);

		mRental.setReturnDate(DateParser.parse("01/01/2000 02:30:00"));
		
		try {
			System.out.println(mRental.accept(vLibreCard));
			System.out.println(vLibreCard.getTimeCredit());
		} catch (InvalidBikeTypeException e) {
			fail("Invalid bike type given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		}
	}
}
