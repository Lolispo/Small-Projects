import java.util.*;

public class Player{
	private String name;
	private String userName;
	private int csGo;
	private int dota;
	private String[] languages; // Choose correct datastructure
	private String languageString;

	public Player(String name, String userName, int csGo, int dota, String languageString){
		this.name = name;
		this.userName = userName;
		this.csGo = csGo;
		this.dota = dota;
		this.languageString = languageString;
		languages = languageString.split(", ");		
	}

	public Player(String userName, int csGo, int dota, String languageString){
		this("", userName, csGo, dota, languageString);
	}

	public int getCS(){
		return csGo;
	}

	public int getDota(){
		return dota;
	}

	public int getGame(int game){
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