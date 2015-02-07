package algorithms.coursera;

public interface WeightedQuickUnion {

	//what is default access specifiers for functions in interface.
	boolean isConnected(int i, int j);
	
	void connect(int i, int j);
}
