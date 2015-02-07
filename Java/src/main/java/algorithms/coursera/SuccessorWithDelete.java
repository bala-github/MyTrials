package algorithms.coursera;

import java.util.Arrays;

public class SuccessorWithDelete extends WeightedQuickUnionBySize {
     
	private boolean [] isDeleted;
	
	private int[] successor;
	
	SuccessorWithDelete(int n) {
		super(n);
		isDeleted = new boolean[n+1];
		Arrays.fill(isDeleted, false);
		successor = new int[n+1];
          
		//intially the successor of each element is the same element.
		for(int i = 1; i <= n; i++) {
			successor[i] = i;
		}
		
	}


	public void remove(int i) {

		if(i > n ) {
			throw new IndexOutOfBoundsException();
		}
		
		if(isDeleted[i] == true) {
			//already deleted. do nothing.
			return;
		}
		
		isDeleted[i] = true;
		
		
		if(i == n) {
			//do nothing
			return;
		}
		
		//connect i with i+1;
	    connect(i, i+1);	
	}

	public int successor(int i) {

		if(i > n ) {
			throw new IndexOutOfBoundsException();
		}
		
        if(i == n && isDeleted[i] == true) {
        	return -1;
        }
      
        if(isDeleted[i] == false) {
        	//this node is not deleted.
        	return i;
        } else {
        	
        	int maxNode = getMaximumOfConnectedComponents(i);
        	return(isDeleted[maxNode] == false ? maxNode : -1);
        }
	}
	
  
public static void main(String[] args) {
	
	SuccessorWithDelete nodes = new SuccessorWithDelete(10);
	
	for(int i = 1; i <= 10; i++) {
		System.out.println("Successor of " + i + ":" + nodes.successor(i));
	}
	
	nodes.remove(1);
	nodes.remove(10);

	for(int i = 1; i <= 10; i++) {
		System.out.println("Successor of " + i + ":" + nodes.successor(i));
	}
	
	nodes.remove(2);
	nodes.remove(9);
	nodes.remove(5);
	nodes.remove(6);
	
	for(int i = 1; i <= 10; i++) {
		System.out.println("Successor of " + i + ":" + nodes.successor(i));
	}
	
}	

}
