package algorithms.coursera;

public class LargestAmongConnected {

	public static void main(String[] args) {
		WeightedQuickUnionBySize memberConnections = new WeightedQuickUnionBySize(10);
		
		memberConnections.connect(1, 2);
		memberConnections.connect(6, 9);
		memberConnections.connect(2, 6);
		System.out.println("Largest in group containing 2 is " + memberConnections.getMaximumOfConnectedComponents(2));
		
		memberConnections.connect(5,10);
		System.out.println("Largest in group containing 5 is " + memberConnections.getMaximumOfConnectedComponents(5));
		
		System.out.println("Largest in group containing 7 is " + memberConnections.getMaximumOfConnectedComponents(7));
	}

}
