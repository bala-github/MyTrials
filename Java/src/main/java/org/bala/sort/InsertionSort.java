package org.bala.sort;

public class InsertionSort extends Sort {

	public InsertionSort(SortOrder order) {
		super(order);
	}

	@Override
	public <T> T[] sort(Comparable<T>[] a) {
	
		for(int i = 0; i < a.length; i++) {
			for(int j = i; j > 0 ; j = j - 1) {					
				switch (order) {	
					case asc:
						if(less(a[j], a[j-1])) {
							exchange(a, j, j-1);
						}
						break;
					case desc:
						if(greater(a[j], a[j-1])) {
							exchange(a, j, j-1);
						}
						break;						
				}
			}
		}
		return (T[])a;
	}

	public static void main(String[] args) {
		
		Sort sorter = new InsertionSort(SortOrder.asc);
		Integer [] input = new Integer[] { 3, 6, 4, 1};
		
		System.out.println("Ascending Order");
		for(Integer i : sorter.sort(input)) {
			
			System.out.println(i);
		}
		
		sorter = new InsertionSort(SortOrder.desc);
		
		System.out.println("Descending Order");
		
		for(Integer i : sorter.sort(input)) {
			
			System.out.println(i);
		}
		
	}
}
