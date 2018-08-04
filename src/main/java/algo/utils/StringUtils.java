package main.java.algo.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StringUtils {

    private static Random rand = new Random();

    public static String mutateString(String str){
        String excludedNumbers = getExcludedNumbers(str);

        if (excludedNumbers.length() > 0){
            if (rand.nextInt(10) < 5) {
                char char1 = getRandomChar(str);
                char char2 = getRandomChar(excludedNumbers);
                str = swap(str, char1, char2);
            }
            else {
                str = swap(str);
            }
        } else{
            str = swap(str);
        }

        return str;
    }

    public static String crossOverStrings(String str1, String str2, int pivot){

        String out1 = str1.substring(0,pivot);
        String out2 = str2.substring(pivot);

        // 0.5 prob
        if (rand.nextInt(2) % 2 == 0){
            out1 = str2.substring(0,pivot);
            out2 = str2.substring(pivot);
        }

        return out1 + out2;
    }


    //return letter-number map of number string
    public static Map<Character,Character> getCharMap(String keys, String values){
        Map<Character,Character> map = new HashMap<Character,Character>();
        for(int i=0;i<keys.length();i++){
            if (!map.containsValue(values.charAt(i)))
                map.put(keys.charAt(i),values.charAt(i));
        }
        return map;

    }

    //convert string of numbers to String of letters based on specific map
    public static String numToWord(String src, String target, String map) {
        for(int i=0;i<target.length();i++){
            int indx = map.indexOf(target.charAt(i));
            if (indx > -1)
                target = target.replace(target.charAt(i), src.charAt(indx));
        }
        return target;
    }


    //returned score based on length diff between optional solution to target
    public static double lenScore(String sol, String target){
        return (double)(sol.length()- lenDist(sol, target))/(double)sol.length();
    }


    //calc the number of similar chars between the src to the target
    public static int sameCharsNum(String src, String target){
        int same = 0;
        for(int i=0;i<target.length();i++)
            if (src.contains(target.substring(i, i+1))){
                same++;
            }

        return same;
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

    public static String getExcludedNumbers(String numbers){
        for(int i=0;i<10;i++){
            if (!numbers.contains(Integer.toString(i)))
                numbers.replace(Integer.toString(i),"");
        }
        return numbers;
    }

    public static String swap(String str, char char1, char char2) {
        return str.replace(char1, char2);
    }

    public static boolean isNumeric(char c){
        if (!Character.isDigit(c)) return false;

        return true;
    }

    public static boolean isNumeric(String str){
        for (char c : str.toCharArray())
            if (!Character.isDigit(c)) return false;

        return true;
    }

    public static int lenDist(String str1, String str2){
		return Math.abs(str1.length() - str2.length());
	}
}
