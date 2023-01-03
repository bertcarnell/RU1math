import java.util.ArrayList;
import java.util.Arrays;

public class UnaryGame extends Game {
	private int nPairs;
	
	public UnaryGame(int nPairs) {
		super(Util.factorial(nPairs));
		this.nPairs = nPairs;
		createCollection();
	}
	
	@Override
	public void createCollection() {
		ArrayList<ArrayList<Integer[]> > allCombinationSets = Util.allCombinationSets(nPairs*2);
		
		for (int i = 0; i < allCombinationSets.size(); i++) {
			ArrayList<Pair> pairs = new ArrayList<Pair>(nPairs);
			for (Integer[] pair : allCombinationSets.get(i)) {
				pairs.add(new Pair(pair[0].intValue(), pair[1].intValue()));
			}
			this.setPairSet(i, new PairSet(pairs));
		}
	}
	
	private int[] createAvailable(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs, 
			ArrayList<Pair> correctPairs) {
		int collectionSize = this.getCollectionSize();
		int[] available = new int[collectionSize];
		boolean isOrdered = false;
		
		Arrays.fill(available, 1);
		
		// The available options have to match all the correctPairs
		if (correctPairs != null) {
			for (Pair correctPair : correctPairs) {
				for (int i = 0; i < collectionSize; i++) {
					if (!this.getCollection().get(i).has(correctPair, isOrdered)) {
						available[i] = 0;
					}
				}
			}
		}
		
		// The available options need to match none of the wrongPairs
		if (wrongPairs != null) {
			for (Pair wrongPair : wrongPairs) {
				for (int i = 0; i < collectionSize; i++) {
					if (this.getCollection().get(i).has(wrongPair, isOrdered)) {
						available[i] = 0;
					}
				}
			}
		}
		
		// the available options need to have the right number of pairs in common with each results set.
		if (results != null) {
			for (ResultPairSet result : results) {
				for (int i = 0; i < collectionSize; i++) {
					if (this.getCollection().get(i).numberMatches(result.get(), isOrdered) != result.getCorrect()) {
						available[i] = 0;
					}
				}
			}
		}
		return available;
	}
	
	@Override
	public double[] calculateProbabilities(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs, 
			ArrayList<Pair> correctPairs) {
		int collectionSize = this.getCollectionSize();
		double[] probs = new double[collectionSize];
		int[] available = createAvailable(results, wrongPairs, correctPairs);
		int totalAvailable = Arrays.stream(available).sum();
		
		for (int i = 0; i < collectionSize; i++) {
			probs[i] = (available[i] == 1) ? 1.0 / (double) totalAvailable : 0.0;
		}

		return probs;
	}
	
	@Override
	public double[][] toMatrix(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs, 
			ArrayList<Pair> correctPairs) {
		double[] probs = calculateProbabilities(results, wrongPairs, correctPairs);
		double[][] mat = new double[nPairs * 2][nPairs * 2];
		Arrays.stream(mat).forEach(row -> Arrays.fill(row,  0.0));
		
		for (int i = 0; i < this.getCollectionSize(); i++) {
			for (Pair p : this.getCollection().get(i).getPairs()) {
				mat[p.getIds().get(0)][p.getIds().get(1)] += probs[i];
				mat[p.getIds().get(1)][p.getIds().get(0)] += probs[i];
			}
		}
		
		return mat;
	}
}
