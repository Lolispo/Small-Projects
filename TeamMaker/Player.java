import java.util.*;

public class Player{
	private String name;
	private String userName;
	private int csGo;
	private int dota;
	private String[] languages; // Choose correct datastructure
	private String languageString;
	private int mmr;

	public Player(String name, String userName, int csGo, int dota, String languageString){
		this.name = name;
		this.userName = userName;
		this.csGo = csGo;
		this.dota = dota;
		this.languageString = languageString;
		languages = languageString.split(", ");		
		setInitRank();
	}

	public Player(String userName, int csGo, int dota, String languageString){
		this("", userName, csGo, dota, languageString);
	}

	public void setInitRank(){
		switch(csGo){
			case 1:
				mmr = 700;
				break;
			case 2: 
				mmr = 800;
				break;
			case 3: 
				mmr = 900;
				break;
			case 4: 
				mmr = 1000;
				break;
			case 5: 
				mmr = 1100;
				break;
			case 6: 
				mmr = 1200;
				break;
		}
	}

	public void setMmr(int mmr){
		this.mmr = mmr;
	}

	public int getMmr(){
		return this.mmr;
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

}