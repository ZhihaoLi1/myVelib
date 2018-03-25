package core.test.scenarios;

import core.Network;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.ridePlan.InvalidRidePlanPolicyException;
import core.ridePlan.NoValidStationFoundException;
import core.ridePlan.RidePlan;
import core.user.User;
import core.utils.DateParser;
import core.utils.Point;

public class Scenario1 {
	
	public static void main(String[] args) {
		// We are in the year 2000, January the 1st
		
		// create the network 
		Network n = new Network("myVelib", 10, 10, 10, 0.75, 0.3, 0.5, DateParser.parse("01/01/2000 09:30:00"));
		CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
		// create users
		User alice = null, bob = null, charles = null;
		try {
			alice = new User("Alice", cardVisitorFactory.createCard("NO_CARD"));
			bob = new User("Bob", cardVisitorFactory.createCard("VLIBRE_CARD"));
			charles = new User("Charles", cardVisitorFactory.createCard("VMAX_CARD"));
		} catch (InvalidCardTypeException e) {
			System.out.println("Invalid card type given.");
			return;
		}
		
		
		// add users to network 
		n.addUser(alice);
		n.addUser(bob);
		n.addUser(charles);
		
		// create landmarks
		Point home = new Point(0, 0);
		Point school = new Point(5,5);
		Point supermarket = new Point(7,2);
		Point library = new Point(2,6);
		
		// plan a ride for Alice 
		System.out.println("Alice plans a ride using the fastest policy, for a mech bike:");
		RidePlan aliceRidePlanGo = null;
		try {
			aliceRidePlanGo = n.createRidePlan(home, supermarket, alice, "FASTEST", "MECH");
		} catch (InvalidBikeTypeException e) {
			System.out.println("Invalid bike type given");
			return;
		} catch (InvalidRidePlanPolicyException e) {
			System.out.println("Invalid policy given");
			return;
		} catch (NoValidStationFoundException e) {
			System.out.println("No valid station was found");
			return;
		}
		System.out.println(aliceRidePlanGo);
		// Time Alice might take to go 
		// System.out.println(aliceRidePlanGo.approximateTime());
		
		// Alice rents the bike at 9:30am 
		System.out.println("\nAlice rents a mech bike at 9:30am at the source station of her plan:");
		String AliceRentBikeMessage = n.rentBike(alice.getId(), aliceRidePlanGo.getSourceStation().getId(), "MECH", DateParser.parse("01/01/2000 09:30:00"));
		System.out.println(AliceRentBikeMessage);
		// Alice returns the bike at 10.45am
		System.out.println("\nAlice returns the mech bike at 11:20am at the destination station of her plan:");
		String AliceReturnBikeMessage = n.returnBike(alice.getId(), aliceRidePlanGo.getDestinationStation().getId(), DateParser.parse("01/01/2000 11:20:00"), 110);
		System.out.println(AliceReturnBikeMessage);
		
		System.out.println("\nAlice plans a new ride using the shortest policy, for an elec bike:");
		RidePlan aliceRidePlanReturn = null;
		try {
			aliceRidePlanReturn = n.createRidePlan(home, supermarket, alice, "SHORTEST", "MECH");
		} catch (InvalidBikeTypeException e) {
			System.out.println("Invalid bike type given");
			return;
		} catch (InvalidRidePlanPolicyException e) {
			System.out.println("Invalid policy given");
			return;
		} catch (NoValidStationFoundException e) {
			System.out.println("No valid station was found");
			return;
		}
		System.out.println(aliceRidePlanReturn);

		// Time Alice might take to return 
		// System.out.println(aliceRidePlanReturn.approximateTime());
		
		// Alice rents the bike at 1:00pm
		System.out.println("\nAlice rents an elec bike at 1:00pm at the source station of her plan:");
		String AliceRentBikeMessage2 = n.rentBike(alice.getId(), aliceRidePlanReturn.getSourceStation().getId(), "ELEC", DateParser.parse("01/01/2000 13:00:00"));
		System.out.println(AliceRentBikeMessage2);
		// Alice returns the bike at 2:15pm
		System.out.println("\nAlice returns the elec bike at 2:15pm at the destination station of her plan:");
		String AliceReturnBikeMessage2 = n.returnBike(alice.getId(), aliceRidePlanReturn.getDestinationStation().getId(), DateParser.parse("01/01/2000 14:15:00"), 75);
		System.out.println(AliceReturnBikeMessage2);
	}
}
