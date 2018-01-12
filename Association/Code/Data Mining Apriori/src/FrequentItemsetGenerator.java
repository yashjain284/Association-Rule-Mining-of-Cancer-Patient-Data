import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

public class FrequentItemsetGenerator {
	public static ArrayList<ArrayList<String>> dataset= new ArrayList<>();
	public static HashMap<String, Integer> length1 = new HashMap<>();
	public static HashMap<Integer, HashMap<String[], Integer>> freqItemSet = new HashMap<>();
	public static double supportThreshold=0;
	public static int transactions = 0;
	public static int finalFreqItemSet = 0;
	
	public static void loadDataSet() {
		try {
			BufferedReader br = new BufferedReader(new FileReader("associationruletestdata.txt"));
			String line = "";
			int i=0;
			String prefix = "G";
			while((line=br.readLine())!=null) {
				transactions++;
				ArrayList<String> temp = new ArrayList<String>();
				String entry[] = line.split("\\t");
				for(int k=0;k<entry.length;k++) {
					int count = 0;
					prefix = "G"+(k+1);
					String gene = prefix+"_"+entry[k];
//					String gene = entry[k];
					if(k == entry.length-1)
						gene = entry[k];
					temp.add(gene);
					if(length1.containsKey(gene))
						count = length1.get(gene);
					length1.put(gene, count+1);
				}
				i++;
				dataset.add(temp);
			}
			br.close();
			pruneInitialSet();
			int fromLength = 1;
			while(generateCandiateItemsets(fromLength)) {
				fromLength++;
				finalFreqItemSet += freqItemSet.get(fromLength).size();
				System.out.println("No of frequent itemsets of length "+fromLength+ " = "+freqItemSet.get(fromLength).size());
			}
			System.out.println("************************************************");
			System.out.println("Total frequent itemsets = "+DemoClass.totalFreqItemSet.size());
			System.out.println("************************************************");
			freqItemSet = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void printMap(HashMap<String[], Integer> hashMap) {
		Iterator it = hashMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			String[] temp = (String[]) pair.getKey();
			for(int i=0;i<temp.length;i++)
				System.out.print(temp[i] +" ");
			System.out.print(" value = "+pair.getValue());
			System.out.println();
		}
	}

	public static void pruneInitialSet() {
		HashMap<String[], Integer> result = new HashMap<>();
		Iterator it = length1.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			double count = (1.0 * (int) pair.getValue())/transactions;
			if(count >= supportThreshold) {
				String genes[] = new String[1];
				genes[0] = (String) pair.getKey();
				result.put(genes, (Integer) pair.getValue());
				DemoClass.totalFreqItemSet.put("["+genes[0]+"]", (Integer) pair.getValue());
			}
		}
		freqItemSet.put(1, result);
		finalFreqItemSet += result.size();
		System.out.println("************************************************");
		System.out.println("Total frequent itemsets of length 1 = "+result.size());
		
	}
	
	public static boolean generateCandiateItemsets(int fromLength){
		ArrayList<String[]> localSet = new ArrayList<>();
		HashMap<String[], Integer> temp = freqItemSet.get(fromLength);
		Object[] keys = temp.keySet().toArray();
		for(int i=0;i<keys.length-1;i++) {
			for(int j=i+1;j<keys.length;j++) {
				String genes1[] = (String[]) keys[i];
				String genes2[] = (String[]) keys[j];
				String result[] = mergeSet(genes1, genes2);
				if(result != null)
					localSet.add(result);
			}
		}
		pruneCandidateSet(fromLength+1,localSet);
		if(freqItemSet.get(fromLength+1).size() > 0)
			return true;
		return false;
	}

	private static void pruneCandidateSet(int ofLength, ArrayList<String[]> localSet) {
		int i,j,k;
		HashMap<String[], Integer> result = new HashMap<>();
		for(i=0;i<localSet.size();i++) {
			String genes[] = localSet.get(i);
			String genesString = "";
			int count = 0;
			for(j=0;j<dataset.size();j++) {
				ArrayList<String> currDataSet = dataset.get(j);
				for(k=0;k<genes.length;k++) {
					if(!currDataSet.contains(genes[k]))break;
					genesString += genes[k]+",";
				}
				if(k == genes.length)count++;
			}
			double sc = (1.0*count)/transactions;
			if(sc>=supportThreshold) {
				result.put(genes, count);
				String temp = genesString.substring(0, genesString.length());
				TreeSet<String> genesSet = new TreeSet<>(Arrays.asList(genes));
				DemoClass.totalFreqItemSet.put(genesSet.toString(), count);
				TreeSet<String> tr1 = new TreeSet<String>();
				Rule ob1 = new Rule(genesSet,tr1);
				DemoClass.myQueue.add(ob1);
			}
		}
		freqItemSet.put(ofLength, result);
	}

	private static String[] mergeSet(String[] genes1, String[] genes2) {
		if(genes1.length != genes2.length)
			return null;
		int length = genes1.length;
		String result[] = new String[length+1];
		for(int i=0;i<length-1;i++) {
			if(!genes1[i].equals(genes2[i]))
				return null;
			result[i] = genes1[i];
		}
		result[length-1] = genes1[length-1];
		result[length] = genes2[length-1];
		return result;
	}

	public static void setSupportThreshold(double sup) {
		supportThreshold = sup;
	}
	
	
}
