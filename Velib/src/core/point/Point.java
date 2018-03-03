package core.point;

public class Point {
	private double x;
	private double y;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double distance(Point p) {
		return Math.sqrt(Math.pow(p.y - this.y, 2) + Math.pow(p.x - this.x, 2));
	}
}
