
public class ResultPairSet {
	private PairSet pairSet;
	private int nCorrect;
	
	public ResultPairSet(int nPairs, int nCorrect) {
		pairSet = new PairSet(nPairs);
		this.nCorrect = nCorrect;
	}

	public ResultPairSet(int nPairs) {
		this(nPairs, 0);
	}
	
	public ResultPairSet(PairSet pairSet, int nCorrect) {
		this.pairSet = pairSet;
		this.nCorrect = nCorrect;
	}
	
	public PairSet get() {
		return pairSet;
	}
	
	public int getCorrect() {
		return nCorrect;
	}
}
