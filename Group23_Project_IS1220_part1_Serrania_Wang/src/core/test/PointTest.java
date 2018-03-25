package core.test;

import static org.junit.Assert.*;

import org.junit.Test;

import core.utils.Point;

/**
 * Test Point class ofr distance and equality
 * @author animato
 *
 */
public class PointTest {

	@Test
	public void distanceTest() {
		Point a = new Point(0, 1);
		Point b = new Point(1, 4);
		assertEquals(a.distance(b), 3.162, 0.01);
	}

	@Test
	public void equalsTest() {
		Point a = new Point(1, 1);
		Point b = new Point(1, 1);
		assertEquals(a, b);
	}
}
