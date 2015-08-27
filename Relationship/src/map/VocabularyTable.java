package map;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import parse.Token;

public class VocabularyTable {
	Map<String,String> table;
	
	public VocabularyTable() {
		this.table = new HashMap<String,String>();
	}
	public void put(Token rdfToken, Token mapToken) {
		this.put(rdfToken.image.trim(), mapToken.image.trim());
	}
	public void put(String rdfString, String mapLinkString) {
		this.table.put(rdfString, mapLinkString);
	}
	public String get(String rdfString) {
		return this.table.get(rdfString);
	}
	public int size() {
		return this.table.size();
	}
	public String toString() {
		StringBuffer out = new StringBuffer();
		Iterator<String> i = this.table.keySet().iterator(); 
		while(i.hasNext()) {
		   String rdfString     = (String)i.next(); 
		   String mapLinkString = this.table.get(rdfString);
		   out.append("   ");
		   out.append(rdfString);
		   out.append(" -> ");
		   out.append(mapLinkString);
		   out.append("\n");
		}
		return out.toString();
	}
}
