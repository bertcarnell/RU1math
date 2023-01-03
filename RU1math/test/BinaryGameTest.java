import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class BinaryGameTest {

	@Test
	void testCreateCollection() {
		BinaryGame bg = new BinaryGame(3);
		assertEquals(6, bg.getCollectionSize());
		
		ArrayList<PairSet> pairs = bg.getCollection();
		assertEquals(0, pairs.get(0).getPairs().get(0).getIds().get(0));
		assertEquals(0, pairs.get(0).getPairs().get(0).getIds().get(1));
		
		assertEquals(1, pairs.get(0).getPairs().get(1).getIds().get(0));
		assertEquals(1, pairs.get(0).getPairs().get(1).getIds().get(1));
		
		assertEquals(2, pairs.get(0).getPairs().get(2).getIds().get(0));
		assertEquals(2, pairs.get(0).getPairs().get(2).getIds().get(1));
	}

	@Test
	void testBinaryGame() {
		BinaryGame bg = new BinaryGame(3);
		assertEquals(6, bg.getCollectionSize());
	}

	private void sumToOneTest(double[] probs) {
		double sum = Arrays.stream(probs).sum();
		assertEquals(1.0, sum, 1E-6);
	}

	private BinaryGame calculateProbabilitiesTest(int n) {
		// test base probabilities
		BinaryGame bg = new BinaryGame(n);
		double[] probs = bg.calculateProbabilities(null, null, null);
		int nFact = Util.factorial(n);
		double baseProb = 1.0 / (double) nFact;
		Arrays.stream(probs).forEach(d -> assertEquals(baseProb, d, 1E-6));
		sumToOneTest(probs);
		return bg;
	}
	
	@Test
	void testCalculateProbabilities() {
		double[] probs;
		
		calculateProbabilitiesTest(5);
		calculateProbabilitiesTest(4);
		
		BinaryGame bg3 = calculateProbabilitiesTest(3);

		// Create a test scenario of (0,1), (1,2), (2,0) with one match
		int[] firsts =  {0, 1, 2};
		int[] seconds = {1, 2, 0};
		ArrayList<ResultPairSet> resulttestsets = new ArrayList<ResultPairSet>(1);
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 1));
		
		probs = bg3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		double[] expected1 = {0.0, 1.0 / 3.0, 0.0, 1.0 / 3.0, 0.0, 1.0 / 3.0}; 
		assertArrayEquals(expected1, probs, 1E-6);
		
		// Create a test scenario of (0,1), (1,2), (2,0) with no matches
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 0));
		
		probs = bg3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		double[] expected2 = {0.5, 0.0, 0.5, 0.0, 0.0, 0.0}; 
		assertArrayEquals(expected2, probs, 1E-6);
		
		// Create a test scenario of (0,1), (1,2), (2,0) with two matches which fails because none have exactly two matches
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 2));
		
		probs = bg3.calculateProbabilities(resulttestsets, null, null);
		assertTrue(Arrays.stream(probs).allMatch(n -> n == 0.0));

		// Create a test scenario of (0,1), (1,2), (2,0) with three matches
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 3));
		
		probs = bg3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		double[] expected3 = {0.0, 0.0, 0.0, 0.0, 1.0, 0.0}; 
		assertArrayEquals(expected3, probs, 1E-6);
		
		// Create a test scenario of (0,1), (1,2), (2,0) with one match
		// followed by (0,1), (1,0), (2,2) with no matches
		int[] firsts2 =  {0, 1, 2};
		int[] seconds2 = {1, 0, 2};
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 1));
		resulttestsets.add(new ResultPairSet(new PairSet(firsts2, seconds2), 0));
		
		probs = bg3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		double[] expected4 = {0.0, 0.0, 0.0, 0.5, 0.0, 0.5}; 
		assertArrayEquals(expected4, probs, 1E-6);

		// followed by (0,0), (1,2), (2,1) with no matches
		int[] firsts3 =  {0, 1, 2};
		int[] seconds3 = {0, 2, 1};
		resulttestsets.add(new ResultPairSet(new PairSet(firsts3, seconds3), 0));

		probs = bg3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		double[] expected5 = {0.0, 0.0, 0.0, 0.0, 0.0, 1.0}; 
		assertArrayEquals(expected5, probs, 1E-6);

		// Exclude one match
		ArrayList<Pair> exclusions = new ArrayList<Pair>();
		exclusions.add(new Pair(0, 0));
		probs = bg3.calculateProbabilities(null, exclusions, null);
		sumToOneTest(probs);

		double[] expected6 = {0.0, 0.25, 0.25, 0.0, 0.25, 0.25};
		assertArrayEquals(expected6, probs, 1E-6);
		
		// Exclude two matches
		exclusions.add(new Pair(2, 2));
		probs = bg3.calculateProbabilities(null, exclusions, null);
		sumToOneTest(probs);

		double[] expected7 = {0.0, 0.0, 1.0/3.0, 0.0, 1.0/3.0, 1.0/3.0};
		assertArrayEquals(expected7, probs, 1E-6);
		
		// also include one match
		ArrayList<Pair> inclusions = new ArrayList<Pair>();
		inclusions.add(new Pair(0, 2));
	
		probs = bg3.calculateProbabilities(null, exclusions, inclusions);
		sumToOneTest(probs);

		// System.out.println(bg3.getCollection().toString());
		// System.out.println(Arrays.toString(probs));

		double[] expected8 = {0.0, 0.0, 0.5, 0.0, 0.0, 0.5};
		assertArrayEquals(expected8, probs, 1E-6);
	}
	
	@Test
	void testToMatrix() {
		BinaryGame bg3 = calculateProbabilitiesTest(3);

		// Create a test scenario of (0,1), (1,2), (2,0) with no matches
		int[] firsts =  {0, 1, 2};
		int[] seconds = {1, 2, 0};
		ArrayList<ResultPairSet> resulttestsets = new ArrayList<ResultPairSet>(1);
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 0));
		
		double[][] mat = bg3.toMatrix(resulttestsets, null, null);
		
		double[] expected1 = {0.5, 0.0, 0.5};
		double[] expected2 = {0.5, 0.5, 0.0};
		double[] expected3 = {0.0, 0.5, 0.5};
		assertArrayEquals(expected1, mat[0], 1E-6);
		assertArrayEquals(expected2, mat[1], 1E-6);
		assertArrayEquals(expected3, mat[2], 1E-6);
	}

}
