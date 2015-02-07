package algorithms.coursera;

public class WeightedQuickUnionByHeight implements WeightedQuickUnion {
	private int [] parent;
	private int [] height;
	private int [] maximum;
	//total no. of nodes.
	protected int n;
	WeightedQuickUnionByHeight(int n) {
		this. n = n;
	  	parent = new int[n + 1];
	  	
	  	//how to initialize an array with default value.
	  	height = new int[n + 1]; 
	  	
	  	maximum = new int[n+1];
	  	
	  	for(int i = 0; i <= n; i ++) {
	  		parent[i] = i;
	  		height[i] = 0;
	  		maximum[i] = i;
	  	}
	}
	private int root(int i) {
		
		while(i != parent[i]) {
			i = parent[i];
		}
		
		return i;
		
	}
	@Override
	public boolean isConnected(int i, int j) {

		if(i < 1 || i > n) {
			throw new IndexOutOfBoundsException();
		}

		if(j < 1 || j > n) {
			throw new IndexOutOfBoundsException();
		}
		int iRoot = root(i);
		int jRoot = root(j);

		return iRoot == jRoot;
	}

	@Override
	public void connect(int i, int j) {

		if(i < 1 || i > n) {
			throw new IndexOutOfBoundsException();
		}

		if(j < 1 || j > n) {
			throw new IndexOutOfBoundsException();
		}
		
		int iRoot = root(i);
		int jRoot = root(j);
		
		if (iRoot == jRoot) {
			
			return;
		}
		
		if ( height[iRoot] < height[jRoot]) {
			
			parent[iRoot] = parent[jRoot];
			
			height[jRoot] = height[jRoot] > (1 + height[iRoot]) ? height[jRoot] : (1 + height[iRoot]);
			maximum[jRoot] = (maximum[iRoot] > maximum[jRoot]) ? maximum[iRoot] : maximum[jRoot];
		} else {
			parent[jRoot] = parent[iRoot];
			height[iRoot] = height[iRoot] > (1 + height[jRoot]) ? height[iRoot] : (1 + height[jRoot]);
			maximum[iRoot] = (maximum[iRoot] > maximum[jRoot]) ? maximum[iRoot] : maximum[jRoot];
		}

	}

	public int getheightOfConnectedComponents(int i) {

		if(i < 1 || i > n) {
			throw new IndexOutOfBoundsException();
		}
		
		return height[root(i)];
	}
	
	public int getMaximumOfConnectedComponents(int i) {

		if(i < 1 || i > n) {
			throw new IndexOutOfBoundsException();
		}
		
		return maximum[root(i)];
	}


}
