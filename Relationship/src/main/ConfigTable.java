package main;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import parse.Token;

public class ConfigTable {
	Map<String,String> table;
	
	public ConfigTable() {
		this.table = new HashMap<String,String>();
	}
	public void insert(String var, String value) {
		this.table.put(var, value);
	}
	public Object get(String var) {
		return this.table.get(var);
	}
	public String getString(String var) {
		return this.table.get(var);
	}
	public int getInt(String var) {
		return Integer.parseInt(this.table.get(var));
	}
	public double getDouble(String var) {
		return Double.parseDouble(this.table.get(var));
	}
	public boolean getBoolean(String var) {
		return Boolean.parseBoolean(this.table.get(var));
	}
	public int size() {
		return this.table.size();
	}
	public String toString() {
		StringBuffer out = new StringBuffer();
		Iterator<String> i = this.table.keySet().iterator(); 
		while(i.hasNext()) {
		   String var   = (String)i.next(); 
		   String value = this.table.get(var);
		   out.append("   ");
		   out.append(var);
		   out.append(" = ");
		   out.append(value);
		   out.append("\n");
		}
		return out.toString();
	}
}
