
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

public class RuleGenerator {
	public static double confidenceThreshold=0;
	public static int ruleCount = 0;
	
	
	public static boolean verifyConfidence(String itemSet,String leftRuleString) {
//		System.out.println("itemset = "+itemSet+"left = "+leftRuleString);
		int itemSetSupport = DemoClass.totalFreqItemSet.get(itemSet);
		double leftSupport = DemoClass.totalFreqItemSet.get(leftRuleString);
		double confidence = (1.0 * itemSetSupport) / leftSupport;
		
		return confidence >= confidenceThreshold;
//		return true;
	}

	public static void ruleSetGenerator() throws IOException{
		
		//OpenFileFor
		File myfile = new File("rules"+confidenceThreshold+".txt");
		FileWriter fr = new FileWriter(myfile);

		while (DemoClass.myQueue.isEmpty() == false) {
			
			//Popping a rule from the queue.
			Rule currentQueueElement = DemoClass.myQueue.remove();
			
			// Creating left and right parts of the rules,
			TreeSet<String> left = currentQueueElement.leftRule;
			TreeSet<String> right = currentQueueElement.rightRule;
			
			// String Equivalent of rules(TreeSet -> rules)
			String leftRuleString = left.toString();
			String rightRuleString = right.toString();
			
			//Combining tree Sets to get support count for the frequentItemSet.
			TreeSet<String> combined = new TreeSet<String>();
			combined.addAll(left);
			combined.addAll(right);
			String itemSet = combined.toString();
			
			//Condition to check for Confidence Threshold.
			if (verifyConfidence(itemSet,leftRuleString)) {
				
				HashSet<String> temp = new HashSet<String>();
				//If the right half of the rule is not empty.
				if (right.size() > 0) {
					// Add an entry to bodyMap
					if (DemoClass.bodyMap.get(leftRuleString) != null)
						temp = DemoClass.bodyMap.get(leftRuleString);
//					System.out.println(leftRuleString +" and temp = "+temp);
					int len1 = temp.size();
					temp.add(rightRuleString);
					int len2 = temp.size();
					if(len2>len1) {
						fr.write(leftRuleString+" -> "+rightRuleString+"\n");
						//System.out.println("Adding to bodymap "+leftRuleString+" "+temp);
						ruleCount++;
					}
					
					DemoClass.bodyMap.put(leftRuleString, temp);
					// Add an entry to headMap
					temp = new HashSet<String>();
					if (DemoClass.headMap.get(rightRuleString) != null)
						temp = DemoClass.headMap.get(rightRuleString);
					// To check for duplicates
					temp.add(leftRuleString);
					DemoClass.headMap.put(rightRuleString, temp);
				}
				// Create children rules and add it to the queue.
				TreeSet<String> tempLeft = new TreeSet<>();
				TreeSet<String> tempRight = new TreeSet<>();
				tempLeft.addAll(left);
				tempRight.addAll(right);
				for (String item : tempLeft) {
					//We remove from left and add to right.
					left.remove(item);
					right.add(item);
					//Keep adding children to queue untill the left rule is empty.
					if (left.size() > 0) {
						Rule newObject = new Rule(left.clone(), right.clone());
						//System.out.println("adding to queue "+newObject.leftRule+" "+newObject.rightRule);
						DemoClass.myQueue.add(newObject);
					}
					left.add(item);
					right.remove(item);
				}
			}//Condition for confidence ends here.
		}//Queue Loop ends here.
		fr.close();
		System.out.println("************************************************");
		System.out.println("Number of Rules Generated : "+ruleCount);
		System.out.println("************************************************");
	}//ruleGenerator function ends here. 

	public static void setConfidenceThreshold(double conf) {
		// TODO Auto-generated method stub
		confidenceThreshold = conf;
	}
}