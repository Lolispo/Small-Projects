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

	Lägg till eget ELO system, så man kan ha många olika sätt att välja balanserade lag när man valt active players
		Elo balance
		Inhouse balance = based on skill and prev teams
		Only rank balance = First iteration = balanced(playerList);
		Random balance = done? = randomSeparation(playerList)
*/

public class balancingSystem{

	private int amountTeams;
	private int amountPlayers;
	private int game; // 0 = cs, 1 = dota
	private int amountOfTeamCombs = 5;
	private TeamCombination currentTeamComb;

	private Random r;
	private Kattio io;

	private ArrayList<Player> playerList; // List of all players total #TODO Currently also used as active
	private ArrayList<Player> activePlayers; // Implement me?

	private ArrayList<TeamCombination> teamCombs; // All teamcombinations for active players 
	private ArrayList<TeamCombination> playedTeamCombs; // Used teamcombinations

//			ArrayList<ArrayList<Player>> inhouseBalancedTeams = inhouseBalance(newPlayers);
			 // #TODO VÄLJ MED KNAPPAR
			// Old - print to check it's balance - use simple and final
			/*
			ArrayList<ArrayList<Player>> balancedTeams = balanced(playerList);
			ArrayList<ArrayList<Player>> randomTeams = randomSeparation(playerList);
			*/

	// Gör om så att man inte väljer game by default, man gör det i GUI
	balancingSystem(int teams, int players, int game){ // Remove except
		this.amountTeams = teams; // Amount of teams
		this.amountPlayers = players; // PER TEAM
		this.game = game;
		//this.amountOfTeamCombs = 2; // Testing, default value 5

		r = new Random();
		playerList = new ArrayList<Player>();

		io = new Kattio(System.in);
		StringBuilder sb = new StringBuilder();
		while(io.hasMoreTokens()){
			sb.append(io.getWord());
		}
		readJSONData(sb.toString()); // Init all players to field "playerList"
		teamCombs = new ArrayList<TeamCombination>();
		playedTeamCombs = new ArrayList<TeamCombination>();

		int i = 0;
		do{
			//Should choose activeplayers
			this.activePlayers = activePlayers(this.playerList); // #TODO

			boolean newPlayers;
			if(i == 0){ // # TODO ^activePlayers
				newPlayers = true;
			}else{
				newPlayers = false;
			}
			
			// Get balance
			String[] buttons = { "Inhouse balance", "balanced (old)", "randomSep (old)" };
	    	int rc = 0;
	    	//rc = JOptionPane.showOptionDialog(null, Player.getActivePlayers(this.activePlayers), "Which way to balance?",
	        //JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);	
			ArrayList<ArrayList<Player>> balancedTeams;
			switch(rc){
				case 0:
					System.out.println("inhouseBalanceChosen");
					balancedTeams = inhouseBalance(playerList, newPlayers);
					break;
				case 1:
					System.out.println("balanced(old)chosen");
					balancedTeams = balanced(playerList);
					break;
				case 2:
					System.out.println("random(old)chosen");
					balancedTeams = randomSeparation(playerList);
					break;
				default: 
					balancedTeams = inhouseBalance(playerList, newPlayers);
			}

			// Choose winner
			if(!chooseWinner(balancedTeams)){
				continue;
			}

			// Should print result
			finalPrint(balancedTeams);
			simplePrint(balancedTeams);
			i++;
		}while(i < amountOfTeamCombs);
	
	/*
		System.out.println("---------------------------");
		System.out.println("Played Games: ");
		System.out.println("");
		for(TeamCombination ptc : playedTeamCombs){
			finalPrint(ptc.getTeamCombination());
			simplePrint(ptc.getTeamCombination());
		}
	*/
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
		String[] buttons = { "CS", "Dota", "Generic Game"};
    	int rc = JOptionPane.showOptionDialog(null, "Games available: CsGo and Dota", "Which game?",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);	
		if(rc >= 2){ // Current default doto
			rc = 1;
		}
		new balancingSystem(rc); 
			
	}

	public boolean chooseWinner(ArrayList<ArrayList<Player>> chosenTeams){
        String[] buttons = { "Team 1", "Team 2", "Didn't Play" };
    	int rc = JOptionPane.showOptionDialog(null, currentTeamComb.toStringShort(), "Who won?",
        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, buttons, buttons[2]);	

    	System.out.println("--------------------------");
		System.out.println("Teams BEFORE rating change");
		finalPrint(chosenTeams);
		switch(rc){
			case 0:
				updateRating(true);
				playedTeamCombs.add(currentTeamComb);
				return true;
			case 1: 
				updateRating(false);
				playedTeamCombs.add(currentTeamComb);
	    		return true;
	    	case 2: 
		    	return false;
		    default: 
		    	return false;
        }		     		
	}

