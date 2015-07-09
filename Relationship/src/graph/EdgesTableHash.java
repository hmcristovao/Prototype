// It simply contains a general list of edges names.
// It does not contain information of edges.

package graph;

import java.util.HashMap;
import java.util.Map;

public class EdgesTableHash {
	private Map<String,EdgeData> table; 
	
	public EdgesTableHash() {
		this.table = new HashMap<String, EdgeData>();
	}	
	public EdgeData get(String idEdge) {
		if(this.table.containsKey(idEdge))
			return this.table.get(idEdge);
		return null;
	}
	public void put(String idEdge, EdgeData edgeData) {
		this.table.put(idEdge, edgeData);
	}
	public boolean containsKey(String idEdge) {
		return this.table.containsKey(idEdge);
	}
	
	public String toString() {
		return  this.table.toString();
	}
}


