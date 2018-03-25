package core.scenarios;

import core.Network;
import core.bike.BikeType;
import core.card.VLibreCardVisitor;
import core.card.VMaxCardVisitor;
import core.point.Point;
import user.User;
import core.ridePlan.RidePlan;
import core.ridePlan.RidePlanPolicyName;
import core.utils.DateParser;
public class Scenario1 {
	
	public static void main(String[] args) {
		// We are in the year 2000, January the 1st
		
		// create the network 
		Network n = new Network("myVelib", 10, 10, 10, 0.75, 0.3, 0.5);
		// create users
		User alice = new User("Mary");
		User bob = new User("Bob", new VLibreCardVisitor());
		User charles = new User("Charles", new VMaxCardVisitor());
		
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
		RidePlan aliceRidePlanGo = n.createRidePlan(home, supermarket, alice, RidePlanPolicyName.FASTEST, BikeType.MECH);
		
		// Time Alice might take to go 
		// System.out.println(aliceRidePlanGo.approximateTime());
		
		// Alice rents the bike at 9:30am 
		String AliceRentBikeMessage = n.rentBike(alice.getId(), aliceRidePlanGo.getSourceStation().getId(), "MECH", DateParser.parse("01/01/2000 09:30:00"));
		System.out.println(AliceRentBikeMessage);
		// Alice returns the bike at 10.45am
		String AliceReturnBikeMessage = n.returnBike(alice.getId(), aliceRidePlanGo.getDestinationStation().getId(), DateParser.parse("01/01/2000 11:20:00"), 110);
		System.out.println(AliceReturnBikeMessage);
		
		RidePlan aliceRidePlanReturn = n.createRidePlan(supermarket, home, alice, RidePlanPolicyName.SHORTEST, BikeType.ELEC);

		// Time Alice might take to return 
		// System.out.println(aliceRidePlanReturn.approximateTime());
		
		// Alice rents the bike at 13:00
		String AliceRentBikeMessage2 = n.rentBike(alice.getId(), aliceRidePlanReturn.getSourceStation().getId(), "MECH", DateParser.parse("01/01/2000 13:00:00"));
		System.out.println(AliceRentBikeMessage2);
		// Alice returns the bike at 10.45am
		String AliceReturnBikeMessage2 = n.returnBike(alice.getId(), aliceRidePlanReturn.getDestinationStation().getId(), DateParser.parse("01/01/2000 14:15:00"), 75);
		System.out.println(AliceReturnBikeMessage2);
		
		
		// plan a ride for Bob 
		RidePlan bobRidePlanGo = n.createRidePlan(school, library, bob, RidePlanPolicyName.PREFER_PLUS, BikeType.MECH);
		
		// Time bob might take to go 
		// System.out.println(bobRidePlanGo.approximateTime());
		
		// bob rents the bike at 9:30am 
		String bobRentBikeMessage = n.rentBike(bob.getId(), bobRidePlanGo.getSourceStation().getId(), "MECH", DateParser.parse("01/01/2000 09:30:00"));
		System.out.println(bobRentBikeMessage);
		// Dest Station 1 goes offline 
		bobRidePlanGo.getDestinationStation().setOnline(false);
		// bob create another rideplan 
		RidePlan bobRidePlanGo2 = n.createRidePlan(school, library, bob, RidePlanPolicyName.PREFER_PLUS, BikeType.MECH);
		// bob returns the bike at 10.45am
		String bobReturnBikeMessage = n.returnBike(bob.getId(), bobRidePlanGo2.getDestinationStation().getId(), DateParser.parse("01/01/2000 11:20:00"), 110);
		System.out.println(bobReturnBikeMessage);
		
		// Time bob might take to return 
		// System.out.println(bobRidePlanReturn.approximateTime());
		
		RidePlan bobRidePlanReturn = n.createRidePlan(library, home, bob, RidePlanPolicyName.AVOID_PLUS, BikeType.ELEC);
		// bob rents the bike at 13:00
		String bobRentBikeMessage2 = n.rentBike(bob.getId(), bobRidePlanReturn.getSourceStation().getId(), "MECH", DateParser.parse("01/01/2000 13:00:00"));
		System.out.println(bobRentBikeMessage2);
		// bob returns the bike at 10.45am
		String bobReturnBikeMessage2 = n.returnBike(bob.getId(), bobRidePlanReturn.getDestinationStation().getId(), DateParser.parse("01/01/2000 14:15:00"), 75);
		System.out.println(bobReturnBikeMessage2);
	}
}
