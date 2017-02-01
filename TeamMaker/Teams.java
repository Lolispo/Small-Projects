import java.util.*;

public class Teams{
	
	private ArrayList<ArrayList<Player>> teams;

	public Teams(ArrayList<ArrayList<Player>> teams){
		this.teams = teams;
	}
}

/*
		int[] used = new used[players.length];
		Player[] team1;
		Player[] team2 = new Player[players.length / 2];
		if(players.length % 2 == 0){
			team1 = new Player[players.length/2];
		}
		else{
			team1 = new Player[(players.length/2)+1];	
		}
		for(int i = 0, j = 0; i < players.length / 2; i++){
			int n = r.nextInt(players.length);		// TODO: Check if players.length or same +1 
			if(used[n] == 0){
				used[n] = 1;
				team1[j] = players[n];
				j++;
			}		
		}
		if((players.length / 2) * 2 != players.length){ // Add for odd numbered cases the last situation
			while(true){
				int n = r.nextInt(players.length);
				if(used[n] == 0){
					used[n] = 1;
					if(team1[team1.length-1] != null){
						System.err.println("Error @randomSeparation. Last odd element");
					}
					team1[team1.length - 1] = players[n];	
					break;
				}
			}
		}
		for(int i = 0; i < players.length; i++){
			if(used[i] == 1){
				team2.add(players[i]);
			}
		}
		// Check correctness
		if(players.length % 2 == 0){ // Even
			if(team1.length * 2 != players.length || team2.length != team1.length){
				System.err.println("Error @randomSeparation. Even team sizes");
			}
		}
		else{ // Odd
			if(team1.length + team2.length != players.length || team1.length != (team2.length +1){
				System.err.println("Error @randomSeparation. Odd team sizes");
			}
		}
*/
