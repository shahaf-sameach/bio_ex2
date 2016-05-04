package bio_ex2;

import java.util.ArrayList;
import java.util.Random;

public class Utils {
	
	public static boolean isNumeric(char c){
		if (!Character.isDigit(c)) return false;

		return true;
	}
	
	public static boolean isNumeric(String str){
	    for (char c : str.toCharArray())
	        if (!Character.isDigit(c)) return false;
	    
	    return true;
	}
	
	public static int dist(String str1, String str2){
		return Math.abs(str1.length() - str2.length());
	}
	
	public static int rand(int arr[]){
		return arr[new Random().nextInt(arr.length)];
	}
	
	public static Object rand(ArrayList arr){
		return arr.get(new Random().nextInt(arr.size()));
	}


}
