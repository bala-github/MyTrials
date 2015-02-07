public class Percolation {
  private WeightedQuickUnionUF siteConnections;
  private int[] sites;
  private int n;

  // create N-by-N grid, with all sites blocked
  public Percolation(int N) {

	if (N <= 0) {
		throw new IllegalArgumentException();
	}
	
    this.n = N;
	siteConnections = new WeightedQuickUnionUF((n * n) + 2);
	sites = new int[(n * n) + 2];

	for (int i = 0; i < (n * n) + 2; i++) {
		sites[i] = 0;
	}

	sites[0] = 1;
	sites[(n * n) + 1] = 1;
  }

  // open site (row i, column j) if it is not already
  public void open(int i, int j) {

	if(i < 1 || i > n) {
		throw new IndexOutOfBoundsException();
	}

	if(j < 1 || j > n) {
		throw new IndexOutOfBoundsException();
	}
	
	int siteIndex = ((i - 1) * n) + j;
	sites[siteIndex] = 1;

	if (i == 1) {
		// first row.
	siteConnections.union(siteIndex, 0);
  }
  if (i > 1) {
	// not first row. Check site at top.
	if (isOpen(i - 1, j)) {
		int topSiteIndex = ((i - 2) * n) + j;
		siteConnections.union(siteIndex, topSiteIndex);
	}
  }

  if (i != n) {
	// not last row. Check site at bottom.
	if (isOpen(i + 1, j)) {
		int bottomSiteIndex = (i * n) + j;
		siteConnections.union(siteIndex, bottomSiteIndex);
	}

  }

  if ((j % n) != 1) {
	// not first column. Check site at left.
	if (isOpen(i, j - 1)) {
		int leftSiteIndex = ((i - 1) * n) + j - 1;
		siteConnections.union(siteIndex, leftSiteIndex);
	}
  }

  if ((j % n) != 0) {
	// not last column. Check site at right.
	if (isOpen(i, j + 1)) {
		int rightSiteIndex = ((i - 1) * n) + j + 1;
		siteConnections.union(siteIndex, rightSiteIndex);
	}
  }

  if (i == n) {
	// last row.
		siteConnections.union(siteIndex, (n * n) + 1);
	}
  }

  // is site (row i, column j) open?
  public boolean isOpen(int i, int j) {

	if(i < 1 || i > n) {
		throw new IndexOutOfBoundsException();
	}

	if(j < 1 || j > n) {
		throw new IndexOutOfBoundsException();
	}

	return sites[((i - 1) * n) + j] == 1;
  }

  // is site (row i, column j) full?
  public boolean isFull(int i, int j) {

    if(i < 1 || i > n) {
	  throw new IndexOutOfBoundsException();
    }

    if(j < 1 || j > n) {
	  throw new IndexOutOfBoundsException();
    }
	  
	int siteIndex = ((i - 1) * n) + j;
	return siteConnections.connected(siteIndex, 0);
  }

  // does the system percolate?
  public boolean percolates() {

	return siteConnections.connected(0, (n * n) + 1);
  }

  //  test client, optional
  public static void main(String[] args) {
	int N = 200;
	Percolation grid = new Percolation(N);
	int sitesOpened = 0;

	do {
		// select a site at random.
        int row = StdRandom.uniform(1, N + 1);
        int column = StdRandom.uniform(1, N + 1);
        if (grid.isOpen(row, column)) {
	      continue;
        }
        grid.open(row, column);
        // new site opened.
        sitesOpened++;
       } while (!grid.percolates());

      System.out.println("No. of open sites:" + sitesOpened);
  }

}
