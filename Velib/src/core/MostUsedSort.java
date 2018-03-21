package core;

import java.util.ArrayList;
import java.util.Collections;

import core.station.Station;

public class MostUsedSort implements SortingStrategy {
	public ArrayList<Station> sort(ArrayList<Station> stations) {
		ArrayList<Station> sortedStations = new ArrayList<Station>(stations);
		
		int maxNumOfUses;
		int curNumOfUses;
		int maxNumOfUsesIndex;
		for (int i = 0; i < stations.size(); i++) {
			maxNumOfUses = stations.get(i).getStats().getTotalRentals() + 
					stations.get(i).getStats().getTotalReturns();
			maxNumOfUsesIndex = i;
			for (int j = i; j < stations.size(); j++) {
				curNumOfUses = stations.get(j).getStats().getTotalRentals() + 
						stations.get(j).getStats().getTotalReturns();
				if (curNumOfUses > maxNumOfUses) {
					maxNumOfUses = curNumOfUses;
					maxNumOfUsesIndex = j;
				}
			}
			
			Collections.swap(sortedStations, maxNumOfUsesIndex, i);
		}
		
		return sortedStations;
	}
}
