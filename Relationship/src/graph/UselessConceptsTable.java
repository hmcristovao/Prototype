package graph;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import parse.Token;

// it mainly has been used in StreamGraph.insertRDF_withinTheStreamGraph

public class UselessConceptsTable {
	Map<String, Integer> table;
	
	public UselessConceptsTable() {
		this.table = new HashMap<String,Integer>();
	}
	public boolean containsKey(String uselessConcept) {
		return this.table.containsKey(uselessConcept);
	}
	public boolean containsKeyAndPlusOne(String uselessConcept) {
		boolean bool = this.table.containsKey(uselessConcept);
		if(bool) {
			int count = this.get(uselessConcept);
		    count++;
		    this.table.put(uselessConcept, new Integer(count));
		    return true;
		}
		return false;
	}
	public void insert(String uselessConcept) {
		this.table.put(uselessConcept, new Integer(0));
	}
	public int get(String uselessConcept) {
		Integer integer = this.table.get(uselessConcept);
		return integer.intValue();
	}
	public int size() {
		return this.table.size();
	}
	public String toString() {
		StringBuffer out = new StringBuffer();
		Iterator<String> i = this.table.keySet().iterator(); 
		while(i.hasNext()) {
		   String uselessConcept = (String)i.next(); 
		   Integer count = this.table.get(uselessConcept);
		   out.append("   ");
		   out.append(uselessConcept);
		   out.append(" (count: ");
		   out.append(count.toString());
		   out.append(")\n");
		}
		return out.toString();
	}
}
