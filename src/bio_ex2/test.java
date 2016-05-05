package bio_ex2;

import java.util.HashMap;
import java.util.Map;

public class test {
	public String ffoo(String str){
		str = "h";
		return str;
	}
	
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
		
		test t = new test();
		String ah = "jhj";
		System.out.println(ah);
		System.out.println(t.ffoo(ah));
		System.out.println(ah);
		Utils bb = new Utils();
		System.out.println(bb.swap(ah, 'h','6'));
		
	}
}
