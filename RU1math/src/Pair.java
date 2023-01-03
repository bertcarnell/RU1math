import java.util.ArrayList;

public class Pair {
	private ArrayList<Integer> ids;
	private ArrayList<String> names;
	
	public Pair(int first, int second, String firstName, String secondName) {
		ids = new ArrayList<Integer>(2);
		names = new ArrayList<String>(2);
		ids.add(0,  first);
		ids.add(1, second);
		names.add(0,  firstName);
		names.add(1,  secondName);
	}
	
	public Pair(int first, int second){
		this(first, second, "A", "B");
	}

	boolean equals(Pair obj, boolean isOrdered) {
		if (isOrdered) {
			return ids.get(0) == obj.getIds().get(0) && ids.get(1) == obj.getIds().get(1);
		} else {
			return (ids.get(0) == obj.getIds().get(0) && ids.get(1) == obj.getIds().get(1)) ||
					(ids.get(0) == obj.getIds().get(1) && ids.get(1) == obj.getIds().get(0));
		}
	}
	
	ArrayList<String> getNames() {
		return names;
	}
	
	ArrayList<Integer> getIds() {
		return ids;
	}
	
	public String toString() {
		return String.format("(%d,%d)", ids.get(0), ids.get(1));
	}
}
