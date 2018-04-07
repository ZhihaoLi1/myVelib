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
import core.card.VMaxCardVisitor;
import core.rentals.BikeRental;
import utils.DateParser;

/**
 * Test class for VMaxCardVisitor
 * 
 * @author matto
 *
 */
public class VMaxCardVisitorTest {

	CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
	BikeFactory bikeFactory = new BikeFactory();
	
	CardVisitor card;
	Bike eBike, mBike;

	@Before
	public void initialize() throws InvalidCardTypeException, InvalidBikeTypeException {
		card = cardVisitorFactory.createCard("VMAX_CARD");
		mBike = bikeFactory.createBike("MECH");
		eBike = bikeFactory.createBike("ELEC");
	}
	
	/**
	 * Test that adding time credit does work.
	 */
	@Test
	public void testAddTimeCredit() {
		card.addTimeCredit(10);
		assertEquals(card.getTimeCredit(), 10);
	}

	/**
	 * Test that adding or removing negative time credit throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void whenNegativeTimeCreditGivenThenThrowException() {
		try {
			card.addTimeCredit(-10);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			card.removeTimeCredit(-10);
			fail("Should have thrown IllegalArgumentException");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Test that removing time credit higher than the card's total time credit
	 * throws an IllegalArgumentException.
	 */
	@Test
	public void whenNegativeTimeCreditLeftThenThrowException() {
		card.addTimeCredit(10);
		
		try {
			card.removeTimeCredit(20);
			fail("Should have thrown IllegalArgumentException exception");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	/**
	 * Verify that price calculation does work <br>
	 * <br>
	 * Test scenarios: <br>
	 * <br>
	 * 1. The card has at first no time credit. <br>
	 * Calculate the price of a 2-hour ride with a mech bike: should be 1€. <br>
	 * Then add 40 minutes of time credit to the card. <br>
	 * Calculate the price of a 50-min ride with a mech bike: should be 0€, no time
	 * credit removal required. <br>
	 * Calculate the price of a 1,5-hour ride with a mech bike: should be 1€, 0€
	 * with 30 minutes of time credit removed (10 minutes left). <br>
	 * Then add 50 minutes of time credit to the card (60 minutes total) <br>
	 * Finally calculate the price of a 1-hour ridewith a mech bike: should be 0€,
	 * no time credit removal required. <br>
	 * 
	 * 2. The card has at first no time credit. <br>
	 * Calculate the price of a 2-hour ride with an elec bike: should be 1€. <br>
	 * Then add 80 minutes of time credit to the card. <br>
	 * Calculate the price of a 50-min ride with an elec bike: should be 0€, no time
	 * credit removal required. <br>
	 * Calculate the price of a 1,5-hour ride with an elec bike: should be 1€, 0€
	 * with 30 minutes of time credit removed (50 minutes left). <br>
	 * Then add 10 minutes of time credit to the card (60 minutes total) <br>
	 * Finally calculate the price of a 1-hour ride with an elec bike: should be 0€,
	 * no time credit removal required.
	 */
	@Test
	public void testVisit() throws InvalidBikeException, InvalidDatesException {
		LocalDateTime rentDate = DateParser.parse("01/01/2000T00:00:00");
		BikeRental mRental = new BikeRental(mBike, rentDate);

		mRental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));
		assertTrue(mRental.accept(card) == 1);

		card.addTimeCredit(40);
		mRental.setReturnDate(DateParser.parse("01/01/2000T00:50:00"));
		assertTrue(mRental.accept(card) == 0);
		assertTrue(mRental.getTimeCreditUsed() == 0);

		mRental.setReturnDate(DateParser.parse("01/01/2000T01:30:00"));
		assertTrue(mRental.accept(card) == 0);
		assertTrue(mRental.getTimeCreditUsed() == 30);
		card.removeTimeCredit(0);

		card.addTimeCredit(50);
		mRental.setReturnDate(DateParser.parse("01/01/2000T01:00:00"));
		assertTrue(mRental.accept(card) == 0);
		assertTrue(mRental.getTimeCreditUsed() == 0);

		BikeRental eRental = new BikeRental(eBike, rentDate);

		// Reset time credit to start over from a clean card
		card.removeTimeCredit(card.getTimeCredit());

		eRental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));
		assertTrue(eRental.accept(card) == 1);

		card.addTimeCredit(80);
		eRental.setReturnDate(DateParser.parse("01/01/2000T00:50:00"));
		assertTrue(eRental.accept(card) == 0);
		assertTrue(eRental.getTimeCreditUsed() == 0);

		eRental.setReturnDate(DateParser.parse("01/01/2000T01:30:00"));
		assertTrue(eRental.accept(card) == 0);
		assertTrue(eRental.getTimeCreditUsed() == 30);
		card.removeTimeCredit(30);

		card.addTimeCredit(10);
		eRental.setReturnDate(DateParser.parse("01/01/2000T01:00:00"));
		assertTrue(eRental.accept(card) == 0);
		assertTrue(eRental.getTimeCreditUsed() == 0);
	}

	/**
	 * Test that giving a rental whose dates are not filled throws an
	 * InvalidDatesException.
	 */
	@Test
	public void whenInvalidDatesAreGivenThenThrowException() throws InvalidBikeException{
		BikeRental rental = new BikeRental(mBike, null);
		try {
			rental.accept(card);
			fail("Visitor should have thrown InvalidDatesException");
		} catch (InvalidDatesException e) {
			assertTrue(true);
		}
	}
}
