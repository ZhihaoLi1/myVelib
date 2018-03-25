package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import core.BikeType;
import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;
import core.card.VMaxCardVisitor;
import core.rentals.BikeRental;
import core.utils.DateParser;

/**
 * Test class for VMaxCardVisitor
 * 
 * @author matto
 *
 */
public class VMaxCardVisitorTest {

	/**
	 * Test that adding time credit does work.
	 */
	@Test
	public void testAddTimeCredit() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		try {
			vMaxCard.addTimeCredit(10);
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown when it shouldn't have");
		}
		assertEquals(vMaxCard.getTimeCredit(), 10);
	}

	/**
	 * Test that adding or removing negative time credit throws an
	 * IllegalArgumentException.
	 */
	@Test
	public void whenNegativeTimeCreditGivenThenThrowException() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		try {
			vMaxCard.addTimeCredit(-10);
			fail("Should have thrown IllegalArgumentException exception");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
		try {
			vMaxCard.removeTimeCredit(-10);
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
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		try {
			vMaxCard.addTimeCredit(10);
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown when it shouldn't have");
		}
		try {
			vMaxCard.removeTimeCredit(20);
			fail("Should have thrown IllegalArgumentException exception");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}

	@Test
	public void testGetTimeCredit() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		assertEquals(vMaxCard.getTimeCredit(), 0);
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
		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");

		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike(BikeType.MECH);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental mRental = new BikeRental(mBike, rentDate);

		try {
			mRental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));
			assertTrue(mRental.accept(vMaxCard) == 1);

			vMaxCard.addTimeCredit(40);
			mRental.setReturnDate(DateParser.parse("01/01/2000 00:50:00"));
			assertTrue(mRental.accept(vMaxCard) == 0);
			assertTrue(vMaxCard.getTimeCredit() == 40);

			mRental.setReturnDate(DateParser.parse("01/01/2000 01:30:00"));
			assertTrue(mRental.accept(vMaxCard) == 0);
			assertTrue(vMaxCard.getTimeCredit() == 10);

			vMaxCard.addTimeCredit(50);
			mRental.setReturnDate(DateParser.parse("01/01/2000 01:00:00"));
			assertTrue(mRental.accept(vMaxCard) == 0);
			assertTrue(vMaxCard.getTimeCredit() == 60);
		} catch (InvalidBikeException e) {
			fail("Invalid bike type given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException thrown when it shouldn't have");
		}

		vMaxCard = new VMaxCardVisitor();
		Bike eBike = null;
		try {
			eBike = new BikeFactory().createBike(BikeType.ELEC);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental eRental = new BikeRental(eBike, rentDate);

		try {
			eRental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));
			assertTrue(eRental.accept(vMaxCard) == 1);

			vMaxCard.addTimeCredit(80);
			eRental.setReturnDate(DateParser.parse("01/01/2000 00:50:00"));
			assertTrue(eRental.accept(vMaxCard) == 0);
			assertTrue(vMaxCard.getTimeCredit() == 80);

			eRental.setReturnDate(DateParser.parse("01/01/2000 01:30:00"));
			assertTrue(eRental.accept(vMaxCard) == 0);
			assertTrue(vMaxCard.getTimeCredit() == 50);

			vMaxCard.addTimeCredit(10);
			eRental.setReturnDate(DateParser.parse("01/01/2000 01:00:00"));
			assertTrue(eRental.accept(vMaxCard) == 0);
			assertTrue(vMaxCard.getTimeCredit() == 60);
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
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();

		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike(BikeType.MECH);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental rental = new BikeRental(mBike, null);
		try {
			rental.accept(vMaxCard);
			fail("Visitor should have thrown InvalidDatesException");
		} catch (InvalidDatesException e) {
			assertTrue(true);
		} catch (InvalidBikeException e) {
			fail("Visitor should have thrown InvalidDatesException");
		}
	}

	/**
	 * Test that giving a rental without a bike throws an IllegalArgumentException.
	 */
	@Test
	public void whenInvalidBikeIsGivenThenThrowException() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();

		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");
		BikeRental rental = new BikeRental(null, rentDate);
		rental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));

		try {
			rental.accept(vMaxCard);
			fail("Should have thrown InvalidBikeException");
		} catch (InvalidDatesException e) {
			fail("Should have thrown InvalidBikeException");
		} catch (InvalidBikeException e) {
			assertTrue(true);
		}
	}

}