	public void updateRating(boolean team1Won){
		int change = currentTeamComb.getMMRChange(team1Won);
		ArrayList<Player> winningTeam;
		ArrayList<Player> losingTeam;
		if(team1Won){
			winningTeam = currentTeamComb.getTeamCombination().get(0);
			losingTeam = currentTeamComb.getTeamCombination().get(1);
		}else{
			winningTeam = currentTeamComb.getTeamCombination().get(1);
			losingTeam = currentTeamComb.getTeamCombination().get(0);
		}
		for(Player p : winningTeam){
			p.setMmr(game, p.getMmr(game) + change);
		}
		for(Player p : losingTeam){
			p.setMmr(game, p.getMmr(game) - change);
		}
	}

	public ArrayList<ArrayList<Player>> inhouseBalance(ArrayList<Player> players, boolean newPlayers){
		if(newPlayers){ // Handles first time and when different players are playing
			// Should make combinations
			generateTeamCombs(players); // change to active Players
			// Should give values for all combinations
			for(TeamCombination tempTeamComb : teamCombs){
				tempTeamComb.calcSkill(this.game);
			}
		}
		for(TeamCombination tempTeamComb : teamCombs){
			tempTeamComb.calcMMR(this.game);
			tempTeamComb.calcTeamSimilar(this.playedTeamCombs, this.game);
			tempTeamComb.calcScore();
		}
		// Should choose the best chosen
		int bestSuitedTeamComb = Integer.MAX_VALUE;
		ArrayList<TeamCombination> chosenTeamCombs = new ArrayList<TeamCombination>();
		for(TeamCombination tempTeamComb : teamCombs){
			int tempScore = tempTeamComb.getScore();
			if(tempScore < bestSuitedTeamComb){
				bestSuitedTeamComb = tempScore;
				chosenTeamCombs = new ArrayList<TeamCombination>();
				chosenTeamCombs.add(tempTeamComb);
			}else if(tempScore == bestSuitedTeamComb && chosenTeamCombs.isEmpty()){ // If equal, favorize fairer in skill over previous games - good / bad, need testing
				if(tempTeamComb.getSkillDiff() < chosenTeamCombs.get(0).getSkillDiff()){
					chosenTeamCombs = new ArrayList<TeamCombination>();
					chosenTeamCombs.add(tempTeamComb);
				}else{
					// Add for randomed result when same
					chosenTeamCombs.add(tempTeamComb);
				}
			}
		}

		TeamCombination chosenTeamComb;
		if(chosenTeamCombs.size() == 1){
			chosenTeamComb = chosenTeamCombs.get(0);
		}else{
			// Random from equal seeds
			System.out.println(chosenTeamCombs.size() + " teams were equal, randoming ...");
			Random r = new Random();
			chosenTeamComb = chosenTeamCombs.get(r.nextInt(chosenTeamCombs.size()));
		}
		chosenTeamComb.calcMMRChange(this.game);

		System.out.println("---------------------------------\n");
		System.out.println("Chosen team: ");
		System.out.println(chosenTeamComb.toString(this.game));

		// We have the chosen combination - add to correct spots
		this.currentTeamComb = chosenTeamComb;
		return chosenTeamComb.getTeamCombination();
	}

	public void generateTeamCombs(ArrayList<Player> playersPlaying){
		if(playersPlaying.size() != amountTeams * amountPlayers){
			System.out.println("@generateTeamCombs, Somethings wrong, wrong teamsizes");
		}

		// Amount of combinations = om 2 lag: playersPlaying! / amountPlayers


		// Straight forward method in getting all permutations of 10 players in 5 teams, with duplicates
		for (int i = 0; i < playersPlaying.size(); i++) {
		    for (int j = i + 1; j < playersPlaying.size(); j++) {
		    	for (int k = j+1; k < playersPlaying.size(); k++) {
		    		for (int l = k+1; l < playersPlaying.size(); l++) {
		   				for (int m = l+1; m < playersPlaying.size(); m++) {
							int[] playersChosenNum = {i, j, k, l, m};
							teamCombs.add(new TeamCombination(getBothTeams(playersPlaying, playersChosenNum)));
		   				}
		   			}
		   		}
		    }
		}
	}

