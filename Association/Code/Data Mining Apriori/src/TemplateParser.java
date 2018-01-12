import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

public class TemplateParser {
		
	public static void main2(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Template type ? ");
		int type = sc.nextInt();
		HashSet<String> result = new HashSet<>();
		while(type != 4) {
			if(type == 1 || type == 2) {
				result = DemoClass.getInput(sc, type);
			} else if(type == 3) {
				String combination = sc.nextLine();
				if(combination.contains("OR")) {
					String types[] = combination.split("OR");
					HashSet<String> result1 = DemoClass.getInput(sc, Integer.parseInt(types[0]));
					HashSet<String> result2 = DemoClass.getInput(sc, Integer.parseInt(types[1]));
					result = new HashSet<>();
					result.addAll(result1);result.addAll(result2);
				} else {
					String types[] = combination.split("AND");
					HashSet<String> result1 = DemoClass.getInput(sc, Integer.parseInt(types[0]));
					HashSet<String> result2 = DemoClass.getInput(sc, Integer.parseInt(types[1]));
					result = new HashSet<>();
					for(String s:result1) {
						if(result2.contains(s))
							result.add(s);
					}
				}
			}
			DemoClass.printAnswer(result);
			type = sc.nextInt();
		}
	}
	
	public static HashSet<String> templateOneParser(String where, String howMany, String what[]) {
		HashSet<String> result = new HashSet<>();
		int type = checkType(howMany);
		Iterator it = null;
		if(where.equals("BODY") || where.equals("RULE"))
			it = DemoClass.bodyMap.entrySet().iterator();
		else if(where.equals("HEAD"))
			it = DemoClass.headMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
	        String content = null;
	        if(where.equals("BODY") || where.equals("HEAD")) {
	        	content = (String) pair.getKey();
	        	if(checkEntry(what, content, type)) {
	        		HashSet<String> hset = (HashSet<String>) pair.getValue();
	        		for (String temp : hset) {
	        			String rule = "";
	        			if(where.equals("BODY"))
	        				rule = content +" ==> "+temp;
	        			else
	        				rule = temp +" ==> "+content;
//	        			System.out.println(content +" ==> "+temp);
	        			result.add(rule);
	        		}
	        	}
	        } else {
	        	content = (String) pair.getKey();
	        	HashSet<String> hset = (HashSet<String>) pair.getValue();
	        	for (String temp : hset) {
	        		String ruleContent = content+""+temp;
	        		if(checkEntry(what, ruleContent, type)) {
//	        			System.out.println(content +" ==> "+temp);
	        			result.add(content +" ==> "+temp);
	        		}
	        	}
	        }
	    }
	    return result;
	}
	

	public static HashSet<String> templateTwoParser(String where, int n) {
		HashSet<String> result = new HashSet<>();
		Iterator it = null;
		if(where.equals("BODY") || where.equals("RULE"))
			it = DemoClass.bodyMap.entrySet().iterator();
		else if(where.equals("HEAD"))
			it = DemoClass.headMap.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
	        //System.out.println(pair.getKey() + " = " + pair.getValue());
			String content = null;
	        if(where.equals("BODY") || where.equals("HEAD")) {
	        	content = (String) pair.getKey();
	        	if(checkCount(content, n)) {
	        		HashSet<String> hset = (HashSet<String>) pair.getValue();
	        		for (String temp : hset) {
	        			String rule = "";
	        			if(where.equals("BODY"))
	        				rule = content +" ==> "+temp;
	        			else
	        				rule = temp +" ==> "+content;
//	        			System.out.println(rule);
	        			result.add(rule);
	        		}
	        	}
	        } else {
	        	content = (String) pair.getKey();
	        	HashSet<String> hset = (HashSet<String>) pair.getValue();
	        	for (String temp : hset) {
	        		String ruleContent = content+""+temp;
	        		if(checkCount(ruleContent, n)) {
//	        			System.out.println(content +" ==> "+temp);
	        			result.add(content +" ==> "+temp);
	        		}
	        	}
	        }
		}
		return result;
	}
	
	public static boolean checkCount(String content, int count) {
		String temp[] = content.split("_");
		if(temp.length-1 >= count) return true;
		else return false;
	}
	
	//type 0 NONE
	//type 1 ONE
	//type 2 Any
	public static int checkType(String howMany) {
		if(howMany.equals("NONE")) return 0;
		else if(howMany.equals("ONE")) return 1;
		else return 2;
	}

	public static boolean checkEntry(String input[], String content, int type) {
		boolean flag = false;
		for(int i=0;i<input.length;i++) {
			if(content.contains(input[i])) {
				if(type == 2) return true;
				else if(type == 0) return false;
				else if(type == 1 && flag) return false;
				flag = true;
			}
		}
		if(type == 0)
			return true;
		else if(type == 2)
			return false;
		else
			return flag;
	}
}
