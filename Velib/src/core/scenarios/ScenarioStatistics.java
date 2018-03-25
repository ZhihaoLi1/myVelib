package core.scenarios;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import core.Network;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.point.Point;
import core.station.Station;
import core.utils.DateParser;
import user.User;

public class ScenarioStatistics {
	public static void main (String[] args) {
		// We are in the year 2000, January the 1st
		
		// create the network 
		Network n = new Network("myVelib", 10, 10, 10, 0.75, 0.3, 0.5);
		CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();
		// create users
		User alice = null, bob = null, charles = null;
		try {
			alice = new User("Alice", cardVisitorFactory.createCard("NO_CARD"));
			bob = new User("Bob", cardVisitorFactory.createCard("VLIBRE_CARD"));
			charles = new User("Charles", cardVisitorFactory.createCard("VMAX_CARD"));
		} catch (InvalidCardTypeException e) {
			System.out.println("Invalid card type given.");
		}
		
		
		// add users to network 
		n.createUser(alice);
		n.createUser(bob);
		n.createUser(charles);
		
		ArrayList<Station> stations = new ArrayList<Station>(n.getStations().values());
		
		// 10 rides for each user using random source and destination station 
		for (int i=0; i<10; i++) {
			int sInd = ThreadLocalRandom.current().nextInt(0, stations.size());
			int day = ThreadLocalRandom.current().nextInt(10, 28);
			int timeSpent = ThreadLocalRandom.current().nextInt(30, 300);
			n.rentBike(alice.getId(), stations.get(sInd).getId(), "MECH", DateParser.parse(day + "/01/2000 09:30:00"));
			n.returnBike(alice.getId(), stations.get(sInd).getId(), DateParser.parse(day + "/01/2000 09:30:00").plusMinutes(timeSpent), timeSpent);
		}
		
		for (int i=0; i<10; i++) {
			int sInd = ThreadLocalRandom.current().nextInt(0, stations.size());
			int day = ThreadLocalRandom.current().nextInt(10, 28);
			int timeSpent = ThreadLocalRandom.current().nextInt(30, 300);
			n.rentBike(bob.getId(), stations.get(sInd).getId(), "MECH", DateParser.parse(day + "/01/2000 09:30:00"));
			n.returnBike(bob.getId(), stations.get(sInd).getId(), DateParser.parse(day + "/01/2000 09:30:00").plusMinutes(timeSpent), timeSpent);
		}

		for (int i=0; i<10; i++) {
			int sInd = ThreadLocalRandom.current().nextInt(0, stations.size());
			int day = ThreadLocalRandom.current().nextInt(10, 28);
			int timeSpent = ThreadLocalRandom.current().nextInt(30, 300);
			n.rentBike(charles.getId(), stations.get(sInd).getId(), "MECH", DateParser.parse(day + "/01/2000 09:30:00"));
			n.returnBike(charles.getId(), stations.get(sInd).getId(), DateParser.parse(day + "/01/2000 09:30:00").plusMinutes(timeSpent), timeSpent);
		}
		
		for (Station s: stations) {
			System.out.println(s.displayStats());
		}
		
		System.out.println(alice.displayStats());
		System.out.println(bob.displayStats());
		System.out.println(charles.displayStats());
	}
}
