package org.bala.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort extends Sort {

	public MergeSort(SortOrder order) {
		super(order);
		
	}

	public <T> T[] merge(T[] left, T[] right) {
		
		int i = 0, j = 0;
		
		List<T> result = new ArrayList<T> (left.length + right.length);
		
		while(i < left.length && j < right.length) {
			
			switch (order) {	
			case asc:
				if(less(left[j], right[j])) {
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
	@Override
	public <T> T[] sort(Comparable<T>[] a) {
		// TODO Auto-generated method stub
		
		if(a.length == 1) {
			return (T[])a;
		}
		
		T[] left = sort(Arrays.copyOf(a, a.length/2));
		T[] right = sort(Arrays.copyOfRange(a, a.length/2, (a.length/2) -1);
		
		return merge(left, right);
	}

}
