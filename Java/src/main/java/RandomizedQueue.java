import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;


public class RandomizedQueue<Item> implements Iterable<Item> {

  private Random random = new Random();	
  private Node begin;
  private Node end;
  private int size;
  
  private class Node {
	  Item data;
	  Node next;
  }
  // construct an empty randomized queue
  public RandomizedQueue() {
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
  public void enqueue(Item item) {
	  
	if(item == null) {
		throw new NullPointerException();
	}
	
    Node newNode = new Node();
	newNode.data = item;
	newNode.next = null;
	
	if(size == 0) {
		begin = newNode;
		end = newNode;
	} else {
	  Node currentEnd = end;
	  currentEnd.next = newNode;
	  end = newNode;
	}  		
	size++;
  }
  
  // delete and return a random item  
  public Item dequeue() {
      if(size == 0) {
    	  throw new NoSuchElementException();
      }
	  int n = random.nextInt(size + 1);
	  Node current = begin;
	  Item data = null;
	  if(n == 0) {
		  //dequeue first node
		  data = begin.data;
		  begin = begin.next;
	  } else {
	  
	    for(int i = 0; i < n - 1; i++) {
		   current = current.next; 
	    }
  	    //item to return.
	     data = current.next.data;
	  
  	    //delete the item.
	     current.next = current.next.next;
	     
	     if(current.next == null) {
	    	 //if we deleted the last node. update end
	    	 end = current;
	     }
	  }
	  
	  size--;
	  return data;
  }
  
  // return (but do not delete) a random item  
  public Item sample() {

	  int n = random.nextInt(size);
	  Node current = begin;  
	  for(int i = 0; i < n; i++) {
		 current = current.next; 
	  }
	  return current.data;
  }
  
  
  @Override
  public Iterator<Item> iterator() {
  
    return new RandomQueueIterator();
  }
  
  private class RandomQueueIterator implements Iterator<Item> {

	private boolean[] visited = new boolean[size];
	private int seen = 0;
	private Node current = begin;
	
	@Override
	public boolean hasNext() {		
	  return (seen != size);
	}

	@Override
	public Item next() {
	  if(current == null || seen == size) {
		  throw new NoSuchElementException();
	  }
	  int n;
	  do {
	     n = random.nextInt(size);
	     
	  } while(visited[n] == true);
	  
	  Node temp = current; 
	  for(int i = 0; i < n; i++) {
		 temp = temp.next; 
	  }
	  visited[n] = true;
	  seen++;
	  return temp.data;
	  
	  /*
      Item data = current.data;
      current = current.next;
	  return data;
	  */
	}

	@Override
	public void remove() {

		throw new UnsupportedOperationException();
	}
	  
  }
  // unit testing
  public static void main(String[] args) {
	  
	  RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
	  queue.enqueue(1);
	  queue.enqueue(2);
	  queue.enqueue(2);
	  
	  System.out.println("Sample:" + queue.sample());
	  System.out.println("Sample:" + queue.sample());
	  System.out.println("Sample:" + queue.sample());
	  
	  System.out.println("Queue size" + queue.size());
	  Iterator<Integer> iterator = queue.iterator();
	  
	  while(iterator.hasNext()) {
		  System.out.println(iterator.next());
	  }
	  
	  queue.dequeue();
	  queue.dequeue();

      iterator = queue.iterator();
	  
	  while(iterator.hasNext()) {
		  System.out.println(iterator.next());
	  }	  
  }


	
}
