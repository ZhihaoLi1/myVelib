package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.BikeType;
import core.bike.InvalidBikeTypeException;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;
import core.card.NoCardVisitor;
import core.rentals.BikeRental;
import core.utils.DateParser;

/**
 * Test class for NoCardVisitor
 * 
 * @author matto
 *
 */
public class NoCardVisitorTest {

	/**
	 * Test that adding time credit does work.
	 */
	@Test
	public void testAddTimeCredit() {
		NoCardVisitor noCard = new NoCardVisitor();
		noCard.addTimeCredit(10);
		assertEquals(noCard.getTimeCredit(), 0);
	}

	@Test
	public void testGetTimeCredit() {
		NoCardVisitor noCard = new NoCardVisitor();
		assertEquals(noCard.getTimeCredit(), 0);
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
		NoCardVisitor noCard = new NoCardVisitor();

		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike(BikeType.MECH);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}

		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");
		BikeRental mRental = new BikeRental(mBike, rentDate);

		try {
			mRental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));
			assertTrue(mRental.accept(noCard) == 2);

			mRental.setReturnDate(DateParser.parse("01/01/2000 00:50:00"));
			assertTrue(mRental.accept(noCard) == 1);

			mRental.setReturnDate(DateParser.parse("01/01/2000 01:30:00"));
			assertTrue(mRental.accept(noCard) == 2);
		} catch (InvalidBikeException e) {
			fail("Invalid bike given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		}

		Bike eBike = null;
		try {
			eBike = new BikeFactory().createBike(BikeType.ELEC);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental eRental = new BikeRental(eBike, rentDate);
		try {
			eRental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));
			assertTrue(eRental.accept(noCard) == 4);

			eRental.setReturnDate(DateParser.parse("01/01/2000 00:50:00"));
			assertTrue(eRental.accept(noCard) == 2);

			eRental.setReturnDate(DateParser.parse("01/01/2000 01:30:00"));
			assertTrue(eRental.accept(noCard) == 4);
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
		NoCardVisitor noCard = new NoCardVisitor();

		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike(BikeType.MECH);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}

		BikeRental rental = new BikeRental(mBike, null);
		try {
			rental.accept(noCard);
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
		NoCardVisitor noCard = new NoCardVisitor();

		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");
		BikeRental rental = new BikeRental(null, rentDate);
		rental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));

		try {
			rental.accept(noCard);
			fail("Visitor should have thrown InvalidBikeTypeException");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (InvalidBikeException e) {
			assertTrue(true);
		}
	}

}
