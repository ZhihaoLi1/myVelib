package core.test.junit.network;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import core.Network;
import core.bike.BikeFactory;
import core.bike.InvalidBikeTypeException;
import core.card.CardVisitorFactory;
import core.station.InvalidStationTypeException;
import core.station.InvalidTimeSpanException;
import core.station.Station;
import core.station.StationFactory;
import core.station.stationSort.InvalidSortingPolicyException;
import utils.DateParser;
import utils.Point;

/**
 * Test sortStation and createStationSort methods of Network
 * 
 * @author animato
 */
public class SortStationTest {

	// create a network
	static Network n = new Network("EmptyNetwork", 0, 0, 4, 0, 0, 0, DateParser.parse("01/01/2000T00:00:00"));

	static CardVisitorFactory cardVisitorFactory = new CardVisitorFactory();

	static StationFactory stationFactory = new StationFactory();
	static BikeFactory bikeFactory = new BikeFactory();
	// Create standard source station
	static Station sourceStation;
	// Create standard dest station
	static Station destStation;
	// Create empty nearest to starting point
	static Station emptySourceStation;
	// Create elec station nearest to starting point
	static Station elecSourceStation;
	// Create full destination station
	static Station fullDestStation;

	@BeforeClass
	public static void initialize() throws InvalidStationTypeException, InvalidBikeTypeException {
		sourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.1), true);
		destStation = stationFactory.createStation("STANDARD", 10, new Point(9, 9.5), true);
		emptySourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.02), true);
		elecSourceStation = stationFactory.createStation("STANDARD", 10, new Point(0, 0.01), true);
		fullDestStation = stationFactory.createStation("STANDARD", 10, new Point(9, 9.9), true);

		// Plus and Standard destinations are the same distance away
		n.addStation(sourceStation);
		n.addStation(destStation);
		n.addStation(emptySourceStation);
		n.addStation(fullDestStation);
		n.addStation(elecSourceStation);
	}

	/**
	 * Verify that createStationSort does work when given correct arguments and policy
	 */
	@Test
	public void testCreateStationSort() throws InvalidTimeSpanException, InvalidSortingPolicyException {
		n.createStationSort("MOST_USED");
	}

	/**
	 * When an invalid policy is given an InvalidSortingPolicyException should be
	 * thrown
	 */
	@Test
	public void whenInvalidPolicyIsGivenThenThrowException() throws InvalidTimeSpanException {
		try {
			n.createStationSort("NOPE");
			fail("InvalidSortingPolicyException e");
		} catch (InvalidSortingPolicyException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * When an null policy is given an IllegalArgumentException should be
	 * thrown
	 * 
	 */
	@Test
	public void whenNullPolicyIsGivenThenThrowException() throws InvalidTimeSpanException, InvalidSortingPolicyException {
		try {
			n.createStationSort(null);
			fail("IllegalArgumentException should have been thrown");
		} catch (IllegalArgumentException e) {
			assertTrue(true);
		}
	}
	
	/**
	 * Verify that sortStation does work when given correct arguments and policy
	 */
	@Test
	public void testSortStation() {
		n.sortStation("MOST_USED");
	}

	/**
	 * When an invalid policy is given to planRide nothing happens
	 */
	@Test
	public void whenInvalidPolicyIsGivenThenNothingHappens() {
		n.sortStation("NOPE");
	}
	
	/**
	 * 	 * When an invalid policy is given to planRide nothing happens

	 */
	@Test
	public void whenNullPolicyIsGivenThenNothingHappens() {
		n.sortStation(null);
	}
	
	
}
