package org.bala.sort;

public class HeapSort extends Sort {

	
	public HeapSort() {
		super(SortOrder.asc);
	}

	@Override
	public <T> T[] sort(Comparable<T>[] a) {
	
		int n = a.length - 1;
		//convert the array into heap.
		//values are present from index 1.
		for(int i = n/2; i>=1; i--) {
			sink(a,i, n);
		}

		for(int i = n; i > 1; i--) {
			exchange(a, n--, 1);
			sink(a, 1, n);
		}
		
		return (T[])a;
	}

	public <T> void sink(Comparable<T>[] a, int k, int n /*size of the heap*/) {
		//ensures the binary tree starting at pos k is a heap.
		while(k <= n/2) {
			int j = 2*k;
			if(j < n && less(a[j],a[j+1])) {
				j++;
			}
			
			if(less(a[k],a[j])) {
				exchange(a,k,j);
			}
			
			k = j;
		}
	}
	
	public <T> void swim(Comparable<T>[] a, int k, int n) {
		
		while(k > 1 && less(a[k/2], a[k])) {			
			exchange(a, k/2, k);
			k = k/2;			
		}
	}
	
	public static void main(String[] args) {
		
		Sort sorter = new HeapSort();
		Integer [] input = new Integer[] {-1, 3, 6, 4, 1};
		
		System.out.println("Ascending Order");
		for(Integer i : sorter.sort(input)) {
			
			System.out.println(i);
		}
		
	}
}
