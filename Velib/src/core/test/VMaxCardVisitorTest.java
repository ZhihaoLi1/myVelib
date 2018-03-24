package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import core.BikeType;
import core.bike.Bike;
import core.bike.BikeFactory;
import core.bike.ElecBike;
import core.bike.InvalidBikeTypeException;
import core.bike.MechBike;
import core.card.InvalidBikeException;
import core.card.InvalidDatesException;
import core.card.NegativeTimeCreditGivenException;
import core.card.NegativeTimeCreditLeftException;
import core.card.VMaxCardVisitor;
import core.rentals.BikeRental;
import core.utils.DateParser;

/**
 * @author matto
 *
 */
public class VMaxCardVisitorTest {
	
	@Test
	public void testAddTimeCredit() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		try {
			vMaxCard.addTimeCredit(10);
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
		}
		assertEquals(vMaxCard.getTimeCredit(), 10);
	}
	
	@Test
	public void whenNegativeTimeCreditGivenThenThrowException() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		try {
			vMaxCard.addTimeCredit(-10);
			fail("Should have thrown NegativeTimeCreditGivenException exception");
		} catch (NegativeTimeCreditGivenException e) {
			assertTrue(true);
		}
		try {
			vMaxCard.removeTimeCredit(-10);
			fail("Should have thrown NegativeTimeCreditGivenException exception");
		} catch (NegativeTimeCreditGivenException e) {
			assertTrue(true);
		} catch (NegativeTimeCreditLeftException e) {
			fail("Should have thrown NegativeTimeCreditGivenException exception");
		}
	}
	
	@Test
	public void whenNegativeTimeCreditLeftThenThrowException() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		try {
			vMaxCard.addTimeCredit(10);
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
		}
		try {
			vMaxCard.removeTimeCredit(20);
			fail("Should have thrown NegativeTimeCreditLeftException exception");
		} catch (NegativeTimeCreditGivenException e) {
			fail("Should have thrown NegativeTimeCreditLeftException exception");
		} catch (NegativeTimeCreditLeftException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetTimeCredit() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		assertEquals(vMaxCard.getTimeCredit(), 0);
	}
	
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
			assertTrue(vMaxCard.getTimeCredit() == 0);
		} catch (InvalidBikeException e) {
			fail("Invalid bike type given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
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
			assertTrue(vMaxCard.getTimeCredit() == 0);
		} catch (InvalidBikeException e) {
			fail("Invalid bike type given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
		}
	}
	
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
	
	@Test
	public void whenInvalidBikeIsGivenThenThrowException() {
		VMaxCardVisitor vMaxCard = new VMaxCardVisitor();
		
		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");
		BikeRental rental = new BikeRental(null, rentDate);
		rental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));

		try {
			rental.accept(vMaxCard);
			fail("Should have thrown InvalidBikeTypeException");
		} catch (InvalidDatesException e) {
			fail("Should have thrown InvalidBikeTypeException");
		} catch (InvalidBikeException e) {
			assertTrue(true);
		}
	}

}
