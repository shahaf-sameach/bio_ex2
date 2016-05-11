package bio_ex2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
	
	public static String rand(String arr[]){
		return arr[new Random().nextInt(arr.length)];
	}
	
	public static String swap(String str){
		char char1;
		char char2;
		do {
			char1 = getRandomChar(str);
			char2 = getRandomChar(str);
		} while (char1 == char2);
		return  str.replace(char1, '-').replace(char2, '+').replace('-', char2).replace('+', char1);
	}
	
	public static char getRandomChar(String str){
		Random rand = new Random();
		int rnd = rand.nextInt(str.length());
		return str.substring(rnd, rnd+1).charAt(0);
	}
	
	public static String getExcludeNumbers(String numbers){
		for(int i=0;i<10;i++){
			if (!numbers.contains(Integer.toString(i)))
				numbers.replace(Integer.toString(i),"");
		}
		return numbers;
	}

	public static String swap(String str, char char1, char char2) {
		return str.replace(char1, char2);
	}
	
	public static int min(int[] array){
		int min = 100;
		for(int i=0;i<array.length;i++){
			if (array[i] < min){
				min = array[i];
			}
		}
		return min;
	}
	
	public static int max(int[] array){
		int max = 0;
		for(int i=0;i<array.length;i++){
			if (array[i] > max){
				max = array[i];
			}
		}
		return max;
	}
	
	public static int avg(int[] array){
		int avg = 0;
		for(int i=0;i<array.length;i++){
			avg = avg + array[i];
		}
		return avg/array.length;
	}
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ){
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( map.entrySet() );
	    
	    Collections.sort( list, new Comparator<Map.Entry<K, V>>(){
	        public int compare( Map.Entry<K, V> o1, Map.Entry<K, V> o2 ){
	            return (o2.getValue()).compareTo( o1.getValue() );
	        }
	    } );
	
	    Map<K, V> result = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list){
	        result.put( entry.getKey(), entry.getValue() );
	    }
	    return result;
	}

}
