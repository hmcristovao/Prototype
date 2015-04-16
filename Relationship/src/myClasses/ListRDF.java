package myClasses;

import java.util.LinkedList;

public class ListRDF {
	private LinkedList<ItemRDF> list;
	
	public ListRDF() {
		this.list = new LinkedList<ItemRDF>();
	}
	public ListRDF(LinkedList<ItemRDF> list) {
		super();
		this.list = list;
	}
	public LinkedList<ItemRDF> getList() {
		return this.list;
	}
	public int size() {
		return this.list.size();
	}
	
	public void insert(ItemRDF item) {
		this.list.add(item);  
	}
	@Override
	public String toString() {
		return "ListRDF [list=\n" + this.getList() + "]";
	}
}