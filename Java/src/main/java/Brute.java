import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;


public class Brute {

	public static void main(String[] args) throws IOException {
		
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		
		if(args.length != 1) {
			throw new IllegalArgumentException();
		}
		
		String inputFile = args[0];
	
		Scanner scanner = new Scanner(Brute.class.getClassLoader().getResourceAsStream(inputFile));
		
		int n = scanner.nextInt();
		
		Point points[] = new Point[n];
		
		int i = 1;
		
		while(i <= n) {
			points[i-1] = new Point(scanner.nextInt(), scanner.nextInt());
			i++;
		}
		
		scanner.close();
		
		Collections.sort(Arrays.asList(points));
		
		for(i = 0; i < n; i++) {
			Point pointi = points[i];
			pointi.draw();
			//System.out.println("pointi" + pointi);
			for(int j = i + 1; j < n; j++) {
				Point pointj = points[j];
				double slopej = pointi.slopeTo(pointj);
				//System.out.println("pointj" + pointj + "slopej" + slopej);
				for(int k = j + 1; k < n; k++) {
					Point pointk = points[k];
					double slopek = pointi.slopeTo(pointk);
					//System.out.println("pointk" + pointk + "slopek" + slopek);
					if(slopej != slopek) {
						continue;
					}
					for(int l = k + 1; l < n; l++) {
						
						Point pointl = points[l];
						double slopel = pointi.slopeTo(pointl);
						//System.out.println("pointl" + pointl + "slopel" + slopel);
						if(slopel != slopek) {
							continue;
						}
						ArrayList<Point> collinearPoints = new ArrayList<Point>();
						collinearPoints.add(pointi);
						collinearPoints.add(pointj);
						collinearPoints.add(pointk);
						collinearPoints.add(pointl);
						Collections.sort(collinearPoints);
						
						System.out.print(collinearPoints.get(0));
						for(i = 1; i < collinearPoints.size(); i++) {
							System.out.print(" -> " + collinearPoints.get(i));
						}
						System.out.println();
						collinearPoints.get(0).drawTo(collinearPoints.get(collinearPoints.size() - 1));
					}
				}
			}
		}
	}
}
