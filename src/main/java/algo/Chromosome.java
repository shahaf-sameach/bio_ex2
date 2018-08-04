package main.java.algo;

import main.java.algo.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Chromosome {

    private char sign;
    private String target;
    private String solution;
    private String sequence;
    private int divider;

    private final Random rand = new Random();

    public Chromosome(String seq1, String seq2, String target, char sign) {
        this.sequence = seq1 + seq2;
        this.divider = seq1.length();
        this.target = target;
        this.sign = sign;
        this.solution = randSolution();
    }


    public Chromosome(Chromosome c) {
        this.sequence = c.getSequence();
        this.divider = c.getDivider();
        this.target = c.getTarget();
        this.sign = c.getSign();
        this.solution = randSolution();
    }


    private Chromosome(Chromosome chromosome, String sol) {
        this.sequence = chromosome.getSequence();
        this.divider = chromosome.getDivider();
        this.target = chromosome.getTarget();
        this.sign = chromosome.getSign();
        this.solution = sol;
    }

    // return initial valid random solution
    private String randSolution(){
        String sol = this.sequence;
        String numbers = "0123456789";
        for(int i = 0; i < sol.length(); i++)
        {
            int rnd = rand.nextInt(numbers.length());
            //first char of each word cannot be zero
            if ((i == 0) || (i == this.divider))
                rnd = rand.nextInt(numbers.length() - 1) + 1;

            if (!(StringUtils.isNumeric(sol.charAt(i))))
            {
                sol = sol.replace(sol.charAt(i), numbers.charAt(rnd));
                numbers = numbers.replace(numbers.substring(rnd, rnd+1),"");
            }
        }
        return sol;
    }

    public String getSolution() {
        return solution;
    }


    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getDivider() {
        return divider;
    }

    public void setDivider(int divider) {
        this.divider = divider;
    }

    public int hashcode(){
        return this.solution.hashCode();
    }

    //return a mutate string of a solution
    public Chromosome mutate() {
        return new Chromosome(this, StringUtils.mutateString(this.solution));
    }

    // return a string resulting crossover between two optional solutions (strings)
    // concat begin of one solution to the end of another
    public Chromosome crossOver(Chromosome c){
        String str1 = this.solution;
        String str2 = c.getSolution();
        return new Chromosome(this, StringUtils.crossOverStrings(str1, str2, this.divider)).revalidate();

    }

    //convert solution to valid solution
    private Chromosome revalidate(){
        Map<Character,Character> map = StringUtils.getCharMap(this.sequence, this.solution);
        String validSolution = "";

        for(int i=0; i< this.sequence.length() ;i++){
            if (!map.containsKey(this.sequence.charAt(i))){
                char key;
                do {
                    key = Integer.toString(rand.nextInt(10)).charAt(0);
                } while (map.containsValue(key));
                map.put(this.sequence.charAt(i), key);
            }
        }

        for(int i=0; i<this.sequence.length();i++){
            validSolution = validSolution + map.get(this.sequence.charAt(i));
        }

        while (validSolution.charAt(0) == '0'){
            char char1 = StringUtils.getRandomChar(validSolution);
            validSolution = StringUtils.swap(validSolution, '0', char1);
        }

        return new Chromosome(this, validSolution);
    }

    public String calcEquation(){
        int num1 = Integer.parseInt(this.solution.substring(0, this.divider));
        int num2 = Integer.parseInt(this.solution.substring(this.divider));
        int result;

        if (sign == '*'){
            result = num1 * num2;
        } else if (sign == '-'){
            result = num1 - num2;
        } else if (sign == '/'){
            if (num2 == 0) {
                return randSolution();
            } else {
                result = num1 / num2;
            }
        } else{
            result = num1 + num2;
        }
        return Integer.toString(result);
    }


    public boolean isCorrect(){
        String result = calcEquation();
        String targetCmp = StringUtils.numToWord(this.sequence, result, this.solution);

        if (targetCmp.length() != target.length())
            return false;

//        if (!isMathValid()){
//            return false;
//        }

        int same_char_count=0;
        for(int i = 0;i<targetCmp.length();i++){
            if (!StringUtils.isNumeric(targetCmp.charAt(i))){
                if (targetCmp.charAt(i) == target.charAt(i)) {
                    same_char_count++;
                }else{
                    return false;
                }
            }
        }

        if (same_char_count == StringUtils.sameCharsNum(this.sequence, target))
            return true;

        return false;
    }

    // returned the target string as number string based on number map
    public String wordToNum(){
        String src = target;
        for(int i=0;i<target.length();i++){
            int indx = sequence.indexOf(target.charAt(i));
            if (indx > -1)
                src = src.replace(target.charAt(i), solution.charAt(indx));
        }
        return src;
    }

//    private boolean isMathValid(){
//        int num1 = Integer.parseInt(solution.substring(0, divider));
//        int num2 = Integer.parseInt(solution.substring(divider));
//        if ((sign == '-') && (num1 < num2)){
//            return false;
//        }
//        if ((sign == '/') && ((num1 % num2) != 0)){
//            return false;
//        }
//        return true;
//    }


    public Map getSolutionMap(){
        Map map = new HashMap();
        String allLetters = this.sequence + this.target;

        String num = this.solution + calcEquation();
        for(int i=0; i< num.length(); i++){
            map.put(allLetters.charAt(i),num.charAt(i));
        }
        return map;
    }


    public char getSign() {
        return sign;
    }

    public String getTarget() {
        return target;
    }
}
