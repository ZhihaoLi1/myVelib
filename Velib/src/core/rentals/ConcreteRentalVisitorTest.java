package core.rentals;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import core.User;
import core.bike.ElecBike;
import core.bike.MechBike;
import core.card.VMaxCard;
import core.card.VlibreCard;

public class ConcreteRentalVisitorTest {
	
	MechBike mb = new MechBike();
	ElecBike eb = new ElecBike();
	Date rentDate = new Date(1000000); // the number of milliseconds since January 1, 1970, 00:00:00 GMT.
	Date returnDate = new Date(1000000+1000*60*60*2); // one hour bike ride
	User mary = new User("Mary"); // by default mary doesn't have a card
	
	@Test
	public void calculatePriceForNoCard() {
		BikeRental br = new BikeRental(mb, rentDate, mary);
		// fake returning the bike
		br.setReturnDate(returnDate);
		double toPay = br.accept(new ConcreteRentalVisitor());
		assertEquals(toPay, 2, 0.01);
		
		BikeRental br2 = new BikeRental(eb, rentDate, mary);
		// fake returning the bike
		br2.setReturnDate(returnDate);
		double toPay2 = br.accept(new ConcreteRentalVisitor());
		assertEquals(toPay2, 4, 0.01);
	}

	@Test
	public void calculatePriceForVlibreCard() {
		mary.setCard(new VlibreCard());
		BikeRental br = new BikeRental(mb, rentDate, mary);
		// fake returning the bike
		br.setReturnDate(returnDate);
		double toPay = br.accept(new ConcreteRentalVisitor());
		assertEquals(toPay, 1, 0.01);
		
		BikeRental br2 = new BikeRental(eb, rentDate, mary);
		// fake returning the bike
		br2.setReturnDate(returnDate);
		double toPay2 = br.accept(new ConcreteRentalVisitor());
		assertEquals(toPay2, 3, 0.01);
	}

	@Test
	public void calculatePriceForVMaxCard() {
		mary.setCard(new VMaxCard());
		BikeRental br = new BikeRental(mb, rentDate, mary);
		// fake returning the bike
		br.setReturnDate(returnDate);
		double toPay = br.accept(new ConcreteRentalVisitor());
		assertEquals(toPay, 1, 0.01);
		
		BikeRental br2 = new BikeRental(eb, rentDate, mary);
		// fake returning the bike
		br2.setReturnDate(returnDate);
		double toPay2 = br.accept(new ConcreteRentalVisitor());
		assertEquals(toPay2, 1, 0.01);
	}
}
