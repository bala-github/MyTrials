package org.bala.sort;

import java.util.Arrays;

public class CountInversion {

	public static  long sortAndCountSplitInversions(int[] left, int[] right, int[] output)
	{
		long count = 0;
		int i = 0, j = 0, k = 0;
		while(i < left.length && j < right.length) 
		{
			if(left[i] <= right[j])
			{
				output[k] = left[i];
				i++;
			}
			else
			{
				output[k] = right[j];
				count = count + (left.length - i);
				j++;
			}
			k++;
		}
		
		while(i < left.length)
		{
			output[k] = left[i];
			i++;
			k++;
		}
			
		while(j < right.length)
		{
			output[k] = right[j];
			j++;
			k++;			
		}
		left = right;
		
		return count;
	}

	public static long countInversions(int[] input)
	{
		if(input.length == 1)
		{
			return 0;
		}
		int[] output = new int[input.length];
		int[] left = Arrays.copyOfRange(input, 0, input.length/2);
		int[] right = Arrays.copyOfRange(input, input.length/2, input.length);
		long leftInversions = countInversions(left);
		long rightInversions = countInversions(right);
		long splitInversions = sortAndCountSplitInversions(left, right, output) ;
		int i = 0;
		
		for(int n : output)
		{
		  
			input[i] = n;
			i++;
		}
		
        
		return leftInversions + rightInversions + splitInversions;
	}
	
	
	
}
