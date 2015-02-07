import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Deque<Item> implements Iterable<Item> {

	  private Random random = new Random();	
	  private Node begin;
	  private Node end;
	  private int size;
	  
	  private class Node {
		  Item data;
		  Node next;
		  Node previous;
	  }
	  // construct an empty randomized queue
	  public Deque() {
	    this.begin = null;
	    this.end = null;
	    this.size = 0;
	  }
	  
	  // is the queue empty?  
	  public boolean isEmpty() {
	    return (size == 0);
	  }
	  // return the number of items on the queue  
	  public int size() {
	     return size;
	  }
	  
	  // add the item  
	  public void addFirst(Item item) {

	    if(item == null) {
		  throw new NullPointerException();
		}
					  
	    Node newNode = new Node();
		newNode.data = item;
		newNode.previous = null;
		newNode.next = begin;
		
		begin = newNode;
		size++;
		if(size == 1) {			
			end = newNode;
		}  		
		
	  }

	  public void addLast(Item item) {

		if(item == null) {
		  throw new NullPointerException();
		}
		  
	    Node newNode = new Node();
		newNode.data = item;
		newNode.next = null;
		newNode.previous = end;
		size++;
		if(size == 1) {
			begin = newNode;
			end = newNode;
		} else {
			end.next = newNode;
			end = newNode;
		}
		
	  }
	  
	  // delete and return a random item  
	  public Item removeFirst() {

         if(size == 0) {
	    	  throw new NoSuchElementException();
	      }
	
          Item data = begin.data;
          begin = begin.next;
          
          if(begin == null) {
        	  end = null;
          }
          size--;
          
          return data;
	  }

	  public Item removeLast() {
	      if(size == 0) {
	    	  throw new NoSuchElementException();
	      }
		  
	      Item data = null;
	      if(size == 1) {
	    	  data = end.data;
	    	  begin = null;
	    	  end = null;
	      } else {
	    	  data = end.data;
	    	  end = end.previous;
	    	  end.next = null;
	      }
	      size--;
	      return data;
	  }
	  
	  
	  @Override
	  public Iterator<Item> iterator() {
	  
	    return new QueueIterator();
	  }
	  
	  private class QueueIterator implements Iterator<Item> {

		private Node current = begin;
		
		@Override
		public boolean hasNext() {		
		  return (current != null);
		}

		@Override
		public Item next() {
		  if(current == null) {
			  throw new NoSuchElementException();
		  }
	      Item data = current.data;
	      current = current.next;
		  return data;
		}

		@Override
		public void remove() {

			throw new UnsupportedOperationException();
		}
		  
	  }
	  // unit testing
	  public static void main(String[] args) {
		  
		  Deque<Integer> queue = new Deque<Integer>();
		  queue.addFirst(1);
		  queue.addLast(3);
		  queue.addFirst(2);
		  
		  System.out.println("Queue size" + queue.size());
		  Iterator<Integer> iterator = queue.iterator();
		  
		  while(iterator.hasNext()) {
			  System.out.println(iterator.next());
		  }
		  
		  queue.removeFirst();
		  queue.removeLast();

		  System.out.println("Queue size" + queue.size());
	      iterator = queue.iterator();
		  
		  while(iterator.hasNext()) {
			  System.out.println(iterator.next());
		  }	  
	  }


}
