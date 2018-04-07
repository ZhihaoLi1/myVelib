package core.test.junit.card;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitor;
import core.card.CardVisitorFactory;
import core.card.InvalidBikeException;
import core.card.InvalidCardTypeException;
import core.card.InvalidDatesException;
import core.card.NoCardVisitor;
import core.rentals.BikeRental;
import utils.DateParser;

/**
 * Test class for NoCardVisitor
 * 
 * @author matto
 *
 */
public class NoCardVisitorTest {

	CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
	BikeFactory bikeFactory = new BikeFactory();
	
	CardVisitor card;
	Bike eBike, mBike;

	@Before
	public void initialize() throws InvalidCardTypeException, InvalidBikeTypeException {
		card = cardVisitorFactory.createCard("NO_CARD");
		mBike = bikeFactory.createBike("MECH");
		eBike = bikeFactory.createBike("ELEC");
	}

	/**
	 * Test that adding time credit does work.
	 */
	@Test
	public void testAddTimeCredit() {
		card.addTimeCredit(10);
		assertEquals(((NoCardVisitor) card).getTimeCredit(), 0);
	}

	/**
	 * Verify that price calculation does work <br>
	 * <br>
	 * Test scenarios: <br>
	 * <br>
	 * 1. <br>
	 * Calculate the price of a 2-hour ride with a mech bike: should be 2€. <br>
	 * Calculate the price of a 50-min ride with a mech bike: should be 1€. <br>
	 * Calculate the price of a 1,5-hour ride with a mech bike: should be 2€. <br>
	 * 
	 * 2. <br>
	 * Calculate the price of a 2-hour ride with an elec bike: should be 4€. <br>
	 * Calculate the price of a 50-min ride with an elec bike: should be 2€ <br>
	 * Calculate the price of a 1,5-hour ride with an elec bike: should be 4€. <br>
	 */
	@Test
	public void testVisit() throws InvalidBikeException, InvalidDatesException {
		LocalDateTime rentDate = DateParser.parse("01/01/2000T00:00:00");
		BikeRental mRental = new BikeRental(mBike, rentDate);

		mRental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));
		assertTrue(mRental.accept(card) == 2);

		mRental.setReturnDate(DateParser.parse("01/01/2000T00:50:00"));
		assertTrue(mRental.accept(card) == 1);

		mRental.setReturnDate(DateParser.parse("01/01/2000T01:30:00"));
		assertTrue(mRental.accept(card) == 2);

		BikeRental eRental = new BikeRental(eBike, rentDate);
		
		eRental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));
		assertTrue(eRental.accept(card) == 4);

		eRental.setReturnDate(DateParser.parse("01/01/2000T00:50:00"));
		assertTrue(eRental.accept(card) == 2);

		eRental.setReturnDate(DateParser.parse("01/01/2000T01:30:00"));
		assertTrue(eRental.accept(card) == 4);
	}

	/**
	 * Test that giving a rental whose dates are not filled throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void whenInvalidDatesAreGivenThenThrowException() throws InvalidBikeException {
		BikeRental rental = new BikeRental(mBike, null);
		try {
			rental.accept(card);
			fail("Visitor should have thrown InvalidDatesException");
		} catch (InvalidDatesException e) {
			assertTrue(true);
		}
	}

}
