package org.bala.stack;

public class StackTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		StackImpl<String> stack = new StackImpl<String>();
		
		stack.push("first");
		stack.push("second");
		
		for(String item : stack) {
			System.out.println(item);
		}
		System.out.println(stack.pop());
		System.out.println(stack.pop());
	}

}
