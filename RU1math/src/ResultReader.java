import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ResultReader {
	public static int CORRECT_POSIT = 0;
	public static int WRONG_POSIT = 1;
	public static int CORRECT_POSIT_EXTRA = 2;
	public static int WRONG_POSIT_EXTRA = 3;
	private ArrayList<Season> seasons;
	
	public ResultReader(File file) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(file);
			seasons = new ArrayList<Season>();
			
			NodeList nodeList = document.getDocumentElement().getElementsByTagName("Season");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					seasons.add(parseSeason(node));
				}
			}
		} catch (ParserConfigurationException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} catch (SAXException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
	}
	
	public ArrayList<Season> getSeasons() {
		return seasons;
	}
	
	private Season parseSeason(Node node) {
		Season season = new Season();
		season.num = Integer.parseInt(node.getAttributes().getNamedItem("Num").getNodeValue().toString());
		NodeList seasonChildren = node.getChildNodes();
		for (int j = 0; j < seasonChildren.getLength(); j++) {
			Node seasonChild = seasonChildren.item(j);
			if (seasonChild.getNodeName() == "Players") {
				NodeList groupNodeList = seasonChild.getChildNodes();
				for (int k = 0; k < groupNodeList.getLength(); k++) {
					Node playersNode = groupNodeList.item(k);
					if (playersNode.getNodeName() == "GroupA") {
						season.groupAList = parsePlayers(playersNode);
					} 
					else if (playersNode.getNodeName() == "GroupB") {
						season.groupBList = parsePlayers(playersNode);
					}
					else if (playersNode.getNodeName() == "GroupAExtra") {
						season.groupAExtraList = parsePlayers(playersNode);
					}
					else if (playersNode.getNodeName() == "GroupBExtra") {
						season.groupBExtraList = parsePlayers(playersNode);
					}
					else if (playersNode.getNodeName() == "Group") {
						season.groupList = parsePlayers(playersNode);
					}
				}
			} else if (seasonChild.getNodeName() == "Episodes") {
				NodeList episodeNodeList = seasonChild.getChildNodes();
				for (int k = 0; k < episodeNodeList.getLength(); k++) {
					Node episodeNode = episodeNodeList.item(k);
					if (episodeNode.getNodeName() == "Episode") {
						season.episodes.add(parseEpisode(episodeNode, 
								season.groupAList, season.groupBList,
								season.groupAExtraList, season.groupBExtraList,
								season.groupList));
					}
				}
			}
		}
		
		return season;
	}
	
	private ArrayList<String> parsePlayers(Node node) {
		ArrayList<String> ret = new ArrayList<String>();
		
		NodeList players = node.getChildNodes();
		for (int i = 0; i < players.getLength(); i++) {
			if (players.item(i).getNodeName() == "Player") {
				ret.add(players.item(i).getTextContent());
			}
		}
		
		return ret;
	}
	
	private Episode parseEpisode(
			Node node, 
			List<String> groupAList,
			List<String> groupBList,
			List<String> groupAListExtra,
			List<String> groupBListExtra,
			List<String> groupList) {
		Episode episode = new Episode();
		episode.num = Integer.parseInt(node.getAttributes().getNamedItem("Num").getNodeValue().toString());
		// truths and ceremony
		NodeList episodeParts = node.getChildNodes();
		for (int i = 0; i < episodeParts.getLength(); i++) {
			Node episodePart = episodeParts.item(i);
			if (episodePart.getNodeName() == "Truths") {
				ArrayList<ArrayList<Pair> > both = parseTruths(episodePart, groupAList, groupBList,
						groupAListExtra, groupBListExtra, groupList);
				episode.correctPairs = both.get(CORRECT_POSIT);
				episode.wrongPairs = both.get(WRONG_POSIT);
				episode.correctPairsExtra = both.get(CORRECT_POSIT_EXTRA);
				episode.wrongPairsExtra = both.get(WRONG_POSIT_EXTRA);
			} 
			else if (episodePart.getNodeName() == "Ceremony") {
				episode.resultPairSet = parseCeremony(episodePart, groupAList, groupBList,
						groupAListExtra, groupBListExtra, groupList, false);
				episode.resultPairSetExtra = parseCeremony(episodePart, groupAList, groupBList,
						groupAListExtra, groupBListExtra, groupList, true);
			}
		}
		
		return episode;
	}
	
	private ArrayList<ArrayList<Pair> > parseTruths(
			Node node,
			List<String> groupAList,
			List<String> groupBList,
			List<String> groupAListExtra,
			List<String> groupBListExtra,
			List<String> groupList) {
		ArrayList<ArrayList<Pair> > ret = new ArrayList<ArrayList<Pair> >(2);
		ret.add(new ArrayList<Pair>()); // correct
		ret.add(new ArrayList<Pair>()); // wrong
		ret.add(new ArrayList<Pair>()); // correct extra
		ret.add(new ArrayList<Pair>()); // wrong extra
		
		NodeList truths = node.getChildNodes();
		for (int i = 0; i < truths.getLength(); i++) {
			Node truth = truths.item(i);
			if (truth.getNodeName() == "Truth") {
				String pA = truth.getAttributes().getNamedItem("A").getNodeValue();
				String pB = truth.getAttributes().getNamedItem("B").getNodeValue();
				boolean correct = truth.getAttributes().getNamedItem("Match").getNodeValue() == "true" ? true : false;
				if (groupAList.contains(pA) && groupBList.contains(pB)) {
					Pair p = new Pair(groupAList.indexOf(pA), groupBList.indexOf(pB), pA, pB);
					if (correct) {
						ret.get(CORRECT_POSIT).add(p);
					}
					else {
						ret.get(WRONG_POSIT).add(p);
					}
				}
				else if (groupAListExtra.contains(pA) && groupBList.contains(pB)) {
					Pair p = new Pair(groupAListExtra.indexOf(pA), groupBList.indexOf(pB), pA, pB);
					if (correct) {
						ret.get(CORRECT_POSIT_EXTRA).add(p);
					}
					else {
						ret.get(WRONG_POSIT_EXTRA).add(p);
					}
					
				}
				else if (groupAList.contains(pA) && groupBListExtra.contains(pB)) {
					Pair p = new Pair(groupAList.indexOf(pA), groupBListExtra.indexOf(pB), pA, pB);
					if (correct) {
						ret.get(CORRECT_POSIT_EXTRA).add(p);
					}
					else {
						ret.get(WRONG_POSIT_EXTRA).add(p);
					}
				}
				else if (groupList.contains(pA) && groupList.contains(pB)) {
					Pair p = new Pair(groupList.indexOf(pA), groupList.indexOf(pB), pA, pB);
					if (correct) {
						ret.get(CORRECT_POSIT).add(p);
					}
					else {
						ret.get(WRONG_POSIT).add(p);
					}
				}
				else {
					System.out.println(String.format("%s and/or %s not found in the list of players", pA, pB));
				}
			}
		}
		
		return ret;
	}
	
	private ResultPairSet parseCeremony (
			Node node,
			List<String> groupAList,
			List<String> groupBList,
			List<String> groupAListExtra,
			List<String> groupBListExtra,
			List<String> groupList,
			boolean returnExtra) {
		int ncorrect = Integer.parseInt(node.getAttributes().getNamedItem("Correct").getNodeValue().toString());
		// problem.  when there are extra people to match, the number correct isn't exact like in the season 1 case
		if (ncorrect == 0) {
			// in this case we know that this one cannot be correct.  Do nothing
		}
		else if (ncorrect > 0 && (groupAListExtra.size() != 0 || groupBListExtra.size() != 0)) {
			// in this case we don't know how to divide up the number correct between the extras and the main players
		}
		ArrayList<Pair> pairs = new ArrayList<Pair>();
		NodeList pairNodes = node.getChildNodes();
		for (int i = 0; i < pairNodes.getLength(); i++) {
			Node pairNode = pairNodes.item(i);
			if (pairNode.getNodeName() == "Pair") {
				String pA = pairNode.getAttributes().getNamedItem("A").getNodeValue();
				String pB = pairNode.getAttributes().getNamedItem("B").getNodeValue();
				if (!returnExtra && groupAList.contains(pA) && groupBList.contains(pB)) {
					pairs.add(new Pair(groupAList.indexOf(pA), groupBList.indexOf(pB), pA, pB));
				}
				else if (returnExtra && groupAListExtra.contains(pA) && groupBList.contains(pB)) {
					pairs.add(new Pair(groupAListExtra.indexOf(pA), groupBList.indexOf(pB), pA, pB));
				}
				else if (returnExtra && groupAList.contains(pA) && groupBListExtra.contains(pB)) {
					pairs.add(new Pair(groupAList.indexOf(pA), groupBListExtra.indexOf(pB), pA, pB));
				}
				else if (groupList.contains(pA) && groupList.contains(pB)) {
					PairSet temp = new PairSet(pairs);
					Pair thisPair = new Pair(groupList.indexOf(pA), groupList.indexOf(pB), pA, pB);
					if (temp.has(thisPair, false)) {
						continue;
					}
					pairs.add(thisPair);
				}
			}
		}
		return (new ResultPairSet(new PairSet(pairs), ncorrect));
	}
}
