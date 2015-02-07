package algorithms.coursera.bst;

public class BSTClient {

	public static void main(String[] args) {
		BST<Integer> bst = new BST<Integer>();
		
		bst.insert(new Integer(2));
		bst.insert(new Integer(1));
		bst.insert(new Integer(4));
		bst.insert(new Integer(3));
		bst.insert(new Integer(5));
		
		bst.preOrder();
		System.out.println();
		
		bst.delete(new Integer(2));
		
		bst.preOrder();
		System.out.println();
		
		bst.delete(new Integer(1));

		bst.preOrder();
		System.out.println();

		bst.delete(new Integer(5));
		bst.preOrder();
		System.out.println();

		System.out.println(bst.findMin().getData());
	}
}
