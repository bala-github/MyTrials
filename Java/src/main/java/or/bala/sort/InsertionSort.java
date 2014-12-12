package or.bala.sort;

public class InsertionSort {

	
	public static int[] sort(int[] array) {
		for(int i = 0; i < array.length - 1; i++) {
			for(int j= i + 1; j > 0; j--) {
				if(array[j-1] > array[j]) {
					int temp = array[j-1];
					array[j-1]= array[j];
					array[j] = temp;
				}
			}
		}
		return array;
	}
	
	public static void main(String[] args) {
		
		int[] array = {1, 7, 5, 3, 4 };
		
		for(int i : sort(array)) {
			System.out.println(i);
		}
	}
}
