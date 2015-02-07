
public class Subset {

	public static void main(String[] args) {
		
		if (args.length != 1) {
			throw new IllegalArgumentException();
		}

		int k = Integer.parseInt(args[0]);
		
		RandomizedQueue <String> queue = new RandomizedQueue<String>();
		String data;
		while((data = StdIn.readString()) != null) {
			queue.enqueue(data);
		}
		
		for(int i = 0; i < k; i++) {
			System.out.println(queue.sample());
		}
	}

}
