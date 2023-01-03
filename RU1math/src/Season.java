import java.util.ArrayList;

public class Season {
	public int num = 0;
	public ArrayList<Episode> episodes;
	public ArrayList<String> groupAList;
	public ArrayList<String> groupBList;
	public ArrayList<String> groupAExtraList;
	public ArrayList<String> groupBExtraList;
	public ArrayList<String> groupList;
	
	public Season() {
		episodes = new ArrayList<Episode>();
		groupAList = new ArrayList<String>();
		groupBList = new ArrayList<String>();
		groupAExtraList = new ArrayList<String>();
		groupBExtraList = new ArrayList<String>();
		groupList = new ArrayList<String>();
	}
}
