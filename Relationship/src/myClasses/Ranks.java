package myClasses;

import java.util.Arrays;

public class Ranks {
	private ConnectedComponentMeasures measures[];  // 0 position correspond to total network
	private int current;
	private int maxQuantity;
	
	public Ranks(int maxQuantity) {
		this.measures = new ConnectedComponentMeasures[maxQuantity];
	}
	
	public void insert(ConnectedComponentMeasures connectedComponentMeasures) throws Exception {
		if(this.current == this.maxQuantity)
			throw new Exception("Quantity of nodes is larger than capacity");
		measures[current] = connectedComponentMeasures;
		this.current++;
	}
	
	public ConnectedComponentMeasures getConnectedComponentMeasures(int position) throws Exception {
		if(position >= this.maxQuantity)
			throw new Exception("Tried to read ConnectedComponentMeasures over the table");
		return this.measures[position];
	}
	
	public String toString() {
		return  Arrays.toString(this.measures);
	}
}
