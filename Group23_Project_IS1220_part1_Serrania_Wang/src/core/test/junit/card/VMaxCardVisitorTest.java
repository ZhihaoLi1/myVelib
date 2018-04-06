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

	public static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
	public VMaxCardVisitor card;

	@Before
	public void initializeCard() {
		try {
			card = (VMaxCardVisitor) cardVisitorFactory.createCard("VMAX_CARD");
		} catch (InvalidCardTypeException e) {
			fail();
		}
	}

	/**
	 * Test that adding time credit does work.
	 */
	@Test
	public void testAddTimeCredit() {
		try {
			card.addTimeCredit(10);
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown when it shouldn't have");
		}
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
			fail("Should have thrown IllegalArgumentException exception");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			card.removeTimeCredit(-10);
			fail("Should have thrown IllegalArgumentException exception");
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
		try {
			card.addTimeCredit(10);
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown when it shouldn't have");
		}
		try {
			card.removeTimeCredit(20);
			fail("Should have thrown IllegalArgumentException exception");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetTimeCredit() {
		assertEquals(card.getTimeCredit(), 0);
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
	public void testVisit() {
		LocalDateTime rentDate = DateParser.parse("01/01/2000T00:00:00");

		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike("MECH");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental mRental = new BikeRental(mBike, rentDate);

		try {
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
		} catch (InvalidBikeException e) {
			fail("InvalidBikeException thrown");
		} catch (InvalidDatesException e) {
			fail("InvalidDatesException thrown");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown");
		}

		Bike eBike = null;
		try {
			eBike = new BikeFactory().createBike("ELEC");
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental eRental = new BikeRental(eBike, rentDate);

		try {
			// Reset the card's time credit
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
		} catch (InvalidBikeException e) {
			fail("Invalid bike type given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown when it shouldn't have");
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
			fail("Visitor should have thrown InvalidDatesException");
		}
	}
}
