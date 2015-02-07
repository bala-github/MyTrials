import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;


public class Fast {

	public static void main(String[] args) {
		
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		if(args.length != 1) {
			throw new IllegalArgumentException();
		}
		
		String inputFile = args[0];
	
		Scanner scanner = new Scanner(Brute.class.getClassLoader().getResourceAsStream(inputFile));
		
		int n = scanner.nextInt();
		
		List<Point> points = new ArrayList<Point>();
		List<Point> auxPoints = new ArrayList<Point>();
		
		int i = 1;
		
		while(i <= n) {
			int x = scanner.nextInt();
			int y = scanner.nextInt();
			points.add(new Point(x, y));
			auxPoints.add(new Point(x, y));
			i++;
		}
		
		scanner.close();
		
		Collections.sort(points);
		List<Point> collinearPoints = new ArrayList<Point>();
		List<Double> collinearSlopes = new ArrayList<Double>();
		for(Point pointx : points) {
			pointx.draw();
			Collections.sort(auxPoints, pointx.SLOPE_ORDER);
			collinearPoints.clear();
			collinearPoints.add(pointx);
			
			int counter = 0;
			
			double slopeToCompare = Double.NaN;
			
			//System.out.println();
			//System.out.print("From Point:" + pointx);
			for(Point pointy : auxPoints) {
				
				double slope = pointx.slopeTo(pointy);
				//System.out.print("->Point:" + pointy + "Slope:" + slope);
								
				if(slope == Double.NEGATIVE_INFINITY) {
				continue;
				}

				//increment counter
				counter++;

				if(counter == 1) {
				//this is first point.
				slopeToCompare = slope;
				collinearPoints.add(pointy);
				} else {
				//compare this slope with previous slope.
				if(slopeToCompare == slope) {
					collinearPoints.add(pointy);
					counter++;
					//System.out.println(pointy);
				} else {
					
					if(collinearPoints.size() >= 4) {
						boolean seen = false;
						for(Double collinearSlope : collinearSlopes) {
							if(collinearSlope == slopeToCompare) {
								seen = true;
								break;
							}
						}
						
						if(seen) {
							continue;
						}
						collinearSlopes.add(slopeToCompare);
						Collections.sort(collinearPoints);
						System.out.print(collinearPoints.get(0));
						for(i = 1; i < collinearPoints.size(); i++) {
							System.out.print(" -> " + collinearPoints.get(i));
						}
						System.out.println();
						collinearPoints.get(0).drawTo(collinearPoints.get(collinearPoints.size() - 1));
				}
					slopeToCompare = slope;
					counter = 1;
					collinearPoints.clear();
					collinearPoints.add(pointx);
					collinearPoints.add(pointy);
				}
				}
				}

		}
	}
}


/*
if(slope == Double.NEGATIVE_INFINITY) {
continue;
}

//increment counter
counter++;

if(counter == 1) {
//this is first point.
slopeToCompare = slope;
} else {
//compare this slope with previous slope.
if(slopeToCompare == slope) {
	collinearPoints.add(pointy);
	counter++;
	//System.out.println(pointy);
} else {
	slopeToCompare = slope;
	counter = 1;
}
}
}

if(collinearPoints.size() >= 4) {
System.out.println();
for(Point point : collinearPoints) {
System.out.print(point + " -> ");
}
*/


/*
boolean seen = false;
for(Double slope : collinearSlopes) {
	if(slope == slopeToCompare) {
		seen = true;
		break;
	}
}

if(seen) {
	continue;
}
collinearSlopes.add(slopeToCompare);*/