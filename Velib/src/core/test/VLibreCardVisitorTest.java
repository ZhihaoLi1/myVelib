package core.test;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import core.bike.Bike;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.card.InvalidBikeTypeException;
import core.card.InvalidDatesException;
import core.card.NegativeTimeCreditGivenException;
import core.card.NegativeTimeCreditLeftException;
import core.card.VLibreCardVisitor;
import core.rentals.BikeRental;
import core.utils.DateParser;

public class VLibreCardVisitorTest {

	@Test
	public void testAddTimeCredit() {
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		try {
			vLibreCard.addTimeCredit(10);
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
		}
		assertEquals(vLibreCard.getTimeCredit(), 10);
	}
	
	@Test
	public void whenNegativeTimeCreditGivenThenThrowException() {
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		try {
			vLibreCard.addTimeCredit(-10);
			fail("Should have thrown NegativeTimeCreditGivenException exception");
		} catch (NegativeTimeCreditGivenException e) {
			assertTrue(true);
		}
		try {
			vLibreCard.removeTimeCredit(-10);
			fail("Should have thrown NegativeTimeCreditGivenException exception");
		} catch (NegativeTimeCreditGivenException e) {
			assertTrue(true);
		} catch (NegativeTimeCreditLeftException e) {
			fail("Should have thrown NegativeTimeCreditGivenException exception");
		}
	}
	
	@Test
	public void whenNegativeTimeCreditLeftThenThrowException() {
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		try {
			vLibreCard.addTimeCredit(10);
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
		}
		try {
			vLibreCard.removeTimeCredit(20);
			fail("Should have thrown NegativeTimeCreditLeftException exception");
		} catch (NegativeTimeCreditGivenException e) {
			fail("Should have thrown NegativeTimeCreditLeftException exception");
		} catch (NegativeTimeCreditLeftException e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void testGetTimeCredit() {
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		assertEquals(vLibreCard.getTimeCredit(), 0);
	}
	
	@Test
	public void testVisit() {		
		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");

		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		Bike mBike = new MechBike();
		BikeRental mRental = new BikeRental(mBike, rentDate);

		try {
			mRental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));
			assertTrue(mRental.accept(vLibreCard) == 1);
			
			vLibreCard.addTimeCredit(40);
			mRental.setReturnDate(DateParser.parse("01/01/2000 00:50:00"));
			assertTrue(mRental.accept(vLibreCard) == 0);
			assertTrue(vLibreCard.getTimeCredit() == 40);
			
			mRental.setReturnDate(DateParser.parse("01/01/2000 01:30:00"));
			assertTrue(mRental.accept(vLibreCard) == 0);
			assertTrue(vLibreCard.getTimeCredit() == 10);
			
			vLibreCard.addTimeCredit(50);
			mRental.setReturnDate(DateParser.parse("01/01/2000 01:00:00"));
			assertTrue(mRental.accept(vLibreCard) == 0);
			assertTrue(vLibreCard.getTimeCredit() == 0);
		} catch (InvalidBikeException e) {
			fail("Invalid bike type given to visitor");
		} catch (InvalidDatesException e) {
			fail("Invalid dates given to visitor");
		} catch (NegativeTimeCreditGivenException e) {
			fail("NegativeTimeCreditGivenException thrown when it shouldn't have");
		}

		vLibreCard = new VLibreCardVisitor();
		Bike eBike = new ElecBike();
		BikeRental eRental = new BikeRental(eBike, rentDate);
		
		try {
			eRental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));
			assertTrue(eRental.accept(vLibreCard) == 3);
			
			vLibreCard.addTimeCredit(80);
			eRental.setReturnDate(DateParser.parse("01/01/2000 00:50:00"));
			assertTrue(eRental.accept(vLibreCard) == 0);
			assertTrue(vLibreCard.getTimeCredit() == 30);

			eRental.setReturnDate(DateParser.parse("01/01/2000 01:30:00"));
			assertTrue(eRental.accept(vLibreCard) == 1);
			assertTrue(vLibreCard.getTimeCredit() == 0);
			
			vLibreCard.addTimeCredit(60);
			eRental.setReturnDate(DateParser.parse("01/01/2000 01:00:00"));
			assertTrue(eRental.accept(vLibreCard) == 0);
			assertTrue(vLibreCard.getTimeCredit() == 0);
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
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		
		Bike mBike = null;
		try {
			mBike = new BikeFactory().createBike(BikeType.MECH);
		} catch (InvalidBikeTypeException e) {
			fail("InvalidBikeTypeException thrown");
		}
		BikeRental rental = new BikeRental(mBike, null);
		try {
			rental.accept(vLibreCard);
			fail("Visitor should have thrown InvalidDatesException");
		} catch (InvalidDatesException e) {
			assertTrue(true);
		} catch (InvalidBikeException e) {
			fail("Visitor should have thrown InvalidDatesException");
		}
	}
	
	@Test
	public void whenInvalidBikeIsGivenThenThrowException() {
		VLibreCardVisitor vLibreCard = new VLibreCardVisitor();
		
		LocalDateTime rentDate = DateParser.parse("01/01/2000 00:00:00");
		BikeRental rental = new BikeRental(null, rentDate);
		rental.setReturnDate(DateParser.parse("01/01/2000 02:00:00"));

		try {
			rental.accept(vLibreCard);
			fail("Should have thrown InvalidBikeTypeException");
		} catch (InvalidDatesException e) {
			fail("Should have thrown InvalidBikeTypeException");
		} catch (InvalidBikeException e) {
			assertTrue(true);
		}
	}
}
