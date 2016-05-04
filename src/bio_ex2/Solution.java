package bio_ex2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Solution {
	
	private String word1;
	private String word2;
	private String target;
	private int max_score = 0;
	private String max_solution;
	private Random rand = new Random();
	
	public Solution(String word1, String word2, String target){
		this.word1 = word1;
		this.word2 = word2;
		this.target = target;
	}
	
	public String[] initSolutions(String[] solutions_arr){
		for(int i=0;i<solutions_arr.length;i++)
			solutions_arr[i] = generate();
		return solutions_arr;
	}
	
	public String generate(){
		String possible_sol = this.word1 + this.word2;
		String numbers = "0123456789";
		Random rand = new Random();
		for(int i = 0; i < possible_sol.length(); i++)
		{
			int rnd = rand.nextInt(numbers.length());
			if (!(Utils.isNumeric(possible_sol.charAt(i))))
			{
				possible_sol = possible_sol.replace(possible_sol.charAt(i), numbers.charAt(rnd));
				numbers = numbers.replace(numbers.substring(rnd, rnd+1),"");
			}

		}
		return possible_sol;
	}
	
	public String getWord1(){
		return this.word1;
	}
	
	public String getWord2(){
		return this.word2;
	}
	
	public String getTarget(){
		return this.target;
	}
	
	public String crossOver(String sol1, String sol2){
		String str1 = sol1.substring(0,this.word1.length());
		String str2 = sol2.substring(this.word1.length(),sol2.length());
		if (rand.nextInt(2) % 2 == 0){
			str1 = sol2.substring(0,this.word1.length());
			str2 = sol1.substring(this.word1.length(),sol1.length());
		}

		return str1 + str2;
	}
	
	public int fitness(String sol){
		int score = 0;
		String calc_result = calcEquation(sol,"+");
		String opt_sol = wordToNum(sol);
		int min_len =  Math.min(opt_sol.length(), calc_result.length());
		for(int i=0; i<min_len; i++){
			if (Utils.isNumeric(opt_sol.charAt(i)))
				if (opt_sol.charAt(i) == calc_result.charAt(i)) 
					score++;
		}
		
		double same_char_score = (double)score/(double)sameChars();
		double len_score = (double)(opt_sol.length()-Utils.dist(opt_sol, target))/(double)opt_sol.length();
		int total_score = (int)((0.5*same_char_score + 0.5*len_score) * 100);
		return total_score;
	}
	
	public String numToWord(String num_map, String target){
		String src = target;
		String letter_map = this.word1 + this.word2;
		for(int i=0;i<target.length();i++){
			int indx = num_map.indexOf(target.charAt(i));
			if (indx > -1)
				src = src.replace(target.charAt(i), letter_map.charAt(indx));
		}
		return src;
	}
	
	public String wordToNum(String num_map){
		String src = target;
		String letter_map = this.word1 + this.word2;
		for(int i=0;i<target.length();i++){
			int indx = letter_map.indexOf(target.charAt(i));
			if (indx > -1)
				src = src.replace(target.charAt(i), num_map.charAt(indx));
		}
		return src;
	}
	
	public boolean isValid(String sol){
		String num_target = wordToNum(sol);
		String comp = calcEquation(sol, "+");
		if (comp.length() != num_target.length())
			return false;
		
		for(int i = 0;i<comp.length();i++){
			if (Utils.isNumeric(num_target.charAt(i))){
				if (comp.charAt(i) != num_target.charAt(i)) return false;
			}
		}
		
		return true;
	}
	
	private String calcEquation(String eq, String sign){
		int num1 = Integer.parseInt(eq.substring(0, word1.length()));
		int num2 = Integer.parseInt(eq.substring(word1.length(), eq.length()));
		int result = num1 + num2;
		return Integer.toString(result);
	}
	
	public Map getSolution(String num, String str){
		Map map = new HashMap(); 
		for(int i=0;i<num.length();i++){
			map.put(str.charAt(i),num.charAt(i));
		}
		return map;
	}
	
	public String revalidate(String num){
		String letter = this.word1 + this.word2;
		Map<Character,Character> map = new HashMap<Character,Character>(); 
		String valid_str = "";
		for(int i=0;i<letter.length();i++){
			if (!map.containsValue(num.charAt(i)))
				map.put(letter.charAt(i),num.charAt(i));
		}
		for(int i=0;i<letter.length();i++){
			if (!map.containsKey(letter.charAt(i))){
				char key;
				do {
					key = Integer.toString(rand.nextInt(10)).charAt(0);
				} while (map.containsValue(key));
				map.put(letter.charAt(i),key);
			}
		}
		
		for(int i=0;i<letter.length();i++){
			valid_str = valid_str + map.get(letter.charAt(i));
		}
		return valid_str;
	}
	
	
	public String solve(String[] solutions, int iterations){
		String str = "";
		ArrayList<String> tmp_arr = new ArrayList<String>();
		Map<String,Integer> fitness_map = new HashMap<String,Integer>(); 
		for(int k=0;k<iterations;k++){
			System.out.println("iteration: " + k);
			tmp_arr.clear();
			fitness_map.clear();
			int fitness_sum = 0;
			int score = 0;
			String solution;
			for(int i=0;i<100;i++){
				solution = solutions[i];
				if (isValid(solution)){
					System.out.println("valid solution = " + solution);
					return solution;
				}
				score = fitness(solution);
				saveMaxSolution(solution, score);
				fitness_sum += score;
				fitness_map.put(solution, score);	
			}
			System.out.println("max=(" + max_solution + "," + max_score + ")");
			System.out.println(fitness_map.size());

			for(int i=0;i<fitness_map.size();i++){
				String key = solutions[i];
				int occurence = (int)((double)fitness_map.get(key)/(double)max_score * 100);
				System.out.println("occurnce="+occurence);
				if (occurence == 0) 
					occurence = 1;
				for(int j=0;j<occurence;j++) 
					tmp_arr.add(key);
			}
			
			for(int i=1;i<80;i++){
				solutions[0] = max_solution;
				int rnd = rand.nextInt(10);
				if (rnd < 2)
					solutions[i] = mutate(solutions[i]);
				else {
					String str1 = (String) Utils.rand(tmp_arr);
					String str2 = (String) Utils.rand(tmp_arr);
					solutions[i] = revalidate(crossOver(str1, str2));
				}
			}
			
			for(int i=80;i<100;i++)
				solutions[i] = generate();

		}
		return str;
	}
	
	public void saveMaxSolution(String solution, int score){
		if (score >= max_score){
			max_score = score;
			max_solution = solution;
		}
	}
	
	private int sameChars(){
		int same = 0;
		String str = this.word1 + this.word2;
		for(int i=0;i<target.length();i++)
			if (str.contains(target.substring(i, i+1))){
				same++;
			}
		
		
		return same;
	}
	
	private String mutate(String str){
		char one;
		char second;
		do {
			one = str.charAt(rand.nextInt(str.length()));
			second = str.charAt(rand.nextInt(str.length()));
		} while (one == second);
		return str.replace(one, second);
	}
	
	
	
	
	public static void main(String[] args) {
		String a = "SEND";
		String b = "MORE";
		String c = "MONEY";
		String[] sol_arry = new String[100];
		
		Solution sol = new Solution(a,b,c);
		System.out.println(sol.isValid("95671085"));

		//init solutions 
		sol_arry = sol.initSolutions(sol_arry);
		
		sol.solve(sol_arry, 200);
		
		System.out.println("-------");
		
		System.out.println(Utils.isNumeric("12B34"));
		
		System.out.println(sol.wordToNum("95671085"));
		System.out.println(sol.getSolution("9567108510652", "SENDMOREMONEY"));
		System.out.println(sol.fitness("24658734"));
		System.out.println(sol.fitness("95671085"));
		
	}

}
