package utils;

/**
 * Represents the coordinates (in a 2D plane) of a point
 * 
 * @author matto
 *
 */
public class Point {
	// The coordinates of the point, in km
	private double x;
	private double y;

	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Calculates distance between this point and another given point.
	 * 
	 * @param p
	 *            the point of which we want to calculate the distance from this.
	 * @return the distance between the two points, in km
	 */
	public double distance(Point p) {
		return Math.sqrt(Math.pow(p.y - this.y, 2) + Math.pow(p.x - this.x, 2));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;
			if (this.x == p.getX() && this.y == p.getY()) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + x + "," + y + ")";
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
