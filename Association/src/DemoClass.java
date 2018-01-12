import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.TreeSet;

public class DemoClass {
	
	public static double minimumSupport;
	public static double minimumConfindence;
	public static Queue<Rule> myQueue = new LinkedList<>();
	public static HashMap<String, HashSet<String>> bodyMap = new HashMap<String, HashSet<String>>();
	public static HashMap<String, HashSet<String>> headMap = new HashMap<String, HashSet<String>>();
	public static HashMap<String, Integer> totalFreqItemSet = new HashMap<>();
	
    //BASIC TEST FOR ABC AND ABD SETS
	
	
	public static void runQuery() {
		Scanner sc = new Scanner(System.in);
		System.out.print("Template type [1/2/3] : ");
		int type = Integer.parseInt(sc.nextLine());
		HashSet<String> result = new HashSet<>();
		while(type != 4) {
			if(type == 1 || type == 2) {
				result = getInput(sc, type);
			} else if(type == 3) {
				System.out.print("Combination [1OR1/1OR2/1AND1/1AND2/2OR2/2AND2] : ");
				String combination = sc.nextLine();
				if(combination.contains("OR")) {
					String types[] = combination.split("OR");
					HashSet<String> result1 = getInput(sc, Integer.parseInt(types[0]));
					HashSet<String> result2 = getInput(sc, Integer.parseInt(types[1]));
					result = new HashSet<>();
					result.addAll(result1);result.addAll(result2);
				} else {
					String types[] = combination.split("AND");
					HashSet<String> result1 = getInput(sc, Integer.parseInt(types[0]));
					HashSet<String> result2 = getInput(sc, Integer.parseInt(types[1]));
					result = new HashSet<>();
					for(String s:result1) {
						if(result2.contains(s))
							result.add(s);
					}
				}
			}
			printAnswer(result);
			System.out.print("Template type [1/2/3] : ");
			String s = "";
			// Hack to fix input error
			while((s = sc.nextLine()).equals(""));
			type = Integer.parseInt(s);
		}
	}

	public static HashSet<String> getInput(Scanner sc, int type) {
		if(type == 1) {
			System.out.print("Where ?[RULE/BODY/HEAD] : ");
			String where = sc.nextLine().toUpperCase();
			System.out.print("how many ?[ANY/NONE/1] : ");
			String howMany = sc.nextLine().toUpperCase();
			System.out.print("genes ?[G<1..100>_<Up/Down>] : ");
			String genes[] = sc.nextLine().split(",");
			return TemplateParser.templateOneParser(where, howMany, genes);
		} else if(type == 2) {
			System.out.print("Where ?[RULE/BODY/HEAD] : ");
			String where = sc.nextLine().toUpperCase();
			System.out.print("count [INSERT ANY NUMBER] : ");
			int n = Integer.parseInt(sc.nextLine());
			return TemplateParser.templateTwoParser(where, n);
		}
		System.out.println("Invalid input type");
		return null;
	}
	
	public static void main(String[] args) {
		try {
			Scanner sc  = new Scanner(System.in);
			
			//Ask for thresholds.
			System.out.print("Enter Support Threshold : ");
			double sup = Double.parseDouble(sc.nextLine());
			FrequentItemsetGenerator.setSupportThreshold(sup);
			System.out.print("Enter Confidence Threshold : ");
			double conf = Double.parseDouble(sc.nextLine());
			RuleGenerator.setConfidenceThreshold(conf);
			
			//Generate Frequent Itemsets using Apriori Algorithm
			FrequentItemsetGenerator.loadDataSet();
			
			//Use the frequent itemsets generated and generate association rules.
			System.out.println("\n\nASSOCUATION RULE GENERATION........");
			RuleGenerator.ruleSetGenerator();
			
			//Running the query handle.
			runQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void printFreqSets() {
		Iterator it = totalFreqItemSet.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey()+" "+pair.getValue());
		}
	}

	public static void printAnswer(HashSet<String> result) {
		System.out.println("\n\nRules");
		System.out.println("************************************************");
		for(String s:result)
			System.out.println(s);
		if(result.size()==0)
			System.out.println("No rules generated!!");
		System.out.println("************************************************");
		
		System.out.println("Total number of rules generated = "+result.size());
		System.out.println("************************************************\n\n");
	}	
}
