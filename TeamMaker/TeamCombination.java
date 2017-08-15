import java.util.*;

public class TeamCombination{

	private ArrayList<ArrayList<Player>> teams;

	private int rangePrevTeams;
	private int amountPlayed;
	private int skillDiff;
	private int comparableScore;

	public int skillImpactFactor = 10;

	public ArrayList<ArrayList<Player>> getTeamCombination(){
		return this.teams;
	}

	public int getRange(){
		return this.rangePrevTeams;
	}

	public int getSkillDiff(){
		return this.skillDiff;
	}

	public int getScore(){
		return this.comparableScore;
	}

	public TeamCombination(ArrayList<ArrayList<Player>> paramTeams){
		this.teams = paramTeams;
		this.rangePrevTeams = 0;
		this.amountPlayed = 0;
	}

	// Calculates Skillcomparison between teams and comparison how previous teams looked
	// #TODO separera så inte calculatePrev ligger här, eftersom den inte ska räknas ut på första körningen iaf
		// Måste flytta var comprabable score beräknas, just nu i calcPRev
	// @param prevTeamCombs: Previous played games' teamcombinations
	// 	game: index for game
	public void compareBoth(ArrayList<TeamCombination> prevTeamCombs, int game){
		//System.out.println("@TeamCombination @compareBoth");
		ArrayList<ArrayList<Player>> currentTeams = this.teams;
		this.skillDiff = compareSkill(currentTeams, game);
		calculatePrev(prevTeamCombs, game);
	}

	public void calculatePrev(ArrayList<TeamCombination> prevTeamCombs, int game){
		ArrayList<ArrayList<Player>> currentTeams = this.teams;
		this.amountPlayed = prevTeamCombs.size(); // Amount of games played currently
		if(prevTeamCombs.isEmpty()){
			this.rangePrevTeams = 0;
		}else{
			this.rangePrevTeams = compareToPrevCombs(prevTeamCombs, currentTeams); // Sets the value of how close this team iteration in comparison to previous ones
		}
		this.comparableScore = this.rangePrevTeams + this.skillDiff * this.skillImpactFactor; // 
		//printTeamComb(game);
	}

	// Compares the total skill level between both teams and sets skilldiff field to it
	public int compareSkill(ArrayList<ArrayList<Player>> currentTeams, int game){
		int team1Skill = sumTeamSkill(currentTeams.get(0), game);
		int team2Skill = sumTeamSkill(currentTeams.get(1), game);
		return Math.abs(team1Skill-team2Skill);

	}

	// Calcs total skill for game chosen and returns the total
	public int sumTeamSkill(ArrayList<Player> team, int game){
		int sum = 0;
		for(Player p : team){
			sum += p.getGameSkill(game);
		}
		return sum;
	}

	// Starts comparison for multiple previous games
	public int compareToPrevCombs(ArrayList<TeamCombination> teamComb, ArrayList<ArrayList<Player>> teams){
		int sum = 0;
		for(int i = 0; i < teamComb.size(); i++){
			sum += compareTeamCombination(teamComb.get(i), teams);			
		}
		return sum;
	}

	// Compares how similar teams (one teamCombination) is to another teamcombination
	public int compareTeamCombination(TeamCombination teamComb, ArrayList<ArrayList<Player>> teams){ 
		int sum = 0;
		for(ArrayList<Player> team : teams){ // teams = 2 teams
			sum += calcRange(teamComb.getTeamCombination(), team);
		}
		return sum;
	}

	//Compares one team to a teamcomb and returns a number comparison
	// Low = good? (big difference)
	public int calcRange(ArrayList<ArrayList<Player>> teamCombTeams, ArrayList<Player> team){
		int count = 0;
		for(int i = 0; i < team.size(); i++){
			Player currentPlayer = team.get(i);
			Player[] teammates = new Player[team.size()-1];
			int index = 0;
			for(int j = 0; j < team.size(); j++){
				if(j != i){
					teammates[index] = team.get(j);
					index++;
				}
			}
			// Should have currentPlayer and his teammates
			// Find where this player played in the selected TeamComb
			for(ArrayList<Player> selectedTeam : teamCombTeams){
				if(selectedTeam.contains(currentPlayer)){
					for(Player p : selectedTeam){
						if(!currentPlayer.comparePlayer(p)){
							// Only his previous teammates
							if(team.contains(p)){
								count++;
							}
						}
					}
				}
			}
		}
		return count;
	}

	public void printTeamComb(int game){
		System.out.println(toString(game));
	}

	public String toStringShort(){
		ArrayList<ArrayList<Player>> currentTeams = this.teams;
		StringBuilder sb = new StringBuilder();
		StringBuilder teamSB; 
		int i = 1;
		for(ArrayList<Player> team : currentTeams){
			teamSB = new StringBuilder();
			for(Player p : team){
				teamSB.append(p.getUserName() + " ");
			}
			sb.append("Team " +i +": " + teamSB.toString() + "\n");
			i++;
		}
		return sb.toString();
	}

	public String toString(int game){
		ArrayList<ArrayList<Player>> currentTeams = this.teams;
		StringBuilder sb = new StringBuilder();
		sb.append("--------- Team Comb --------\n");
		StringBuilder teamSB; 
		int mmrTotal;
		int i = 1;
		for(ArrayList<Player> team : currentTeams){
			teamSB = new StringBuilder();
			mmrTotal = 0;
			for(Player p : team){
				teamSB.append(p.getUserName() + " ");
				mmrTotal += p.getMmr();
			}
			sb.append("Team " +i +": " + teamSB.toString() + ". Avg MMR = " + (mmrTotal / 5)+ "\n");
			i++;
		}
		sb.append("Skill diff: " + this.skillDiff + "\n");
		sb.append("Compared to prev teamCombs (First game = 0) : " + this.rangePrevTeams + "\n");
		sb.append("SCORE: " + this.comparableScore + " (Skill factor: " + this.skillImpactFactor+")\n");
		sb.append("----------------------------\n");
		return sb.toString();
	}
}