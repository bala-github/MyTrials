package org.bala.stack;

public interface  Stack<T> {

	T pop();
	
	void push(T item);
	
	boolean isEmpty();
}
