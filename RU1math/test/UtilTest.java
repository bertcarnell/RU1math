import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class UtilTest {
	private void singlePermutationTest(int n) {
		int[][] ap = Util.allPermutations(n);
		assertEquals(n, ap[0].length);
		assertEquals(Util.factorial(n), ap.length);
		assertTrue(Util.isValidPermutation(ap, n));
	}
	
	@Test
	void testAllPermutations() {
		singlePermutationTest(3);
		singlePermutationTest(4);
		singlePermutationTest(5);
	}
	
	@Test
	void testFactorial() {
		assertEquals(1, Util.factorial(0));
		assertEquals(1, Util.factorial(1));
		assertEquals(2, Util.factorial(2));
		assertEquals(6, Util.factorial(3));
		assertEquals(24, Util.factorial(4));
		assertEquals(120, Util.factorial(5));
		assertEquals(720, Util.factorial(6));
	}

	private void singleAllCombinationSetsTest(int n) {
		ArrayList<ArrayList<Integer[]> > ac = Util.allCombinationSets(n);
		int prod = 1;
		for (int i = n - 1; i > 1; i -= 2) {
			prod *= i;
		}
		assertEquals(prod, ac.size());
		assertEquals(n/2, ac.get(0).size());
		assertEquals(2, ac.get(0).get(0).length);
		
		for (ArrayList<Integer[]> a : ac) {
			int sum = 0;
			for (Integer[] b : a) {
				sum += Arrays.stream(b).mapToInt(i -> i.intValue()).sum();
			}
			// sum of 0 through (6-1)
			assertEquals((n-1)*(n-1+1)/2, sum);
		}
	}

	@Test
	void testAllCombinationSets() {
		singleAllCombinationSetsTest(6);
		singleAllCombinationSetsTest(8);
		singleAllCombinationSetsTest(10);
	}

}
