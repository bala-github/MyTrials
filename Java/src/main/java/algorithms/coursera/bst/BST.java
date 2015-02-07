package algorithms.coursera.bst;

public class BST <T extends Comparable<T>> {

	Node root = null;
	
	private Node insert(Node node, T data) {

		if(node == null) {
			node = new Node(data);			
		}
		
		int cmp = node.data.compareTo(data); 
		if(cmp == 1) {
			node.left = insert(node.left, data);
		} else if(cmp == -1) {
			node.right = insert(node.right, data);
		} else {
			node.data = data;
		}
		
		return node;
	}
	
	private void preOrder(Node node){
		
		if(node != null) {
			
			preOrder(node.left);
			System.out.print(node.data);
			preOrder(node.right);
		}
		
	}
	
	private Node findMin(Node node) {
		
		if(node.left == null) {
			return node;
		}
		
		return findMin(node.left);
	}
	
	private Node delete(Node node, T data) {
		
		if(node == null) {
			return null;
		}
		
		int cmp = node.data.compareTo(data); 
		
		if(cmp == 1) {
			node.left = delete(node.left, data);
		} else if(cmp == -1) {
			node.right = delete(node.right, data);
		} else {
			
			if(node.right == null) {
				return node.right;
			}else if(node.right == null) {
				return node.left;
			} else {
				
				Node min = findMin(node.right);
				node.right = delete(node.right, min.data);
				node.data = min.data;
			}

		}		
		
		return node;
	}
	
	
	public void insert(T data) {
		
		root = insert(root,  data);
	}
	
	public void preOrder() {
		preOrder(root);
	}
	
	public Node findMin() {
		
		return findMin(root);
	}
	
	public void delete(T data) {
		delete(root, data);
	}
	public boolean isPresent(T data) {
		
		Node node = root;
		while(root != null) {
			
			int cmp = node.data.compareTo(data); 
			if(cmp == 1) {
				node = node.left;
			} else if(cmp == -1) {
				node = node.right;
			} else {
				return true;
			}
			
		}
		
		return false;
	}
	
	public class Node {
		T data = null;
		Node left = null;
		Node right = null;
		
		private Node(T data) {
			this.data = data;
		}
		
		T getData() {
			return data;
		}
	}
	
}
