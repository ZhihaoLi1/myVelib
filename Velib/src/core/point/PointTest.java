package core.point;

import static org.junit.Assert.*;

import org.junit.Test;

public class PointTest {

	@Test
	public void distanceTest() {
		Point a = new Point(0,1);
		Point b = new Point(1,4);
		assertEquals(a.distance(b), 3.162, 0.01);
	}

}
