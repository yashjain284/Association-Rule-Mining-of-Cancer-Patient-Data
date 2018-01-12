import java.util.TreeSet;

public class Rule {
	
	TreeSet<String> leftRule;
	TreeSet<String> rightRule;
	
	@SuppressWarnings("unchecked")
	public Rule(Object object,Object object2) {
		leftRule = (TreeSet<String>) object;
		rightRule = (TreeSet<String>) object2;
	}
}
