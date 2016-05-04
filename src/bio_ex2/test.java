package bio_ex2;

import java.util.HashMap;
import java.util.Map;

public class test {
	public static void main(String[] args) {
		System.out.println("hello world:");
		String a = "SEND";
		String b = "MORE";
		String c = "MONEY";
		String[] sol_arry = new String[100];
		Solution sol = new Solution(a,b,c);
		for(int i=0;i<10;i++){
			System.out.println(sol.generate());
		}
	}
}
