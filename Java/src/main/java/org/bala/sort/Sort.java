package org.bala.sort;

public abstract class Sort {

	protected SortOrder order;
	
	public Sort(SortOrder order) {
		this.order = order;
	}
	
	public abstract <T> T[] sort(Comparable<T>[] a);
	
	protected  <T> boolean less(Comparable<T> a , Comparable<T> b) {
		
		return a.compareTo((T)b) < 0;
	
	}

	protected <T> boolean greater(Comparable<T> a, Comparable<T> b) {
		return a.compareTo((T)b) > 0;
	}
	
	protected <T >void exchange(Comparable<T>[]a, int i, int j) {
		Comparable<T> temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}
	
	public enum SortOrder {
		asc,
		desc
	}	
}
