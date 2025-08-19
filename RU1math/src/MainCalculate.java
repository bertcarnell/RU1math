import java.io.File;
import java.util.ArrayList;

public class MainCalculate {

	public static void main(String[] args) {
		File input = new File("data/results.xml");
		ResultReader rr = new ResultReader(input);
		
		try {
			processSeason(rr, 1);
			processSeason(rr, 2);
			processSeason(rr, 8);
		}
		catch(Exception e) {
			System.out.println(e.toString());
		}

	}

	private static void processSeason(ResultReader rr, int seasonNumber) {
		for (Season season : rr.getSeasons()) {
			if (season.num != seasonNumber) {
				continue;
			}
			System.out.println(String.format("Season %d", season.num));
			Game bg;
			// if the extras and simple group list are empty, then it is a binary game
			if (season.groupBExtraList.size() == 0 &&
					season.groupAExtraList.size() == 0 &&
					season.groupList.size() == 0) {
				bg = new BinaryGame(season.groupAList.size());
			}
			else if (season.groupBExtraList.size() > 0 ||
					season.groupAExtraList.size() > 0) {
				bg = new BinaryGamePlus(season.groupAList.size(), 
						season.groupAExtraList.size(), 
						season.groupBExtraList.size());
			}
			else if (season.groupList.size() > 0) {
				bg = new UnaryGame(season.groupList.size() / 2);
			}
			else {
				bg = new BinaryGame(1);
			}
			ArrayList<ResultPairSet> rpss = new ArrayList<ResultPairSet>();
			ArrayList<Pair> correctPairs = new ArrayList<Pair>();
			ArrayList<Pair> wrongPairs = new ArrayList<Pair>();
			
			for (int i = 0; i < season.episodes.size(); i++) {
				System.out.println(String.format("Episode %d", season.episodes.get(i).num));
				rpss.add(season.episodes.get(i).resultPairSet);
				for (Pair p : season.episodes.get(i).correctPairs) {
					correctPairs.add(p);
				}
				for (Pair p : season.episodes.get(i).wrongPairs) {
					wrongPairs.add(p);
				}
				double[][] probMat = bg.toMatrix(rpss, wrongPairs, correctPairs);
				
				System.out.print("\n\t");
				ArrayList<String> columnLabels = (season.groupList.size() > 0) ? season.groupList : season.groupBList;
				ArrayList<String> rowLabels = (season.groupList.size() > 0) ? season.groupList : season.groupAList;
				for (String s : columnLabels) {
					System.out.print(String.format("%s", s).substring(0, 3) + "  \t");
				}
				System.out.print("\n");
				for (int j = 0; j < probMat.length; j++) {
					System.out.print(String.format("%s", rowLabels.get(j)).substring(0, 3) + "  \t");
					for (int k = 0; k < probMat[j].length; k++) {
						System.out.print(String.format("%.4f\t", probMat[j][k]));
					}
					System.out.print("\n");
				}
			}
				
			if (season.groupAExtraList.size() > 0 ||
					season.groupBExtraList.size() > 0) {
				ArrayList<ResultPairSet> rpssExtra = new ArrayList<ResultPairSet>();
				ArrayList<Pair> correctPairsExtra = new ArrayList<Pair>();
				ArrayList<Pair> wrongPairsExtra = new ArrayList<Pair>();

				for (int i = 0; i < season.episodes.size(); i++) {
					System.out.println(String.format("Episode %d Extra", season.episodes.get(i).num));
					
					System.out.println("This is still not correct");
					
					rpssExtra.add(season.episodes.get(i).resultPairSetExtra);
					for (Pair p : season.episodes.get(i).correctPairsExtra) {
						correctPairsExtra.add(p);
					}
					for (Pair p : season.episodes.get(i).wrongPairsExtra) {
						wrongPairsExtra.add(p);
					}
					double[][] probMatExtra = ((BinaryGamePlus) bg).toExtraMatrix(rpssExtra, wrongPairsExtra, correctPairsExtra);
					
					System.out.print("\n\t");
					if (season.groupBExtraList.size() > 0) {
						for (String s : season.groupBExtraList) {
							System.out.print(String.format("%s", s).substring(0, 3) + "  \t");
						}
					}
					else {
						for (String s : season.groupBList) {
							System.out.print(String.format("%s", s).substring(0, 3) + "  \t");
						}
					}
					System.out.print("\n");
					for (int j = 0; j < probMatExtra.length; j++) {
						if (season.groupBExtraList.size() > 0) {
							System.out.print(String.format("%s", season.groupAList.get(j)).substring(0, 3) + "  \t");
						}
						else {
							System.out.print(String.format("%s", season.groupAExtraList.get(j)).substring(0, 3) + "  \t");
						}
						for (int k = 0; k < probMatExtra[j].length; k++) {
							System.out.print(String.format("%.4f\t", probMatExtra[j][k]));
						}
						System.out.print("\n");
					}
				}
			}
		}
	}

