package core.station;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import core.point.Point;
import core.utils.DateParser;

public class StationStats {
	private Station station;
	private int totalRentals = 0;
	private int totalReturns = 0;
	
	public StationStats(Station station) {
		this.station = station;
	}
	
	public double getOccupationRate(LocalDateTime startDate, LocalDateTime endDate) {
		double totalTimeOccupied = 0;
		for (ParkingSlot parkingSlot: station.getParkingSlots()) {
			totalTimeOccupied += parkingSlot.getOccupationRate(startDate, endDate);
		}
		
		return totalTimeOccupied / (station.getParkingSlots().size() * startDate.until(endDate, ChronoUnit.SECONDS));
	}

	public int getTotalRentals() {
		return this.totalRentals;
	}

	public void incrementTotalRentals() {
		this.totalRentals += 1;
	}
	
	public int getTotalReturns() {
		return this.totalReturns;
	}

	public void incrementTotalReturns() {
		this.totalReturns += 1;
	}
	
	@Override
	public String toString() {
		return "total rentals: " + this.totalRentals + ", total returns: " + this.totalReturns;
	}
	
	public static void main(String[] args) {
		Station st = new StandardStation(10, new Point(0,0));
		StationStats ssm = null;
		ssm = new StationStats(st);
		
		LocalDateTime t1 = DateParser.parse("01/01/2000 00:00:00");
		LocalDateTime t2 = DateParser.parse("01/01/2000 00:59:00");
		
		ParkingSlotStatus s;
		
		s = new ParkingSlotStatus(ParkingSlotStatusName.OUT_OF_ORDER, DateParser.parse("31/12/1999 23:15:00"));
		s.setEndDate(DateParser.parse("01/01/2000 00:05:00"));
		System.out.println(s);
		st.getParkingSlots().get(0).setStatus(ParkingSlotStatusName.OUT_OF_ORDER, DateParser.parse("31/12/1999 23:15:00"));
		st.getParkingSlots().get(0).setStatus(ParkingSlotStatusName.FREE, DateParser.parse("01/01/2000 00:05:00"));
		st.getParkingSlots().get(0).setStatus(ParkingSlotStatusName.OCCUPIED, DateParser.parse("01/01/2000 00:24:10"));
		st.getParkingSlots().get(0).setStatus(ParkingSlotStatusName.OUT_OF_ORDER, DateParser.parse("01/01/2000 00:39:26"));
		st.getParkingSlots().get(0).setStatus(ParkingSlotStatusName.FREE, DateParser.parse("01/01/2000 01:04:00"));
		
		st.getParkingSlots().get(1).setStatus(ParkingSlotStatusName.OUT_OF_ORDER, DateParser.parse("31/12/1999 23:15:00"));
		st.getParkingSlots().get(1).setStatus(ParkingSlotStatusName.FREE, DateParser.parse("01/01/2000 00:05:00"));
		st.getParkingSlots().get(1).setStatus(ParkingSlotStatusName.OCCUPIED, DateParser.parse("01/01/2000 00:24:10"));
		st.getParkingSlots().get(1).setStatus(ParkingSlotStatusName.OUT_OF_ORDER, DateParser.parse("01/01/2000 00:39:26"));
		st.getParkingSlots().get(1).setStatus(ParkingSlotStatusName.FREE, DateParser.parse("01/01/2000 01:04:00"));

		System.out.println(ssm.getOccupationRate(t1, t2));
	}



}
