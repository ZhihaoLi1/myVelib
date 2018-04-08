package core.test.junit.stationSort;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import core.station.InvalidStationTypeException;
import core.station.InvalidTimeSpanException;
import core.station.Station;
import core.station.StationFactory;
import core.station.stationSort.MostUsedSort;
import core.station.stationSort.SortingStrategy;
import utils.DateParser;
import utils.Point;

/**
 * Sort the stations according the most used in descending order
 * @author animato
 *
 */
public class MostUsedSortTest {

	@Test
	public void testSort() throws InvalidTimeSpanException, InvalidStationTypeException {
		ArrayList<Station> stations = new ArrayList<Station>();
		SortingStrategy mostUsedSort = new MostUsedSort();

		assertTrue(stations.equals(mostUsedSort.sort(stations, DateParser.parse("01/01/2000T00:00:00"), DateParser.parse("01/01/2000T01:00:00"))));

		StationFactory stationFactory = new StationFactory();
		Station station1 = null, station2 = null, station3 = null;
		
		station1 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
		station2 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);
		station3 = stationFactory.createStation("STANDARD", 2, new Point(0, 0), true);

		stations.add(station1);
		stations.add(station2);
		stations.add(station3);

		stations.get(1).getStats().incrementTotalReturns();

		stations.get(0).getStats().incrementTotalRentals();
		stations.get(0).getStats().incrementTotalReturns();

		stations.get(2).getStats().incrementTotalRentals();
		stations.get(2).getStats().incrementTotalRentals();
		stations.get(2).getStats().incrementTotalReturns();

		ArrayList<Station> expectedStations = new ArrayList<Station>();
		expectedStations.add(station3);
		expectedStations.add(station1);
		expectedStations.add(station2);

		assertTrue(expectedStations.equals(mostUsedSort.sort(stations, DateParser.parse("01/01/2000T00:00:00"), DateParser.parse("01/01/2000T01:00:00"))));
	}

}
