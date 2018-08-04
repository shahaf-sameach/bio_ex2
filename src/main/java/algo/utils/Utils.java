package main.java.algo.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Utils {


    public static <T> T randElement(T[] arr){
        return arr[new Random().nextInt(arr.length)];
    }

	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> unsortedMap ){
	    List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>( unsortedMap.entrySet() );
	    
	    Collections.sort( list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));
	
	    Map<K, V> sortedMap = new LinkedHashMap<K, V>();
	    for (Map.Entry<K, V> entry : list){
	        sortedMap.put(entry.getKey(), entry.getValue());
	    }
	    return sortedMap;
	}

}
