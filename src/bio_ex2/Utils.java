package bio_ex2;

import java.util.ArrayList;
import java.util.Random;

public class Utils {
	
	private Random rand = new Random();
	
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
	
	public String swap(String str){
		char char1;
		char char2;
		do {
			char1 = getRandomChar(str);
			char2 = getRandomChar(str);
		} while (char1 == char2);
		return  str.replace(char1, '-').replace(char2, '+').replace('-', char2).replace('+', char1);
	}
	
	public char getRandomChar(String str){
		Random rand = new Random();
		int rnd = rand.nextInt(str.length());
		return str.substring(rnd, rnd+1).charAt(0);
	}
	
	public String getExcludeNumbers(String numbers){
		for(int i=0;i<10;i++){
			if (!numbers.contains(Integer.toString(i)))
				numbers.replace(Integer.toString(i),"");
		}
		return numbers;
	}

	public String swap(String str, char char1, char char2) {
		return str.replace(char1, char2);
	}


}
