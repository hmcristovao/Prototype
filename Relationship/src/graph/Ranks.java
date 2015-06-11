package graph;

import java.util.Arrays;

public class Ranks {
	private MeasuresRanks measuresRanksTable[];  // 0 position correspond to total network
	private int current;
	private int count;
	
	public Ranks(int quantity) {
		this.measuresRanksTable = new MeasuresRanks[quantity];
		for(int i=0; i < quantity; i++){
			this.measuresRanksTable[i] = new MeasuresRanks(i);
		}
		this.count = quantity;
		this.current = 0;
	}
	
	public int getCount() {
		return this.count;
	}

	public void insert(MeasuresRanks measuresRanks) throws Exception {
		if(this.current == this.count)
			throw new Exception("Quantity of nodes is larger than capacity");
		measuresRanksTable[current] = measuresRanks;
		this.current++;
	}
	
	public MeasuresRanks getMeasuresRankTable(int position) throws Exception {
		if(position >= this.count)
			throw new Exception("Tried to read MeasuresRanks over the table");
		return this.measuresRanksTable[position];
	}
	
	public String toString() {
		StringBuffer str = new StringBuffer();
		for(int i=0; i<this.count; i++) {
			str.append("\n=================================================================");
			str.append("\n=================================================================");
			str.append("\nConnected component: "+i);
			str.append("\n");
			str.append(this.measuresRanksTable[i].toString());
			str.append("\n\n");
		}
		return  str.toString();
	}
}
