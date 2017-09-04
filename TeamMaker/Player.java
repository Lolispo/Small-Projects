import java.util.*;

public class Player{
	private String name;
	private String userName;
	private int csGo; // 0 cs, 1 dota
	private int dota;
	private int playedGames;
	private String[] languages; // Choose correct datastructure
	private String languageString;
	private int[] mmr;

	public Player(String name, String userName, int[] mmr, int playedGames, String languageString){
		this.name = name;
		this.userName = userName;
		this.mmr = mmr;
		this.csGo = mmr[0];
		this.dota = mmr[1];
		this.languageString = languageString;
		this.playedGames = playedGames;
		languages = languageString.split(", ");		
		mmr = new int[2]; // Amount of games with mmr
	}

	public void setPG(int pg){
		this.playedGames = pg;
	}
	
	public int getPG(){
		return this.playedGames;
	}
	
	public void setMmr(int game, int mmr){
		this.mmr[game] = mmr;
	}

	public int getMmr(int game){
		return this.mmr[game];
	}

	public boolean comparePlayer(Player p){
		if(p.getName().equals(this.name) && p.getUserName().equals(this.userName)){
			return true;
		}
		return false;
	}

	public int getCS(){
		return csGo;
	}

	public int getDota(){
		return dota;
	}

	public int getGameSkill(int game){
		switch(game){
			case 1: 
				return getDota();
			case 0:
			default:
				return getCS();
		}
	}
	
	public String getUserName(){
		return userName;
	}

	public String getName(){
		return name;
	}

	public String[] getLanguages(){
		return languages;
	}

	public String toString(){
		return (userName + " (" + name+ ") : CS:GO = "+csGo +", dota = "+dota + ". Lang = " +languageString);
	}

	public static String getActivePlayers(ArrayList<Player> activePlayers){
		StringBuilder sb = new StringBuilder();
		sb.append("Players plaing: ");
		for(Player p : activePlayers){
			sb.append(p.getUserName()+ " ");
		}
		return sb.toString();
	}

}