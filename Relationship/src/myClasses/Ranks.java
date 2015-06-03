package myClasses;

import java.util.Arrays;

public class Ranks {
	private MeasuresRanks measuresRanksTable[];  // 0 position correspond to total network
	private int current;
	private int maxQuantity;
	
	public Ranks(int maxQuantity) {
		this.measuresRanksTable = new MeasuresRanks[maxQuantity];
	}
	
	public void insert(MeasuresRanks measuresRanks) throws Exception {
		if(this.current == this.maxQuantity)
			throw new Exception("Quantity of nodes is larger than capacity");
		measuresRanksTable[current] = measuresRanks;
		this.current++;
	}
	
	public MeasuresRanks getConnectedComponentMeasures(int position) throws Exception {
		if(position >= this.maxQuantity)
			throw new Exception("Tried to read MeasuresRanks over the table");
		return this.measuresRanksTable[position];
	}
	
	public String toString() {
		return  Arrays.toString(this.measuresRanksTable);
	}
}
