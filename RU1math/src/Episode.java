import java.util.ArrayList;

public class Episode {
	public int num = 0;
	public ArrayList<Pair> wrongPairs;
	public ArrayList<Pair> correctPairs;
	public ResultPairSet resultPairSet;
	
	public ArrayList<Pair> wrongPairsExtra;
	public ArrayList<Pair> correctPairsExtra;
	public ResultPairSet resultPairSetExtra;
	
	public Episode() {
		wrongPairs = new ArrayList<Pair>();
		correctPairs = new ArrayList<Pair>();
		wrongPairsExtra = new ArrayList<Pair>();
		correctPairsExtra = new ArrayList<Pair>();
	}
}
