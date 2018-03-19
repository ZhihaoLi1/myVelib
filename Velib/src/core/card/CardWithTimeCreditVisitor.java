package core.card;

import core.rentals.BikeRental;

public abstract class CardWithTimeCreditVisitor implements CardVisitor {
	private int timeCredit;
	
	public CardWithTimeCreditVisitor() {
		timeCredit = 0;
	}
	
	public abstract double visit(BikeRental rental) throws InvalidBikeTypeException, InvalidDatesException;
	
	// Getters / Setters
	public void addTimeCredit(int timeCredit) throws NegativeTimeCreditGivenException {
		if (timeCredit >=  0) {
			this.timeCredit += timeCredit;
		} else {
			throw new NegativeTimeCreditGivenException(timeCredit);
		}
	};

	public void removeTimeCredit(int timeCredit) throws NegativeTimeCreditGivenException, NegativeTimeCreditLeftException {
		if (timeCredit >=  0) {
			if (this.timeCredit < timeCredit) {
				throw new NegativeTimeCreditLeftException(timeCredit);
			} else {
				this.timeCredit -= timeCredit;
			}
		} else {
			throw new NegativeTimeCreditGivenException(timeCredit);
		}
	};
	
	public int getTimeCredit() {
		return timeCredit;
	}
}
