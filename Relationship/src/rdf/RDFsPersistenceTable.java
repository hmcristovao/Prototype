package rdf;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import parse.Token;

public class RDFsPersistenceTable  implements Serializable   {
	Map<String,ListRDF> table;
	
	public RDFsPersistenceTable() {
		this.table = new HashMap<String,ListRDF>();
	}
	public boolean containsKey(String concept) {
		return this.table.containsKey(concept);
	}
	public void put(String concept, ListRDF list) {
		this.table.put(concept, list);
	}
	public ListRDF get(String concept) {
		return this.table.get(concept);
	}
	public int size() {
		return this.table.size();
	}
}
