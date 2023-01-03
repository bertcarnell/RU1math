import java.util.ArrayList;

public class PairSet {
	ArrayList<Pair> pairs;
	
	public PairSet(int n) {
		pairs = new ArrayList<Pair>(n);
	}
	
	public PairSet(int[] firsts, int[] seconds) {
		assert(firsts.length == seconds.length);
		pairs = new ArrayList<Pair>(firsts.length);
		
		for (int i = 0; i < firsts.length; i++) {
			pairs.add(i,  new Pair(firsts[i], seconds[i]));
		}
	}
	
	public PairSet(ArrayList<Pair> pairs) {
		this.pairs = pairs;
	}
	
	public ArrayList<Pair> getPairs() {
		return pairs;
	}
	
	public Pair get(int i) {
		assert(i >= 0 & i < pairs.size());
		return pairs.get(i);
	}
	
	public boolean has(Pair pair, boolean isOrdered) {
		for (Pair pairi : pairs) {
			if (pairi.equals(pair, isOrdered)) {
				return true;
			}
		}
		return false;
	}
	
	public int numberMatches(PairSet pairset, boolean isOrdered) {
		int matches = 0;
		for (Pair pairi : pairs) {
			for (Pair pairj : pairset.getPairs()) {
				if (pairi.equals(pairj, isOrdered)) {
					matches++;
				}
			}
		}
		return matches;
	}
	
	public String toString() {
		return pairs.toString();
	}
}
