package algorithms.coursera;

public class WeightedQuickUnionBySize implements WeightedQuickUnion {

	private int [] parent;
	private int [] size;
	private int [] maximum;
	//total no. of nodes.
	protected int n;
	WeightedQuickUnionBySize(int n) {
		this. n = n;
	  	parent = new int[n + 1];
	  	
	  	//how to initialize an array with default value.
	  	size = new int[n + 1]; 
	  	
	  	maximum = new int[n+1];
	  	
	  	for(int i = 0; i <= n; i ++) {
	  		parent[i] = i;
	  		size[i] = 1;
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
		
		if ( size[iRoot] < size[jRoot]) {
			
			parent[iRoot] = parent[jRoot];
			size[jRoot] = size[jRoot] + size[iRoot];
			maximum[jRoot] = (maximum[iRoot] > maximum[jRoot]) ? maximum[iRoot] : maximum[jRoot];
		} else {
			parent[jRoot] = parent[iRoot];
			size[iRoot] = size[iRoot] + size[jRoot];
			maximum[iRoot] = (maximum[iRoot] > maximum[jRoot]) ? maximum[iRoot] : maximum[jRoot];
		}

	}

	public int getSizeOfConnectedComponents(int i) {

		if(i < 1 || i > n) {
			throw new IndexOutOfBoundsException();
		}
		
		return size[root(i)];
	}
	
	public int getMaximumOfConnectedComponents(int i) {

		if(i < 1 || i > n) {
			throw new IndexOutOfBoundsException();
		}
		
		return maximum[root(i)];
	}


}