	private static void processResults(ResultReader rr) {
		for (Season season : rr.getSeasons()) {
			System.out.println(String.format("Season %d", season.num));
			Game bg;
			// if the extras and simple group list are empty, then it is a binary game
			if (season.groupBExtraList.size() == 0 &&
					season.groupAExtraList.size() == 0 &&
					season.groupList.size() == 0) {
				bg = new BinaryGame(season.groupAList.size());
			}
			else if (season.groupBExtraList.size() > 0 ||
					season.groupAExtraList.size() > 0) {
				bg = new BinaryGamePlus(season.groupAList.size(), 
						season.groupAExtraList.size(), 
						season.groupBExtraList.size());
			}
			else if (season.groupList.size() > 0) {
				bg = new UnaryGame(season.groupList.size() / 2);
			}
			else {
				bg = new BinaryGame(1);
			}
			ArrayList<ResultPairSet> rpss = new ArrayList<ResultPairSet>();
			ArrayList<Pair> correctPairs = new ArrayList<Pair>();
			ArrayList<Pair> wrongPairs = new ArrayList<Pair>();
			
			for (int i = 0; i < season.episodes.size(); i++) {
				System.out.println(String.format("Episode %d", season.episodes.get(i).num));
				rpss.add(season.episodes.get(i).resultPairSet);
				for (Pair p : season.episodes.get(i).correctPairs) {
					correctPairs.add(p);
				}
				for (Pair p : season.episodes.get(i).wrongPairs) {
					wrongPairs.add(p);
				}
				double[][] probMat = bg.toMatrix(rpss, wrongPairs, correctPairs);
				
				System.out.print("\n\t");
				ArrayList<String> columnLabels = (season.groupList.size() > 0) ? season.groupList : season.groupBList;
				ArrayList<String> rowLabels = (season.groupList.size() > 0) ? season.groupList : season.groupAList;
				for (String s : columnLabels) {
					System.out.print(String.format("%s", s).substring(0, 3) + "  \t");
				}
				System.out.print("\n");
				for (int j = 0; j < probMat.length; j++) {
					System.out.print(String.format("%s", rowLabels.get(j)).substring(0, 3) + "  \t");
					for (int k = 0; k < probMat[j].length; k++) {
						System.out.print(String.format("%.4f\t", probMat[j][k]));
					}
					System.out.print("\n");
				}
			}
				
			if (season.groupAExtraList.size() > 0 ||
					season.groupBExtraList.size() > 0) {
				ArrayList<ResultPairSet> rpssExtra = new ArrayList<ResultPairSet>();
				ArrayList<Pair> correctPairsExtra = new ArrayList<Pair>();
				ArrayList<Pair> wrongPairsExtra = new ArrayList<Pair>();

				for (int i = 0; i < season.episodes.size(); i++) {
					System.out.println(String.format("Episode %d Extra", season.episodes.get(i).num));
					
					System.out.println("This is still not correct");
					
					rpssExtra.add(season.episodes.get(i).resultPairSetExtra);
					for (Pair p : season.episodes.get(i).correctPairsExtra) {
						correctPairsExtra.add(p);
					}
					for (Pair p : season.episodes.get(i).wrongPairsExtra) {
						wrongPairsExtra.add(p);
					}
					double[][] probMatExtra = ((BinaryGamePlus) bg).toExtraMatrix(rpssExtra, wrongPairsExtra, correctPairsExtra);
					
					System.out.print("\n\t");
					if (season.groupBExtraList.size() > 0) {
						for (String s : season.groupBExtraList) {
							System.out.print(String.format("%s", s).substring(0, 3) + "  \t");
						}
					}
					else {
						for (String s : season.groupBList) {
							System.out.print(String.format("%s", s).substring(0, 3) + "  \t");
						}
					}
					System.out.print("\n");
					for (int j = 0; j < probMatExtra.length; j++) {
						if (season.groupBExtraList.size() > 0) {
							System.out.print(String.format("%s", season.groupAList.get(j)).substring(0, 3) + "  \t");
						}
						else {
							System.out.print(String.format("%s", season.groupAExtraList.get(j)).substring(0, 3) + "  \t");
						}
						for (int k = 0; k < probMatExtra[j].length; k++) {
							System.out.print(String.format("%.4f\t", probMatExtra[j][k]));
						}
						System.out.print("\n");
					}
				}
			}
		}
	}
}
