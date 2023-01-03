import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Util {
	public static final int factorial(int n) {
		assert(n >= 0 & n <= 12);
		if (n == 0) {
			return 1;
		}
		int ret = 1;
		for (int i = 0; i < n; i++) {
			ret *= (i + 1);
		}
		return ret;
	}
	
	private static final void swap(int[] obj, int i, int j) {
		int temp = obj[i];
		obj[i] = obj[j];
		obj[j] = temp;
	}
	
	public static final int[][] allPermutations(int n) {
		int nrows = Util.factorial(n);
		int[][] ret = new int[nrows][n];
		int[] entries = new int[n];
		int[] indices = new int[n];
		
		Arrays.fill(indices, 0);
		for (int i = 0; i < n; i++) {
			entries[i] = i;
		}
		
		int rowi = 0;
		System.arraycopy(entries, 0, ret[rowi], 0, n);
		
		int i = 0;
		while (i < n && rowi < nrows - 1) {
		    if (indices[i] < i) {
		    	int swap1 = i % 2 == 0 ? 0 : indices[i];
		    	int swap2 = i;
				rowi++;
				Util.swap(entries, swap1, swap2);
				System.arraycopy(entries, 0, ret[rowi], 0, n);
				indices[i]++;
				i = 0;
		    }
		    else {
		        indices[i] = 0;
		        i++;
		    }
		}
		return ret;
	}

	// there are n choose 2 possible pairings out of n elements
	// They can be arranged in sets of (n-1)*(n-3)*(n-5)*...*(1)
	public static final ArrayList<ArrayList<Integer[]> > allCombinationSets(int n) {
		if (n % 2 != 0) {
			System.out.println("n must be even");
		}
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < n; i++) {
			l.add(i);
		}
		ArrayList<ArrayList<Integer[]> > combos = new ArrayList<ArrayList<Integer[]>>();
		recursiveChoice(l, new ArrayList<Integer[]>(), combos);
		return combos;
	}
	
	private static void recursiveChoice(ArrayList<Integer> l, ArrayList<Integer[]> choice,
			ArrayList<ArrayList<Integer[]> > combos) {
		ArrayList<Integer[]> nextChoice;
		ArrayList<Integer> nextl;
		if (l.size() == 0) {
			combos.add(choice);
		}
		else {
			for (int j = 1; j < l.size(); j++) {
				if (choice.size() > 0) {
					Integer[] temp = {0, 0};
					nextChoice = new ArrayList<Integer[]>(Collections.nCopies(choice.size(), temp));
					Collections.copy(nextChoice, choice);
				} 
				else {
					nextChoice = new ArrayList<Integer[]>();
				}
				Integer[] tempj = {0, 0};
				tempj[0] = l.get(0);
				tempj[1] = l.get(j);
				nextChoice.add(tempj);
				nextl = new ArrayList<Integer>(Collections.nCopies(l.size(), 0));
				Collections.copy(nextl, l);
				nextl.remove(j);
				nextl.remove(0);
				recursiveChoice(nextl, nextChoice, combos);
			}
		}
	}

	public static boolean isValidPermutation(int[][] mat, int n) {
		int sum = 0;
		int firstsum = 0;
		for (int i = 0; i < mat.length; i++) {
			sum = Arrays.stream(mat[i]).sum();
			if (sum != (n-1)*n/2) {
				return false;
			}
		}
		for (int j = 0; j < n; j++) {
			// can't turn this into a stream because of the reverse index
			sum = 0;
			for (int i = 0; i < mat.length; i++) {
				sum += mat[i][j];
			}
			if (j == 0) {
				firstsum = sum;
			} else {
				if (firstsum != sum) {
					return false;
				}
			}
		}
		return true;
	}
}
