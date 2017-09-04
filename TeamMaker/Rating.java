public class Rating{

	private int[] mmr;
	private int playedGames;

	public Rating(int cMMR, int dMMR, int playedGames){
		mmr = new int[2]; // Antal spel
		mmr[0] = cMMR;
		mmr[1] = dMMR;
		this.playedGames = playedGames;
	}

	public Rating(String rank, String highest, String cbelong, String solo, String party, String dbelong){
		mmr = new int[2]; // Antal spel
		int csRank = csgoRank(rank, highest, cbelong);
  	 	int dotaRank = dotaMMR(solo, party, dbelong);
  	 	this.playedGames = 0;
  	 	setInitRank(csRank, dotaRank);
	}

	public int getPG(){
		return this.playedGames;
	}

	public int[] getMMR(){
		return this.mmr;
	}

	public void setInitRank(int csGo, int dota){
		int kValue = 28; // around 500 / 18, där 18 = 6 * 3
		int baseMMR = 700;
		mmr[0] = kValue * csGo + baseMMR;
		mmr[1] = kValue * dota + baseMMR;
		System.out.println("Ranks: " + mmr[0] + " , " + mmr[1]);
	}

	public int csgoRank(String rank, String highest, String belong){
		int value = 0;
		int rankValue = csGoValue(rank);
		int highestValue = csGoValue(highest);
		if(highestValue == -1){
			highestValue = rankValue;
		}
		int belongValue = csGoValue(belong);
		if(belongValue == -1){
			belongValue = rankValue;
		}
		value += rankValue;
		value += highestValue;
		value += belongValue;
		return value;
	}

	public int dotaMMR(String solo, String party, String belong){
		int value = 0;
		int rankValue = dotaRank(solo);
		int highestValue = dotaRank(party);
		if(highestValue == -1){
			highestValue = rankValue;
		}
		int belongValue = dotaRank(belong);
		if(belongValue == -1){
			belongValue = rankValue;
		}
		value += rankValue;
		value += highestValue;
		value += belongValue;
		return value;
	}

	public int csGoValue(String rank){
		int value = 0;
		switch(rank){
			case "Global":
				value = 6;
				break;
			case "Supreme":
				value = 5;
				break;
			case "LegendaryEagleMaster":
				value = 4;
				break;
			case "LegendaryEagle":
			case "DMG":
				value = 3;
				break;
			case "MasterGuardianElite":
			case "MasterGuardian2":
			case "MasterGuardian1":
			case "GoldNova 4":
			case "GoldNova 3":
			case "GoldNova 2":
			case "GoldNova 1":
				value = 2;
				break;
			case "SilverEliteMaster":
			case "SilverElite":
			case "Silver4":
			case "Silver3":
			case "Silver2":
			case "Silver1":
			case "Unranked":
				value = 1;
				break;
			default:
				value = -1;
				break;
		}
		System.out.println(rank + " = "+value);
		return value;
	}

	public int dotaRank(String string){
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
			case "0-2500/Unranked":
				value = 1;
				break;
			default:
				value = -1;
				break;
		}
		return value;
	}
}