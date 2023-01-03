import java.util.ArrayList;

public abstract class Game {
	private ArrayList<PairSet> pairSetCollection;
	
	public Game(int n) {
		pairSetCollection = new ArrayList<PairSet>(n);
	}
	
	abstract public void createCollection();

	abstract public double[] calculateProbabilities(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs,
			ArrayList<Pair> correctPairs);
	
	abstract public double[][] toMatrix(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs,
			ArrayList<Pair> correctPairs);

	public void setPairSet(int i, PairSet pairSet) {
		pairSetCollection.add(i, pairSet);
	}
	
	public int getCollectionSize() {
		return pairSetCollection.size();
	}
	
	public ArrayList<PairSet> getCollection() {
		return pairSetCollection;
	}

}
