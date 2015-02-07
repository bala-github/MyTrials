package algorithms.coursera;

public class SocialNetworkQuestion {

	public static void main(String[] args) {
	
			
			WeightedQuickUnionBySize memberConnections = new WeightedQuickUnionBySize(5);
			
			memberConnections.connect(1, 2);
			
			System.out.println("Everyone " + (memberConnections.getSizeOfConnectedComponents(1) == 5 ? "is" : "not") + "Connected");
			
			memberConnections.connect(3, 4);
			
			System.out.println("Everyone " + (memberConnections.getSizeOfConnectedComponents(1) == 5 ? "is" : "not") + "Connected");
			
			memberConnections.connect(2, 5);

			System.out.println("Everyone " + (memberConnections.getSizeOfConnectedComponents(1) == 5 ? "is" : "not") + "Connected");
			
			memberConnections.connect(5, 3);

			System.out.println("Everyone " + (memberConnections.getSizeOfConnectedComponents(1) == 5 ? "is" : "not") + "Connected");			
		
	}

}
