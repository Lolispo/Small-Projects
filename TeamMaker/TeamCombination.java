public class TeamCombination{

	private ArrayList<ArrayList<Player>> teams;

	private int rangePrevTeams;
	private int amountPlayed;

	public ArrayList<ArrayList<Player>> getTeamCombination(){
		return this.teams;
	}

	public int getRange(){
		return this.rangePrevTeams;
	}


	public TeamCombination(ArrayList<ArrayList<Player>> paramTeams){
		this.teams = paramTeams;
		this.rangePrevTeams = 0;
		this.amountPlayed = 0;
	}


	public void compareThis(TeamCombination teamComb){
		compareTeamCombination(teamComb, this.teams);
	}

	// Compares how similar teams (one teamCombination) is to another teamcombination
	public void compareTeamCombination(TeamCombination teamComb, ArrayList<ArrayList<Player>> teams){
		int sum = 0;
		for(ArrayList<Player> team : teams){
			sum += calcRange(teamComb.getTeamCombination(), team);
		}
		rangePrevTeams = sum;
	}

	//Compares one team to a teamcomb and returns a number comparison
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
}