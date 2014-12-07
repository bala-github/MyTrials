package org.bala.stack;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class StackImpl<T> implements Stack<T> , Iterable<T>{
	
	Node ptr = null;
	
	private class Node {
		T item;
		Node next;
		
		Node(T item) {
			this.item = item;
			next = null;
		}
	}
	public T pop() {
	    T item = ptr.item;
	    ptr = ptr.next;
		return item;
	}

	public void push(T item) {
		
		Node node = new Node(item);
		node.next = ptr;
		ptr = node;
	}

	public boolean isEmpty() {
		
		return (ptr == null);
	}

	public Iterator<T> iterator() {
		
		return new StackIterator();
	}
	
	private class StackIterator implements Iterator<T> {

		private Node current = ptr;
		
		public boolean hasNext() {
			
			return current != null;
		}

		public T next() {
			if(!hasNext()) {
				throw new NoSuchElementException();
			}
			T next = current.item;
		    current = current.next;
			return next;
		}

		public void remove() {
			throw new UnsupportedOperationException();
			
		}
		
	}
}
