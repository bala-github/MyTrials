import java.util.Comparator;


public class Point implements Comparable<Point> {

	private int x;
	
	private int y;
	
	public final Comparator<Point> SLOPE_ORDER = new Comparator<Point>() {
		// compare points by slope to this point
		@Override
		public int compare(Point a, Point b) {
			
			if(slopeTo(a) < slopeTo(b)) {
				return -1;
			} else if(slopeTo(a) > slopeTo(b)) {
				return 1;
			} else {
				return 0;
			}
			
		}
		
	};        
	
	public Point(int x, int y) {
		// construct the point (x, y)
		this.x = x;
		this.y = y;
	}
	
	public void draw() {
		// draw this point
		StdDraw.point(x, y);
	}
	public void drawTo(Point that) {
		// draw the line segment from this point to that point
		StdDraw.line(x, y, that.x, that.y);
	}
	
	public String toString() {
		// string representation
		return ("(" + x + ", " + y + ")" );
	}
	
	public    int compareTo(Point that) {
		// is this point lexicographically smaller than that point?
		if(x == that.x && y == that.y) {
			return 0;
		} 
		
		if(y < that.y || (y == that.y && x < that.x)) {
			return -1;
		}
		
		return 1;
	}
	public double slopeTo(Point that) {
		// the slope between this point and that point
		if(that == null) {
			throw new IllegalArgumentException();
		}
		
		if(x == that.x) {
			
			if(y == that.y) {
				return Double.NEGATIVE_INFINITY;
			} else {
				return Double.POSITIVE_INFINITY;
			}
		} else if(y == that.y) {
			if(x == that.x) {
				return Double.NEGATIVE_INFINITY;
			} else {
				return 0;
			}
			
		} else {
			
			return ((double)(that.y - y)) / ((double)(that.x - x));
		}
	}
}
