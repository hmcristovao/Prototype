package user;

import java.util.LinkedList;

public class ListConcept {
	private LinkedList<Concept> list;
	
	public ListConcept() {
		this.list = new LinkedList<Concept>();
	}

	public LinkedList<Concept> getList() {
		return this.list;
	}
	public int size() {
		return this.list.size();
	}
	
	public void add(Concept item) {
		this.list.add(item);  
	}
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		if(this.list != null)
    		for(Concept x : this.list) {
	    		str.append("- ");
    			str.append(x.toStringShort());
	    		str.append("\n");
		    }
		return str.toString();
	}
}