	public ArrayList<ArrayList<Player>> getBothTeams(ArrayList<Player> playersPlaying, int[] playersChosenNum){
		//System.out.println("-------- Making Team Comb ---------");
		ArrayList<Player> playersChosenTeam = new ArrayList<Player>();
		for(int i = 0; i < playersChosenNum.length; i++){ // Assumes playersPlaying = 10 #Check
			playersChosenTeam.add(playersPlaying.get(playersChosenNum[i]));
			//System.out.println("Team 1: " + playersChosenNum[i]);
		}
		ArrayList<Player> playersOtherTeam = new ArrayList<Player>();
		for(int i = 0; i < playersPlaying.size(); i++){ // Assumes playersPlaying = 10 #Check
			boolean secondTeam = true;
			for(int j = 0; j < playersChosenNum.length; j++){
				if(i == playersChosenNum[j]){
					secondTeam = false;
				}
			}
			if(secondTeam){
				playersOtherTeam.add(playersPlaying.get(i));
				//System.out.println("Team 2: " + i);
			}
		}
		ArrayList<ArrayList<Player>> bothTeams = new ArrayList<ArrayList<Player>>();
		bothTeams.add(playersChosenTeam);
		bothTeams.add(playersOtherTeam);
		//System.out.println("--------------------------");
		return bothTeams;
	}

	// #TODO activePlayers
	public ArrayList<Player> activePlayers(ArrayList<Player> allPlayersList){
		
		/*
		JFrame frame = new JFrame();
		// Gör components 
		JContainer container1 = new JContainer();
		JRadioButton button1 = new JRadioButton();
		button1.setEventListener(new action(){
			void actionPerformed(){
				
			}
		});
		JTextField text1 = new JTextField("");
		container1.add(button1);
		frame.add();
*/
		return allPlayersList;
	}

	public void readJSONData(String json){ // Static or not?
		// Unpack using JSON package
		// Make Players relevant 
		JsonArray players = Json.parse(json).asArray();

		// Choosing players
		// UPDATE THIS WITH NEW JSON INFO  https://docs.google.com/spreadsheets/d/1NNavWEzEf9gx3hBzh0vpQ_5pn7kjievQyiXA3PRhU34/edit#gid=31358158
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
			case "Legendary Eagle Master":
				value = 4;
				break;
			case "Legendary Eagle":
			case "DMG":
				value = 3;
				break;
			case "Master Guardian Elite":
			case "Master Guardian 2":
			case "Master Guardian 1":
			case "Gold Nova 4":
			case "Gold Nova 3":
			case "Gold Nova 2":
			case "Gold Nova 1":
				value = 2;
				break;
			case "Silver Elite Master":
			case "Silver Elite":
			case "Silver 4":
			case "Silver 3":
			case "Silver 2":
			case "Silver 1":
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


	public String stringTeam(ArrayList<Player> list){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for(Player p : list){
			sb.append(p.getUserName() + "("+p.getGameSkill(game)+"),");
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
				sb.append(p.getUserName() + "("+p.getGameSkill(game)+"),");	
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
		System.out.println(sb.toString());
	}

	public void finalPrint(ArrayList<ArrayList<Player>> list){
		int team = 0;
		int[] teamTotal = new int[list.size()];
		int[] mmrTotal = new int[list.size()];
		for(ArrayList<Player> tempList : list){
			int i = 0;
			for(Player p : tempList){
				String s = p.getUserName();
				StringBuilder sb = new StringBuilder();
				sb.append(s);
				for(int j = 0; j < 15-s.length(); j++) // Space adding foor loop it seems
					sb.append(" ");
				System.out.println("Team "+(team+1)+": Player " +(i+1)+ ": "+ 
					sb.toString() + "Skill-level: " +p.getGameSkill(game) + " MMR: "+ p.getMmr(this.game));
				teamTotal[team] += p.getGameSkill(game);
				mmrTotal[team] += p.getMmr(this.game);
				i++;
			}
			team++;
		}
		int amountOfTeams = team;
		for(int i = 0; i < amountOfTeams; i++){
			System.out.println("Team "+(i+1) +" skill score total:  " + teamTotal[i] + ". Avg MMR: "+  (mmrTotal[i] / amountPlayers));	
		}
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
			switch(players.get(i).getGameSkill(game)){
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
			//System.out.println("Player "+players.get(i).getUserName()+", skill = "+players.get(i).getGameSkill(game));
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

	/*
		Seperates the incoming group of players in two teams on random, with no seeding
	*/
	public ArrayList<ArrayList<Player>> randomSeparation(ArrayList<Player> players){
		
		Collections.shuffle(players);
		ArrayList<ArrayList<Player>> list = new ArrayList<ArrayList<Player>>();
		for(int i = 0; i < amountTeams; i++){
			list.add(new ArrayList<Player>());
		}
		for(int i = 0; i < players.size(); i++){
			list.get(i % amountTeams).add(players.get(i));
		}
		//TeamCombination tempTeamComb = new TeamCombination(getBothTeams(playersPlaying, playersChosenNum)));
		//this.currentTeamComb = tempTeamComb;
		return list;
	}
}