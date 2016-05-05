package bio_ex2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class Solution {
	
	private String word1;
	private String word2;
	private String words;
	private String target;
	private String sign;
	private int max_score = 0;
	private String max_solution;
	private Random rand = new Random();
	private Utils util = new Utils();
	
	public Solution(String word1, String word2, String target){
		this(word1 ,word2 ,target ,"+");
	}
	
	public Solution(String word1, String word2, String target, String sign){
		this.word1 = word1;
		this.word2 = word2;
		this.target = target;
		this.sign = sign;
		this.words = word1 + word2;
	}
	
	public String[] initSolutions(String[] solutions_arr){
		for(int i=0;i<solutions_arr.length;i++)
			solutions_arr[i] = generate();
		return solutions_arr;
	}
	
	public String generate(){
		String possible_sol = words;
		String numbers = "0123456789";
		Random rand = new Random();
		for(int i = 0; i < possible_sol.length(); i++)
		{
			int rnd = rand.nextInt(numbers.length());
			if (i == 0)
				rnd = rand.nextInt(numbers.length() - 1) + 1;
			
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
		String calc_result = calcEquation(sol);
		String opt_sol = wordToNum(sol);
		int min_len =  Math.min(opt_sol.length(), calc_result.length());
		for(int i=0; i<min_len; i++){
			if (Utils.isNumeric(opt_sol.charAt(i)))
				if (opt_sol.charAt(i) == calc_result.charAt(i)) 
					score++;
		}
		
		double same_char_score = (double)score/(double)sameCharsNum();
		double len_score = (double)(opt_sol.length()-Utils.dist(opt_sol, target))/(double)opt_sol.length();
		int total_score = (int)((0.8*same_char_score + 0.2*len_score) * 100);
		return total_score;
	}
	
	public String numToWord(String num_map, String target){
		for(int i=0;i<target.length();i++){
			int indx = num_map.indexOf(target.charAt(i));
			if (indx > -1)
				target = target.replace(target.charAt(i), words.charAt(indx));
		}
		return target;
	}
	
	public String wordToNum(String num_map){
		String src = target;
		for(int i=0;i<target.length();i++){
			int indx = words.indexOf(target.charAt(i));
			if (indx > -1)
				src = src.replace(target.charAt(i), num_map.charAt(indx));
		}
		return src;
	}
	
	public boolean isValid(String sol){
		String result = calcEquation(sol);
		String target_comp = numToWord(sol, result);
		if (target_comp.length() != target.length())
			return false;
		
		int same_char_count=0;
		for(int i = 0;i<target_comp.length();i++){
			if (!Utils.isNumeric(target_comp.charAt(i))){
				if (target_comp.charAt(i) == target.charAt(i)) {
					same_char_count++;
				}else{
					return false;
				}
			}
		}
		
		if (same_char_count == sameCharsNum())
			return true;
		return false;
	}
	
	private String calcEquation(String eq){
		int num1 = Integer.parseInt(eq.substring(0, word1.length()));
		int num2 = Integer.parseInt(eq.substring(word1.length(), eq.length()));
		int result;
		if (sign == "*"){
			result = num1 + num2;
		} else if (sign == "-"){
			result = num1 - num2;
		} else if (sign == "/"){
			result = num1 / num2;
		} else{
			result = num1 + num2;
		}
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
		Map<Character,Character> map = getMap(num); 
		String valid_str = "";

		for(int i=0;i<words.length();i++){
			if (!map.containsKey(words.charAt(i))){
				char key;
				do {
					key = Integer.toString(rand.nextInt(10)).charAt(0);
				} while (map.containsValue(key));
				map.put(words.charAt(i),key);
			}
		}
		
		for(int i=0;i<words.length();i++){
			valid_str = valid_str + map.get(words.charAt(i));
		}
		
		while (valid_str.charAt(0) == '0'){
			char char1 = util.getRandomChar(valid_str);
			valid_str = util.swap(valid_str, '0', char1);
		}
			
		return valid_str;
	}
	
	public Map<Character,Character> getMap(String str_num){
		Map<Character,Character> map = new HashMap<Character,Character>(); 
		for(int i=0;i<words.length();i++){
			if (!map.containsValue(str_num.charAt(i)))
				map.put(words.charAt(i),str_num.charAt(i));
		}
		return map;
		
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
//				System.out.println("occurnce="+occurence);
				if (occurence == 0) 
					occurence = 1;
				for(int j=0;j<occurence;j++) 
					tmp_arr.add(key);
			}
			System.out.println("tmp_arr size=" + tmp_arr.size());
			solutions[0] = max_solution;
			for(int i=1;i<100;i++){
				int rnd = rand.nextInt(100);
				if (rnd < 15){
					solutions[i] = generate();
				}
				else {
					String str1 = (String) Utils.rand(tmp_arr);
					String str2 = (String) Utils.rand(tmp_arr);
					solutions[i] = revalidate(crossOver(str1, str2));
				}
				if (rand.nextInt(100) < 5){
					solutions[i] = mutate(solutions[i]);
				}
			}				
		}
		return str;
	}
	
	public void saveMaxSolution(String solution, int score){
		if (score >= max_score){
			max_score = score;
			max_solution = solution;
		}
	}
	
	private int sameCharsNum(){
		int same = 0;
		for(int i=0;i<target.length();i++)
			if (words.contains(target.substring(i, i+1))){
				same++;
			}
		
		return same;
	}
	
	private String mutate(String str){
		String exclud_numbers = util.getExcludeNumbers(str);
		if (exclud_numbers.length() > 0){
			char char1 = util.getRandomChar(str);
			char char2 = util.getRandomChar(exclud_numbers);
			str = util.swap(str, char1, char2);
		} else{
			str = util.swap(str);
		}
		return str;
	}
	
	
	public static void main(String[] args) {
		String a = "SEND";
		String b = "MORE";
		String c = "MONEY";
		String[] sol_arry = new String[180];
		
		Solution sol = new Solution(a,b,c);
		System.out.println(sol.isValid("95671085"));

		//init solutions 
		sol_arry = sol.initSolutions(sol_arry);
		
		sol.solve(sol_arry, 1000);
		
		System.out.println("-------");
		
//		System.out.println(Utils.isNumeric("12B34"));
		System.out.println(sol.isValid("93471083"));
//		System.out.println(sol.numToWord("93471083", "MONEY"));
		System.out.println(sol.isValid("95671085"));
//		System.out.println(sol.wordToNum("95671085"));
//		System.out.println(sol.getSolution("9567108510652", "SENDMOREMONEY"));

		System.out.println(sol.fitness("93471083"));
		System.out.println(sol.fitness("95671085"));
		
	}

}
