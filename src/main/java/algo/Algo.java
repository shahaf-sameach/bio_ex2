package main.java.algo;

import main.java.algo.utils.StringUtils;
import main.java.algo.utils.Utils;

import java.util.*;


public class Algo {
	
	private String word1;
	private String word2;
	private String target;
	private char sign;
	private Random rand = new Random();

	
	public Algo(String word1, String word2, String target, char sign){
		this.word1 = word1;
		this.word2 = word2;
		this.target = target;
		this.sign = sign;
	}
	
	// returns an array with random initial solutions 
	public Chromosome[] initPopulation(int populationNum){
	    Chromosome[] population = new Chromosome[populationNum];
	    for(int i=0 ;i<populationNum; i++)
            population[i] = new Chromosome(this.word1, this.word2, this.target, this.sign);

	    return population;
	}

	
	//return fitness score of a solution
	public int fitness(Chromosome c){

		//calc score based on similar chars 
		double similarScore = similarCharsScore(c);
		double lenScore = StringUtils.lenScore(c.wordToNum(), c.getTarget());
		
		//calc score as weighted scores of similar and diff
		int total_score = (int)((0.8*similarScore + 0.2*lenScore) * 100);
		
		if (!c.isCorrect())
			total_score--;
		
		return total_score;
	}
	
	//calc score based on similar chars 
	private double similarCharsScore(Chromosome c){
		int score = 0;
		String result = c.calcEquation();
		String solution = c.wordToNum();

		int minLen =  Math.min(solution.length(), result.length());

		for(int i=0; i<minLen; i++){
			// the char is numeric
			if (StringUtils.isNumeric(solution.charAt(i))){
				// chars are the same and also the same on the letter target and source (words) 
				if (solution.charAt(i) == result.charAt(i))
					score++;
			}
		}
		
		return (double)score/(double) StringUtils.sameCharsNum(c.getSequence(), c.getTarget());
	}

	
	public Chromosome run(int populationNumber, int generations){
        Chromosome[] population = initPopulation(populationNumber);
		
		for(int k=0;k<generations;k++){
			System.out.println("iteration: " + k);

			Chromosome c = Arrays.stream(population).filter(chromosome -> chromosome.isCorrect()).findFirst().orElse(null);
            if (c != null)
                return c;

			showStats(population);
			population = stepGeneration(population);
		}
		return null;
	}
	
	private void showStats(Chromosome[] population){
		int max = Arrays.stream(population).map(c -> fitness(c)).max(Integer::compare).get();
        int min = Arrays.stream(population).map(c -> fitness(c)).min(Integer::compare).get();
        int avg = Arrays.stream(population).map(c -> fitness(c)).mapToInt(Integer::intValue).sum() / population.length;

        System.out.println("max=" + max + " min=" + min + " avg=" + avg);
	}
	
	//return solution-fitness score map
	private Map<Chromosome,Integer> getFitnessMap(Chromosome[] population){
		Map<Chromosome,Integer> fitnessMap = new HashMap<>();

		for (Chromosome c : population)
			fitnessMap.put(c, fitness(c));
		
		return fitnessMap;
	}
	
	private Chromosome[] stepGeneration(Chromosome[] population){
        Map<Chromosome,Integer> fitnessMap = getFitnessMap(population);
        Map<Chromosome,Integer> sortedFitnessMap = Utils.sortByValue(fitnessMap);

        Chromosome[] newPopulation = new Chromosome[population.length];

		int i=0;
		//copy the best 10% solutions to the next generation (elitisem)
		for (Map.Entry<Chromosome, Integer> entry : sortedFitnessMap.entrySet()){
			newPopulation[i] = entry.getKey();
			if (i > sortedFitnessMap.size()/10)
				break;
			i++;
		}

        Chromosome[] occurrence = getOccurrenceArray(fitnessMap);

        for(;i<population.length;i++){
			// with 0.1 prob create a new random solution (avoid local minimum)
			if (rand.nextInt(100) < 10){
				newPopulation[i] = new Chromosome(population[i]);
			}
			else {
                newPopulation[i] = Utils.randElement(occurrence).crossOver(Utils.randElement(occurrence));
				
				//mutate 20% of the crossovers
				if (rand.nextInt(100) < 20)
					newPopulation[i] = population[i].mutate();

			}
		}

		return newPopulation;
	}
	
	//return occurence array of all solution based on their fitness score
	private Chromosome[] getOccurrenceArray(Map<Chromosome,Integer> fitness_map){
		ArrayList<Chromosome> tmp_arr = new ArrayList<>();
		
		int maxScore = fitness_map.values().stream().max(Integer::compare).get();

		for (Map.Entry<Chromosome, Integer> e : fitness_map.entrySet()){
			int occurrence = (int)((double)e.getValue() / (double)maxScore * 100);
			if (occurrence == 0)
				occurrence = 1;

			for(int j=0; j<occurrence; j++)
				tmp_arr.add(e.getKey());
		}
		
		Chromosome[] occurrenceMap = new Chromosome[tmp_arr.size()];
        occurrenceMap = tmp_arr.toArray(occurrenceMap);
		
		return occurrenceMap;
	}

	
	public static void main(String[] args) {
		Algo alg = new Algo("SEND","MORE","MONEY",'+');

		Chromosome sol = alg.run(200, 1000);
		if (sol != null) {
			System.out.println("sol=" + sol.getSolution());
			System.out.println(sol.getSolutionMap());
		} else {
            System.out.println("Couldn't find Solution...");
        }
		
		
		
//		Scanner reader = new Scanner(System.in);
//		System.out.println("Enter the first word: ");
//		String word1 = reader.nextLine();
//
//		System.out.println("Enter the math action: ");
//		char sign = reader.nextLine().charAt(0);
//
//		System.out.println("Enter the second word: ");
//		String word2 = reader.nextLine();
//
//		System.out.println("Enter the result: ");
//		String target = reader.nextLine();
//
//		System.out.println("Enter size of population: ");
//		int population = reader.nextInt();
//
//		System.out.println("Enter number of generations: ");
//		int generations = reader.nextInt();
//
//		Algo alg2 = new Algo(word1.toUpperCase(),word2.toUpperCase(),target.toUpperCase(),sign);
//
//		Chromosome res = alg2.run(population, generations);
//
//		if (res == null){
//			System.out.println("couldn't find solution after " + generations + " iterations");
//		} else {
//			System.out.println("found a valid solution= "  + res.getSolutionMap());
//		}

		
	}

}
