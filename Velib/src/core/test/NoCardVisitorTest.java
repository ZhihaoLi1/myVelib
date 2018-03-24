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
import core.card.NoCardVisitor;
import core.rentals.BikeRental;
import core.utils.DateParser;

// FIXME: Comments
public class NoCardVisitorTest {

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
