package myClasses;

import java.util.LinkedList;

public class ListConcept {
	private LinkedList<Concept> list;
	
	public ListConcept() {
		super();
	}
	public ListConcept(LinkedList<Concept> list) {
		super();
		this.list = list;
	}
	public LinkedList<Concept> getList() {
		return this.list;
	}
	public void setList(LinkedList<Concept> list) {
		this.list = list;
	}
	public void insert(Concept concept) {
		this.list.add(concept);  
	}
	@Override
	public String toString() {
		return "ListConcept [list=" + this.list + "]";
	}
}
