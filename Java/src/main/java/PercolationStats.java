
public class PercolationStats {
	
	private double[] openSitesRatio;
	
	private double mean;
	
	private double stddev;
	
	private double confidenceLo;
	
	private double confidenceHi;
	
  
	// perform T independent computational experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		
		if (N <= 0 || T <= 0) {
			throw new IllegalArgumentException();
		}
		
		openSitesRatio = new double[T];
		
		for (int i = 0; i < T; i++) {
			Percolation grid = new Percolation(N);
			double sitesOpened = 0;
			
		    do {
		    	//select a site at random.
				int row = StdRandom.uniform(1, N + 1);
				int column = StdRandom.uniform(1, N + 1);
                if (grid.isOpen(row, column)) {
                	continue;
                }
                grid.open(row, column);
                //new site opened.
                sitesOpened++;
		    } while(!grid.percolates());
		    
		    //site percolates. Calculate opensitesRatio.
		    openSitesRatio[i] = sitesOpened / (N*N);
		}
		
		mean = StdStats.mean(openSitesRatio);
		stddev = StdStats.stddev(openSitesRatio);
		
		confidenceLo = mean - ((1.96*stddev)/(java.lang.Math.sqrt(T)));
		
		confidenceHi = mean + ((1.96*stddev)/(java.lang.Math.sqrt(T)));
	}
	
	// sample mean of percolation threshold
	public double mean() {
		
		return mean;
		
	}
	
	 // sample standard deviation of percolation threshold
	public double stddev() {
		
		return stddev;
	}
	
	// returns lower bound of the 95% confidence interval
	public double confidenceLo() {
		
		return confidenceLo;
	}
	
	// returns upper bound of the 95% confidence interval
	public double confidenceHi() {
		
		return confidenceHi;
	}
	
	// test client, described below
	public static void main(String[] args) {
		
		if (args.length != 2) {
			throw new IllegalArgumentException();
		}
		
		int N = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		
		Stopwatch watch = new Stopwatch(); 
		PercolationStats  percolationStats = new PercolationStats(N, T);
		
		System.out.println("mean                    = " + percolationStats.mean());
		System.out.println("stddev                  = " + percolationStats.stddev());
		System.out.println("95% confidence interval = " + percolationStats.confidenceLo() + "," + percolationStats.confidenceHi());
		System.out.println("Elapased Time           = " + watch.elapsedTime());
	}

}


/*
 N = 500 T = 100
 mean                    = 0.59311496
 stddev                  = 0.004586457205224265
 95% confidence interval = 0.592216014387776,0.5940139056122239
 Elapased Time           = 2.345
 
 N = 1000 T = 100
 mean                    = 0.59266774
stddev                  = 0.0027847979765327725
95% confidence interval = 0.5921219195965995,0.5932135604034005
Elapased Time           = 19.174
 */

