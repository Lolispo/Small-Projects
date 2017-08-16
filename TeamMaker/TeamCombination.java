import java.util.*;

public class TeamCombination{

	private ArrayList<ArrayList<Player>> teams;

	private int rangePrevTeams;
	private int amountPlayed;
	private int skillDiff;
	private int mmrDiff;
	private int mmrChange1; // team1 stakes: +mmrChange1, -mmrChange2
	private int mmrChange2;
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
	// #TODO separera så inte calcTeamSimilar ligger här, eftersom den inte ska räknas ut på första körningen iaf
		// Måste flytta var comprabable score beräknas, just nu i calcPRev
	
	// @param	game: index for game
	public void calcSkill(int game){
		this.skillDiff = compareSkill(this.teams, game);
	}

	public void calcMMR(int game){
		this.mmrDiff = compareMMR(this.teams, game);
	}

	// @param prevTeamCombs: Previous played games' teamcombinations
	public void calcTeamSimilar(ArrayList<TeamCombination> prevTeamCombs, int game){
		ArrayList<ArrayList<Player>> currentTeams = this.teams;
		this.amountPlayed = prevTeamCombs.size(); // Amount of games played currently
		if(prevTeamCombs.isEmpty()){
			this.rangePrevTeams = 0;
		}else{
			this.rangePrevTeams = compareToPrevCombs(prevTeamCombs, currentTeams); // Sets the value of how close this team iteration in comparison to previous ones
		}
	}

	// Calcs the score for comparison . #Todo MMR instead of skill
	public void calcScore(){
		this.comparableScore = this.rangePrevTeams + this.mmrDiff;//this.skillDiff * this.skillImpactFactor;
		//printTeamComb(game);
	}

	public void calcMMRChange(int game){
		
		// Calc total mmr for both teams
		int team1Total = 0;
		ArrayList<Player> team1 = getTeamCombination().get(0);
		ArrayList<Player> team2 = getTeamCombination().get(1);

		for(Player p : team1){
			team1Total += p.getMmr(game);
		}
		int team2Total = 0;
		for(Player p : team2){
			team2Total += p.getMmr(game);
		}

		// Calc average
		int team1AvgMMR = team1Total / team1.size();
		int team2AvgMMR = team2Total / team2.size();
		
		// Compare avg between teams
		// Get some connection between how much you win per game with how fair the game was
		int ratingChange = 25;
		int foo = 200;
		// difference
		int diff1 = 100 - team1AvgMMR + team2AvgMMR; // Team 1 wins
		int diff2 = 100 - team2AvgMMR + team1AvgMMR; // Team 2 wins
		// diff > 100 -> underdog, diff < 100 -> expected
		// Fixa max 50 min 5
		int change1 = (int)Math.floor(ratingChange * diff1 / 100);
		int change2 = (int)Math.floor(ratingChange * diff2 / 100);
		if(change1 < 5){
			change1 = 5;
		}else if(change1 > 50){
			change1 = 50;
		}
		if(change2 < 5){
			change2 = 5;
		}else if(change2 > 50){
			change2 = 50;
		}
		//System.out.println("@teamComb. calcMmr Rating to Change : " + change + ", diff = " + diff);
		this.mmrChange1 = change1;
		this.mmrChange2 = change2;
	}

	public int getMMRChange(boolean team1Won){
		if(team1Won){
			return this.mmrChange1;
		}
		return this.mmrChange2;
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

	// Compares the total mmr between both teams and sets skilldiff field to it
	public int compareMMR(ArrayList<ArrayList<Player>> currentTeams, int game){
		int team1Skill = sumTeamMMR(currentTeams.get(0), game) / 5;
		int team2Skill = sumTeamMMR(currentTeams.get(1), game) / 5;
		return Math.abs(team1Skill-team2Skill);

	}

	// Calcs total mmr for game chosen and returns the total
	public int sumTeamMMR(ArrayList<Player> team, int game){
		int sum = 0;
		for(Player p : team){
			sum += p.getMmr(game);
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
			mmrTotal = 0;
			teamSB = new StringBuilder();
			for(Player p : team){
				teamSB.append(p.getUserName() + " ");
				mmrTotal += p.getMmr(game);
			}
			sb.append("Team " +i +": " + teamSB.toString() + "\n\t Avg MMR = " + (mmrTotal / 5)+ "\n");
			i++;
		}
		//sb.append("Skill diff: " + this.skillDiff + "\n");
		//sb.append("TeamPlayerComparison: " + this.rangePrevTeams + "\n");
		sb.append("MMR diff: " + this.mmrDiff + "\n");
		sb.append("MMR at stake: \n\tTeam 1: +" + this.mmrChange1 + "/-"+ this.mmrChange2 + " \n\tTeam 2: +" + this.mmrChange2 + "/-"+ this.mmrChange1 +"\n");
		sb.append("TeamMatchupSCORE: " + this.comparableScore + " (Skill diff: " + this.skillDiff +", TeamPlayerComparison: " + this.rangePrevTeams +")\n"); //"Skill factor: " + this.skillImpactFactor+")\n");
		sb.append("----------------------------\n");
		return sb.toString();
	}
}