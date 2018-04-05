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

	public static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
	public CardVisitor card;

	@Before
	public void initializeCard() {
		try {
			card = cardVisitorFactory.createCard("NO_CARD");
		} catch (InvalidCardTypeException e) {
			fail();
		}
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
	public void testVisit() {
		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike("MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}

		LocalDateTime rentDate = DateParser.parse("01/01/2000T00:00:00");
		BikeRental mRental = new BikeRental(mBike, rentDate);

		try {
			mRental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));
			assertTrue(mRental.accept(card) == 2);

			mRental.setReturnDate(DateParser.parse("01/01/2000T00:50:00"));
			assertTrue(mRental.accept(card) == 1);

			mRental.setReturnDate(DateParser.parse("01/01/2000T01:30:00"));
			assertTrue(mRental.accept(card) == 2);
		} catch (InvalidBikeException e) {
			fail("Invalid bike given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		}

		Bike eBike = null;
		try {
			eBike = new BikeFactory().createBike("ELEC");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental eRental = new BikeRental(eBike, rentDate);
		try {
			eRental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));
			assertTrue(eRental.accept(card) == 4);

			eRental.setReturnDate(DateParser.parse("01/01/2000T00:50:00"));
			assertTrue(eRental.accept(card) == 2);

			eRental.setReturnDate(DateParser.parse("01/01/2000T01:30:00"));
			assertTrue(eRental.accept(card) == 4);
		} catch (InvalidBikeException e) {
			fail("Invalid bike given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		}
	}

	/**
	 * Test that giving a rental whose dates are not filled throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void whenInvalidDatesAreGivenThenThrowException() {
		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike("MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}

		BikeRental rental = new BikeRental(mBike, null);
		try {
			rental.accept(card);
			fail("Visitor should have thrown InvalidDatesException");
		} catch (InvalidDatesException e) {
			assertTrue(true);
		} catch (InvalidBikeException e) {
			fail("Invalid bike given to visitor");
		}
	}

	/**
	 * Test that giving a rental without a bike throws an IllegalArgumentException.
	 */
	@Test
	public void whenInvalidBikeIsGivenThenThrowException() {
		LocalDateTime rentDate = DateParser.parse("01/01/2000T00:00:00");
		BikeRental rental = new BikeRental(null, rentDate);
		rental.setReturnDate(DateParser.parse("01/01/2000T02:00:00"));

		try {
			rental.accept(card);
			fail("Visitor should have thrown InvalidBikeTypeException");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (InvalidBikeException e) {
			assertTrue(true);
		}
	}

}
