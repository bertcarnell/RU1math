import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class UnaryGameTest {

	@Test
	void testCreateCollection() {
		UnaryGame ug = new UnaryGame(5);
		PairSet ps = ug.getCollection().get(0);
		assertEquals(5, ps.getPairs().size());
		assertEquals(0, ps.getPairs().get(0).getIds().get(0));
		assertEquals(1, ps.getPairs().get(0).getIds().get(1));
	}

	private void sumToOneTest(double[] probs) {
		double sum = Arrays.stream(probs).sum();
		assertEquals(1.0, sum, 1E-6);
	}

	private void singleCalculateProbabilities(int nPairs) {
		UnaryGame ug = new UnaryGame(nPairs);
		double[] probs = ug.calculateProbabilities(null, null, null);
		Arrays.stream(probs).forEach(d -> assertEquals(1.0 / (double) ug.getCollectionSize(), d, 1E-6));
		sumToOneTest(probs);
	}
	
	@Test
	void testCalculateProbabilities() {
		singleCalculateProbabilities(6);
		singleCalculateProbabilities(5);
		singleCalculateProbabilities(4);
		singleCalculateProbabilities(3);

		UnaryGame ug3 = new UnaryGame(3);

		// Create a test scenario of (0,1), (3,5), (4,2) with one match
		int[] firsts =  {0, 3, 4};
		int[] seconds = {1, 5, 2};
		ArrayList<ResultPairSet> resulttestsets = new ArrayList<ResultPairSet>(1);
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 1));
		
		double[] probs = ug3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		Arrays.stream(probs).forEach(d -> assertTrue(d == 0.0 || d == 1.0 / 6.0));
		
		// Create a test scenario of (0,1), (1,2), (2,0) with no matches
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 0));
		
		probs = ug3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		Arrays.stream(probs).forEach(d -> assertTrue(d == 0.0 || d == 1.0 / 8.0));
		
		// Create a test scenario of (0,1), (1,2), (2,0) with two matches which fails because none have exactly two matches
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 2));
		
		probs = ug3.calculateProbabilities(resulttestsets, null, null);
		assertTrue(Arrays.stream(probs).allMatch(n -> n == 0.0));

		// Create a test scenario of (0,1), (1,2), (2,0) with three matches
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 3));
		
		probs = ug3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);
		Arrays.stream(probs).forEach(d -> assertTrue(d == 0.0 || d == 1.0));

		// Create a test scenario of one match
		// followed by no matches
		int[] firsts2 =  {0, 2, 4};
		int[] seconds2 = {1, 3, 5};
		resulttestsets.clear();
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 1));
		resulttestsets.add(new ResultPairSet(new PairSet(firsts2, seconds2), 0));
		
		probs = ug3.calculateProbabilities(resulttestsets, null, null);
		sumToOneTest(probs);

		// Exclude one match
		ArrayList<Pair> exclusions = new ArrayList<Pair>();
		exclusions.add(new Pair(0, 1));
		probs = ug3.calculateProbabilities(null, exclusions, null);
		sumToOneTest(probs);
		Arrays.stream(probs).forEach(d -> assertTrue(d == 0.0 || d == 1.0 / 12.0));
	}

	@Test
	void testToMatrix() {
		UnaryGame ug3 = new UnaryGame(3);

		double[][] mat2 = ug3.toMatrix(null, null, null);
		Arrays.stream(mat2).forEach(d -> sumToOneTest(d));

		int[] firsts =  {0, 3, 4};
		int[] seconds = {1, 5, 2};
		ArrayList<ResultPairSet> resulttestsets = new ArrayList<ResultPairSet>(1);
		resulttestsets.add(new ResultPairSet(new PairSet(firsts, seconds), 0));
		
		double[][] mat = ug3.toMatrix(resulttestsets, null, null);
		Arrays.stream(mat).forEach(d -> sumToOneTest(d));
	}

	@Test
	void testUnaryGame() {
		UnaryGame ug = new UnaryGame(5);
		assertEquals(9*7*5*3, ug.getCollectionSize());

		ug = new UnaryGame(4);
		assertEquals(7*5*3, ug.getCollectionSize());

		ug = new UnaryGame(3);
		assertEquals(5*3, ug.getCollectionSize());
	}

}
