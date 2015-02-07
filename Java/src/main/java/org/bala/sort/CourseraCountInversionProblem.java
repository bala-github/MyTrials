package org.bala.sort;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseraCountInversionProblem {

	public static void main(String[] args) throws IOException
	{
	   int [] input = new int[100000];
	   
	   BufferedReader reader = new BufferedReader(new FileReader("c:\\IntegerArray.txt"));
	   
	   int i = 0;
	   String line;
	   while((line=reader.readLine()) != null)
	   {
		   input[i] = Integer.parseInt(line);
		   i++;
	   }
	   
	    
	   System.out.println("Total no. of numbers:" + i + "Total no. of inversions:" + CountInversion.countInversions(input));
	}

}
