import java.util.*;
import JSON.*;
import javax.swing.*;

// Author Petter Andersson
// TODO are added in drive document atm for project
// https://docs.google.com/document/d/1p4oNbBUb0dUV3-gGakf0nSoB1pZ1ikyoZjveuWObLLg/edit

/*
	Göra alla kombinationer av lag, 10 över 5
		Varje lag kombination sparar en skilllevel siffra mellan sig och siffra jämförelse med tidigare games
	beräkna lägsta siffran av skill level jämförelse
	efter ett game spelat - beräkna jämförelse med lagen, få ut nya lägsta


*/

public class balancingSystem{

	private int amountTeams;
	private int amountPlayers;
	private int game; // 0 = cs, 1 = dota

	private Random r;
	private Kattio io;

	private ArrayList<Player> playerList;

	private ArrayList<TeamCombination> teamCombs;

	// Gör om så att man inte väljer game by default, man gör det i GUI
	balancingSystem(int teams, int players, int game){
		this.amountTeams = teams; // Amount of teams
		this.amountPlayers = players; // PER TEAM
		this.game = game;

		r = new Random();
		playerList = new ArrayList<Player>();

		io = new Kattio(System.in);
		StringBuilder sb = new StringBuilder();
		while(io.hasMoreTokens()){
			sb.append(io.getWord());
		}
		readJSONData(sb.toString());
		activePlayers(playerList);
		//ArrayList<ArrayList<Player>> randomTeams = randomSeparation(playerList);
		//print(randomTeams);
		teamCombs = new ArrayList<TeamCombination>();
		ArrayList<ArrayList<Player>> balancedTeams = balanced(playerList);
		finalPrint(balancedTeams);
		simplePrint(balancedTeams);
		io.close();
	}

	balancingSystem(int game){
		this(2,5, game);
	}

	balancingSystem(){ // Consider adding more constructors later
		this(0);
	}

	public static void main(String[] args){ // Skicka in Data.json
		System.out.println("Starting TeamMaker");
		new balancingSystem(5,2,1);
			
	}

	public void generateTeamCombs(ArrayList<Player> playersPlaying){
		if(playersPlaying.size() != amountTeams * amountPlayers){
			System.out.println("@generateTeamCombs, Somethings wrong, wrong teamsizes");
		}

		// Amount of combinations = om 2 lag: playersPlaying! / amountPlayers
	}

	public void activePlayers(ArrayList<Player> allPlayersList){
		/*
		JFrame frame = new JFrame();
		// Gör components 
		JContext context1 = new JContext();
		JRadioButton button1 = new JRadioButton();
		button1.setEventListener(new action(){
			void actionPerformed(){
				
			}
		});
		JTextField text1 = new JTextField("");
		context.add(button1);
		frame.add();
*/
	}

	public void readJSONData(String json){ // Static or not?
		// Unpack using JSON package
		// Make Players relevant 
		JsonArray players = Json.parse(json).asArray();

		// Choosing players

		for(JsonValue item : players){ 			
			String name = item.asObject().getString("name", "Unknown Name");
  			String userName = item.asObject().getString("username", "Unknown Item");
  			String languageString = item.asObject().getString("languages", "Unknown Item");
  			String csgoRank = item.asObject().getString("csgoRank", "Unknown Item");
  			String dota2Mmr = item.asObject().getString("dota2Mmr", "Unknown Item");
  			int csRank = csgoRank(csgoRank);
  			int dotaRank = dotaMMR(dota2Mmr);
  			int csGo = item.asObject().getInt("csgo", 0);
  			int dota = item.asObject().getInt("dota2", 0);
  			Player p = new Player(name, userName,csRank,dotaRank,languageString); // csGo or csRank
  			playerList.add(p);
  			System.out.println(p);
  			//String name, String userName, int csGo, int dota, String languageString
			//System.out.println(item);
		}
	}

	public int csgoRank(String string){
		int value = 0;
		switch(string){
			case "Global":
				value = 6;
				break;
			case "Supreme":
				value = 5;
				break;
			case "LEM":
				value = 4;
				break;
			case "DMG/LE":
				value = 3;
				break;
			case "GoldNova/AK":
				value = 2;
				break;
			case "Silver/Unranked":
			default:
				value = 1;
				break;
		}
		System.out.println(string + " = "+value);
		return value;
	}

