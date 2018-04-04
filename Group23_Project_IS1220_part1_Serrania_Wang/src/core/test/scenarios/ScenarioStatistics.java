package core.test.scenarios;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import core.Network;
import core.card.CardVisitorFactory;
import core.card.InvalidCardTypeException;
import core.station.Station;
import core.user.User;
import utils.DateParser;

public class ScenarioStatistics {
	public static void main (String[] args) {
		// We are in the year 2000, January the 1st
		
		// create the network 
		Network n = new Network("myVelib", 10, 10, 10, 0.75, 0.3, 0.5, DateParser.parse("01/01/2000 09:00:00"));
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
		n.addUser(alice);
		n.addUser(bob);
		n.addUser(charles);
		
		ArrayList<Station> stations = new ArrayList<Station>(n.getStations().values());
		
		// 10 rides for each user using random source and destination station 
		for (int i=1; i<10; i++) {
			int sInd = ThreadLocalRandom.current().nextInt(0, stations.size());
			int timeSpent = ThreadLocalRandom.current().nextInt(30, 300);
			System.out.println(n.rentBike(alice.getId(), stations.get(sInd).getId(), "MECH", DateParser.parse("0" + i + "/01/2000 09:00:00")));
			System.out.println(n.returnBike(alice.getId(), stations.get(sInd).getId(), DateParser.parse("0" + i + "/01/2000 09:00:00").plusMinutes(timeSpent), timeSpent));
		}
		
		for (int i=0; i<10; i++) {
			int sInd = ThreadLocalRandom.current().nextInt(0, stations.size());
			int timeSpent = ThreadLocalRandom.current().nextInt(30, 300);
			System.out.println(n.rentBike(bob.getId(), stations.get(sInd).getId(), "MECH", DateParser.parse((10+i) + "/01/2000 09:00:00")));
			System.out.println(n.returnBike(bob.getId(), stations.get(sInd).getId(), DateParser.parse((10+i) + "/01/2000 09:00:00").plusMinutes(timeSpent), timeSpent));
		}

		for (int i=0; i<10; i++) {
			int sInd = ThreadLocalRandom.current().nextInt(0, stations.size());
			int timeSpent = ThreadLocalRandom.current().nextInt(30, 300);
			System.out.println(n.rentBike(charles.getId(), stations.get(sInd).getId(), "MECH", DateParser.parse((20+i) + "/01/2000 09:00:00")));
			System.out.println(n.returnBike(charles.getId(), stations.get(sInd).getId(), DateParser.parse((20+i) + "/01/2000 09:00:00").plusMinutes(timeSpent), timeSpent));
		}
		
		for (Station s: stations) {
			System.out.println(n.displayStation(s.getId()));
		}
		
		System.out.println(alice.displayStats());
		System.out.println(bob.displayStats());
		System.out.println(charles.displayStats());
	}
}
