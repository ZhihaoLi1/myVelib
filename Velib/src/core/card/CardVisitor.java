package core.card;

import core.rentals.BikeRental;

public interface CardVisitor {
	public double visit(BikeRental rental) throws InvalidBikeTypeException, InvalidDatesException;
	public void addTimeCredit(int timeCredit) throws NegativeTimeCreditGivenException;
}
