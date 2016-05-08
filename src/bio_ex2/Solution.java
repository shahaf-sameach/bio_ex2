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
	
	// returns an array with random initial solutions 
	public String[] initSolutions(String[] solutions_arr){
		for(int i=0;i<solutions_arr.length;i++)
			solutions_arr[i] = generate();
		return solutions_arr;
	}
	
	// return initial valid random solution
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
	
	// return a string resulting crossover between two optional solutions (strings)
	// concat begin of one solution to the end of another
	public String crossOver(String sol1, String sol2){
		String str1 = sol1.substring(0,this.word1.length());
		String str2 = sol2.substring(this.word1.length(),sol2.length());
		if (rand.nextInt(2) % 2 == 0){
			str1 = sol2.substring(0,this.word1.length());
			str2 = sol1.substring(this.word1.length(),sol1.length());
		}

		return str1 + str2;
	}
	
	//return fitness score of a solution
	public int fitness(String sol){
		String opt_sol = wordToNum(sol);
		
		//calc score based on similar chars 
		double same_char_score = similarCharsScore(sol);
		
		double len_score = lenScore(opt_sol);
		
		//calc score as weighted scores of similar and diff
		int total_score = (int)((0.8*same_char_score + 0.2*len_score) * 100);
		return total_score;
	}
	
	//calc score based on similar chars 
	private double similarCharsScore(String sol){
		int score = 0;
		String result = calcEquation(sol);
		String opt_sol = wordToNum(sol);
		int min_len =  Math.min(opt_sol.length(), result.length());
		for(int i=0; i<min_len; i++){
			if (Utils.isNumeric(opt_sol.charAt(i)))
				if (opt_sol.charAt(i) == result.charAt(i)) 
					score++;
		}
		
		return (double)score/(double)sameCharsNum();
	}
	
	//returned score based on length diff between optional solution to target
	private double lenScore(String sol){
		return (double)(sol.length()-Utils.dist(sol, target))/(double)sol.length();
	}
	
	//convert string on number to String of letter based on specific map
	public String numToWord(String num_map, String target){
		for(int i=0;i<target.length();i++){
			int indx = num_map.indexOf(target.charAt(i));
			if (indx > -1)
				target = target.replace(target.charAt(i), words.charAt(indx));
		}
		return target;
	}
	
	// returned the target string as number string based on number map
	public String wordToNum(String num_map){
		String src = target;
		for(int i=0;i<target.length();i++){
			int indx = words.indexOf(target.charAt(i));
			if (indx > -1)
				src = src.replace(target.charAt(i), num_map.charAt(indx));
		}
		return src;
	}
	
	// check if a solution is correct
	public boolean isCorrect(String sol){
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
	
	//calc the equation result of a possible solution
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
	
	//return letter-number map of a solution
	public Map getSolution(String num){
		Map map = new HashMap(); 
		String solution = words + this.target;
		num = num + calcEquation(num);
		for(int i=0;i<num.length();i++){
			map.put(solution.charAt(i),num.charAt(i));
		}
		return map;
	}
	
	//convert solution to valid solution 
	public String revalidate(String num){
		Map<Character,Character> map = getCharMap(num); 
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
	
	//return letter-number map of number string
	public Map<Character,Character> getCharMap(String str_num){
		Map<Character,Character> map = new HashMap<Character,Character>(); 
		for(int i=0;i<words.length();i++){
			if (!map.containsValue(str_num.charAt(i)))
				map.put(words.charAt(i),str_num.charAt(i));
		}
		return map;
		
	}
	
	public String solve(int solutions_number, int iterations){
		String[] solutions = new String[solutions_number];
		Map<String,Integer> fitness_map = new HashMap<String,Integer>();
		
		solutions = initSolutions(solutions);
		
		for(int k=0;k<iterations;k++){
			System.out.println("iteration: " + k);
			
			//check to see if there is a correct solution
			for (String sol : solutions){
				if (isCorrect(sol)){
					System.out.println("valid solution = " + sol);
					System.out.println(getSolution(sol));
					return sol;
				}
			}
			
			fitness_map = getFitnessMap(solutions);
			calcStats(solutions);
			System.out.println("max=(" + max_solution + "," + max_score + ")");
			System.out.println(fitness_map.size());
			
			String[] occurnce_array = getOccurnessArray(fitness_map);
			System.out.println("occurnce_array size=" + occurnce_array.length);
			
			solutions = stepGeneration(solutions, occurnce_array);
				
		}
		return "";
	}
	
	private void calcStats(String[] solutions){
		int[] stats = new int[solutions.length];
		for(int i = 0;i<solutions.length;i++){
			stats[i] = fitness(solutions[i]);
		}
		
		System.out.println("max=" + "" + "min=" + "" + "avg=" + "");
	}
	
	//return solution-fitness score map
	private Map<String,Integer> getFitnessMap(String[] solutions){
		Map<String,Integer> fitness_map = new HashMap<String,Integer>();
		
		String sol;
		int score = 0;
		for (int i=0;i<solutions.length;i++){
			sol = solutions[i];
			score = fitness(sol);
			saveMaxSolution(sol, score);
			fitness_map.put(sol, score);	
		}
		
		return fitness_map;
	}
	
	private String[] stepGeneration(String[] solutions, String[] occurnce){
		//copy the best solution to the next generation
		solutions[0] = max_solution;
		
		for(int i=1;i<solutions.length;i++){
			int rnd = rand.nextInt(100);
			// with 0.15 prob create a new random solution (avoid local minimum)
			if (rnd < 15){
				solutions[i] = generate();
			}
			else {
				String str1 = (String) Utils.rand(occurnce);
				String str2 = (String) Utils.rand(occurnce);
				solutions[i] = revalidate(crossOver(str1, str2));
			}
			
			//mutate 5% of the new solutions
			if (rand.nextInt(100) < 5){
				solutions[i] = mutate(solutions[i]);
			}
		}
		return solutions;
	}
	
	//return occurence array of all solution based on their fitness score
	private String[] getOccurnessArray(Map<String,Integer> fitness_map){
		ArrayList<String> tmp_arr = new ArrayList<String>();
		
		for (String key : fitness_map.keySet()){
			int occurence = (int)((double)fitness_map.get(key)/(double)max_score * 100);
			if (occurence == 0) 
				occurence = 1;
			for(int j=0;j<occurence;j++) 
				tmp_arr.add(key);
		}
		
		String[] tmp_string_arry = new String[tmp_arr.size()];
		for(int i=0;i<tmp_arr.size();i++){
			tmp_string_arry[i] = tmp_arr.get(i);
		}
		
		return tmp_string_arry;
	}
	
	//save the solution if it has the max score 
	public void saveMaxSolution(String solution, int score){
		if (score >= max_score){
			max_score = score;
			max_solution = solution;
		}
	}
	
	//calc the number of similar chars between the equation upper side to the result
	private int sameCharsNum(){
		int same = 0;
		for(int i=0;i<target.length();i++)
			if (words.contains(target.substring(i, i+1))){
				same++;
			}
		
		return same;
	}
	
	//return a mutate string of a solution
	private String mutate(String str){
		String exclud_numbers = Utils.getExcludeNumbers(str);
		if (exclud_numbers.length() > 0){
			char char1 = Utils.getRandomChar(str);
			char char2 = Utils.getRandomChar(exclud_numbers);
			str = Utils.swap(str, char1, char2);
		} else{
			str = Utils.swap(str);
		}
		return str;
	}
	
	public static void main(String[] args) {
		String a = "SEND";
		String b = "MORE";
		String c = "MONEY";
		
		Solution sol = new Solution(a,b,c);
		System.out.println(sol.isCorrect("95671085"));

		sol.solve(200, 1000);
		
	}

}