	public int dotaMMR(String string){
		int value = 0;
		switch(string){
			case "6500+":
				value = 6;
				break;
			case "5500-6500":
				value = 5;
				break;
			case "4500-5500":
				value = 4;
				break;
			case "3500-4500":
				value = 3;
				break;
			case "2500-3500":
				value = 2;
				break;
			case "0-2500 / Unranked":
			default:
				value = 1;
				break;
		}
		return value;
	}

	public ArrayList<ArrayList<Player>> balanced(ArrayList<Player> players){

		// Separate based on player skills
		ArrayList<Player> skill1 = new ArrayList<Player>();
		ArrayList<Player> skill2 = new ArrayList<Player>();
		ArrayList<Player> skill3 = new ArrayList<Player>();
		ArrayList<Player> skill4 = new ArrayList<Player>();
		ArrayList<Player> skill5 = new ArrayList<Player>();
		ArrayList<Player> skill6 = new ArrayList<Player>();
		ArrayList<Player> fillers = new ArrayList<Player>();
		
		for(int i = 0; i < players.size(); i++){
			switch(players.get(i).getGame(game)){
				case 1:
					skill1.add(players.get(i));
					break;
				case 2:
					skill2.add(players.get(i));
					break;
				case 3:
					skill3.add(players.get(i));
					break;
				case 4:
					skill4.add(players.get(i));
					break;
				case 5:
					skill5.add(players.get(i));
					break;
				case 6:
					skill6.add(players.get(i));
					break;
				case 0:
				default: 
					fillers.add(players.get(i));
			}
			//System.out.println("Player "+players.get(i).getUserName()+", skill = "+players.get(i).getGame(game));
		}
		// Make randomSeparation within these tiers
		//ArrayList<ArrayList<Player>> s1, s2, s3, fi;
		ArrayList<ArrayList<Player>> s6 = randomSeparation(skill6);
		ArrayList<ArrayList<Player>> s5 = randomSeparation(skill5);
		ArrayList<ArrayList<Player>> s4 = randomSeparation(skill4);
		ArrayList<ArrayList<Player>> s3 = randomSeparation(skill3);
		ArrayList<ArrayList<Player>> s2 = randomSeparation(skill2);
		ArrayList<ArrayList<Player>> s1 = randomSeparation(skill1);
		ArrayList<ArrayList<Player>> fi = randomSeparation(fillers);

		System.out.println("s6 = " + stringTeams(s6));
		System.out.println("s5 = " + stringTeams(s5));
		System.out.println("s4 = " + stringTeams(s4));				
		System.out.println("s3 = " + stringTeams(s3));
		System.out.println("s2 = " + stringTeams(s2));
		System.out.println("s1 = " + stringTeams(s1));
		System.out.println("fi = " + stringTeams(fi));

		//HashMap<ArrayList<Player>, Integer> list = new HashMap<ArrayList<Player>, Integer>();
		LinkedList<ArrayList<Player>> list = new LinkedList<ArrayList<Player>>();
		int[] nums = new int[players.size()];
		int index = 0;
		for(ArrayList<Player> playerList : s6){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 6;
				index++;
			}
		}
		for(ArrayList<Player> playerList : s5){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 5;
				index++;
			}
		}
		for(ArrayList<Player> playerList : s4){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 4;
				index++;
			}
		}
		for(ArrayList<Player> playerList : s3){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 3;
				index++;
			}
		}
		for(ArrayList<Player> playerList : s2){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 2;
				index++;
			}
		}
		for(ArrayList<Player> playerList : s1){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 1;
				index++;
			}
		}
		for(ArrayList<Player> playerList : fi){
			if(!playerList.isEmpty()){
				list.addLast(playerList);
				nums[index] = 0;
				index++;
			}
		}


		ArrayList<ArrayList<Player>> teams = new ArrayList<ArrayList<Player>>();
		int[] size = new int[amountTeams];
		for(int i = 0; i < amountTeams; i++){
			teams.add(new ArrayList<Player>());
			size[i] = 0;
		}
		//int groupsSize = s3.size() + s2.size() + s1.size() + f1.size();
		int i = 0;
		for(ArrayList<Player> tempList : list){
			// Find lowest skill-weight team with less than amountPlayers members
			int smallest = -1;
			int smallestSize = Integer.MAX_VALUE;
			for(int j = 0; j < amountTeams; j++){
				int tempSize = size[j];
				//System.out.println(stringTeams(teams));
				if(tempSize < smallestSize && teams.get(j).size() < amountPlayers){
					smallest = j;
					smallestSize = tempSize;
				}
			}
			int num = nums[i];
			i++;
			teams.get(smallest).addAll(tempList); // Next group i.e. (m2) (a1, b1)
			size[smallest] += num*tempList.size();  
			//System.out.println(stringTeam(tempList)+ " = " + num);
		}
		return teams;
	}

		/*
		1: x3 y3 z3 v3
		2: m2 n2 
		3: a1 b1 c1 d1

		amountTeams = 2
		amountPlayers = 5
	
		s1: (a1,b1) (c1,d1)
		s2: (m2) (n2)
		s3: (x3,z3) (y3,v3)


		1: x3 y3 z3 v3 w3
		2: m2 n2 
		3: a1 b1 c1

		amountTeams = 2
		amountPlayers = 5
	
		s1: (a1,b1) (c1)
		s2: (m2) (n2)
		s3: (x3,z3,w3) (y3,v3)

		comb:
			(a1,b1)(m2)(x3,z3,w3)		(c1)(n2)(y3,v3)				13(6)		9(4)
			(a1,b1)(m2)(y3,v3)			(c1)(n2)(x3,z3,w3)			10(5)		12(5)
			(a1,b1)(n2)(x3,z3,w3)		(c1)(m2)(y3,v3)				13(6)		9(4)
			(a1,b1)(n2)(y3,v3)			(c1)(m2)(x3,z3,w3)			10(5)		12(5)
			(c1)(m2)(x3,z3,w3)			(a1,b1)(n2)(y3,v3)			12(5)		10(5)
			(c1)(m2)(y3,v3)				(a1,b1)(n2)(x3,z3,w3)		9(4)		13(6)
			(c1)(n2)(x3,z3,w3)			(a1,b1)(m2)(y3,v3)			12(5)		10(5)
			(c1)(n2)(y3,v3)				(a1,b1)(m2)(x3,z3,w3)		9(4)		13(6)
		Cant mix from same tier

		comb (focus on 5 players in every team):
		Add så fort som möjligt

		*/
		

		// Rebuild Teams from tiers
		// 		Handle edge cases such as odd numbers 


	public ArrayList<ArrayList<Player>> randomSeparation(ArrayList<Player> players){
		
		Collections.shuffle(players);
		ArrayList<ArrayList<Player>> list = new ArrayList<ArrayList<Player>>();
		for(int i = 0; i < amountTeams; i++){
			list.add(new ArrayList<Player>());
		}
		for(int i = 0; i < players.size(); i++){
			list.get(i % amountTeams).add(players.get(i));
		}
		return list;
	}

	public String stringTeam(ArrayList<Player> list){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Player p : list){
			sb.append(p.getUserName() + "("+p.getGame(game)+"),");
		}
		sb.append("}");
		return sb.toString();
	}

	public String stringTeams(ArrayList<ArrayList<Player>> list){
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for(ArrayList<Player> innerList : list){
			sb.append("{");
			for(Player p : innerList){
				sb.append(p.getUserName() + "("+p.getGame(game)+"),");	
			}
			sb.append("}");
		}
		sb.append("]");
		return sb.toString();
	}

	public void simplePrint(ArrayList<ArrayList<Player>> list){
		StringBuilder sb = new StringBuilder();
		for(int j = 0; j < amountTeams; j++){
			sb.append("Team "+ (j+1)+": ");
			for(int i = 0; i < amountPlayers; i++){
				Player p = list.get(j).get(i);
				sb.append(p.getUserName());
				if(i < amountPlayers-1){
					sb.append(", ");
				}
			}
			sb.append("\n");
		}
		io.println(sb.toString());
	}

	public void finalPrint(ArrayList<ArrayList<Player>> list){
		int team = 0;
		int[] teamTotal = new int[list.size()];
		for(ArrayList<Player> tempList : list){
			int i = 0;
			for(Player p : tempList){
				String s = p.getUserName();
				StringBuilder sb = new StringBuilder();
				sb.append(s);
				for(int j = 0; j < 15-s.length(); j++)
					sb.append(" ");
				io.println("Team "+(team+1)+": Player " +(i+1)+ ": "+ 
					sb.toString() + "Skill-level: " +p.getGame(game));
				teamTotal[team] += p.getGame(game);
				i++;
			}
			team++;
		}
		int amountOfTeams = team;
		for(int i = 0; i < amountOfTeams; i++){
			io.println("Team "+(i+1) +" had " + teamTotal[i] + " total in skill rating.");	
		}
	}


}