import java.util.ArrayList;
import java.util.Arrays;

public class BinaryGamePlus extends Game {
	private int nPairs;
	private int extraGroupA = 0;
	private int extraGroupB = 0;
	private ArrayList<PairSet> extraPairs;
	
	public BinaryGamePlus(int nPairs, int extraGroupA, int extraGroupB) {
		super(Util.factorial(nPairs));
		extraPairs = new ArrayList<PairSet>();
		this.nPairs = nPairs;
		this.extraGroupA = extraGroupA;
		this.extraGroupB = extraGroupB;
		createCollection();
		createExtraCollection();
	}
	
	@Override
	public void createCollection() {
		int[] firsts = new int[nPairs];
		// Set the firsts to be the same for all pairs
		// Set the seconds to all permutations
		for (int i = 0; i < nPairs; i++) {
			firsts[i] = i;
		}
		int[][] seconds = Util.allPermutations(nPairs);
		
		for (int i = 0; i < seconds.length; i++) {
			this.setPairSet(i, new PairSet(firsts, seconds[i]));
		}
	}
	
	public void createExtraCollection() {
		if (extraGroupA > 0) {
			int[][] firsts = new int[extraGroupA][nPairs];
			int[] seconds = new int[nPairs];
			for (int i = 0; i < nPairs; i++) {
				seconds[i] = i;
			}
			for (int i = 0; i < extraGroupA; i++) {
				for (int j = 0; j < nPairs; j++) {
					firsts[i][j] = i;
				}
			}
			
			for (int i = 0; i < firsts.length; i++) {
				extraPairs.add(new PairSet(firsts[i], seconds)); 
			}
		} 
		else if (extraGroupB > 0) {
			int[][] seconds = new int[extraGroupB][nPairs];
			int[] firsts = new int[nPairs];
			for (int i = 0; i < nPairs; i++) {
				firsts[i] = i;
			}
			for (int i = 0; i < extraGroupB; i++) {
				for (int j = 0; j < nPairs; j++) {
					seconds[i][j] = i;
				}
			}
			
			for (int i = 0; i < seconds.length; i++) {
				extraPairs.add(new PairSet(firsts, seconds[i])); 
			}
		}
		else {
			System.out.println("Error in BinaryGamePlus");
		}
	}
	
	private int[] createAvailable(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs, 
			ArrayList<Pair> correctPairs,
			ArrayList<PairSet> collection) {
		int collectionSize = collection.size();
		int[] available = new int[collectionSize];
		
		Arrays.fill(available, 1);
		
		// The available options have to match all the correctPairs
		if (correctPairs != null) {
			for (Pair correctPair : correctPairs) {
				for (int i = 0; i < collectionSize; i++) {
					if (!collection.get(i).has(correctPair, true)) {
						available[i] = 0;
					}
				}
			}
		}
		
		// The available options need to match none of the wrongPairs
		if (wrongPairs != null) {
			for (Pair wrongPair : wrongPairs) {
				for (int i = 0; i < collectionSize; i++) {
					if (collection.get(i).has(wrongPair, true)) {
						available[i] = 0;
					}
				}
			}
		}
		
		// the available options need to have the right number of pairs in common with each results set.
		if (results != null) {
			for (ResultPairSet result : results) {
				for (int i = 0; i < collectionSize; i++) {
					if (collection.get(i).numberMatches(result.get(), true) != result.getCorrect()) {
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
		int[] available = createAvailable(results, wrongPairs, correctPairs, this.getCollection());
		int totalAvailable = Arrays.stream(available).sum();
		
		for (int i = 0; i < collectionSize; i++) {
			probs[i] = (available[i] == 1) ? 1.0 / (double) totalAvailable : 0.0;
		}

		return probs;
	}
	
	public double[] calculateExtraProbabilities (
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs, 
			ArrayList<Pair> correctPairs) {
		int collectionSize = this.extraPairs.size();
		double[] probs = new double[collectionSize];
		int[] available = createAvailable(results, wrongPairs, correctPairs, this.extraPairs);
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
		double[][] mat = new double[nPairs][nPairs];
		Arrays.stream(mat).forEach(row -> Arrays.fill(row,  0.0));
		
		for (int i = 0; i < this.getCollectionSize(); i++) {
			for (Pair p : this.getCollection().get(i).getPairs()) {
				mat[p.getIds().get(0)][p.getIds().get(1)] += probs[i];
			}
		}
		
		return mat;
	}

	public double[][] toExtraMatrix(
			ArrayList<ResultPairSet> results, 
			ArrayList<Pair> wrongPairs, 
			ArrayList<Pair> correctPairs) {
		double[] probs = calculateExtraProbabilities(results, wrongPairs, correctPairs);
		double[][] mat;
		if (extraGroupA > extraGroupB) {
			mat = new double[extraGroupA][nPairs];
		} 
		else {
			mat = new double[nPairs][extraGroupB];
		}
		Arrays.stream(mat).forEach(row -> Arrays.fill(row,  0.0));
		
		for (int i = 0; i < this.extraPairs.size(); i++) {
			for (Pair p : this.extraPairs.get(i).getPairs()) {
				assert(p.getIds().get(0) < mat.length);
				assert(p.getIds().get(1) < mat[0].length);
				mat[p.getIds().get(0)][p.getIds().get(1)] += probs[i];
			}
		}
		
		return mat;
	}
}
