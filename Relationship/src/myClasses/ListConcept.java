// essa classe ainda não serve para nada
// talvez, depois sirva para armazenar métricas dos conceitos
// mas, para isso, precisaria de outra classe: Metricas

package myClasses;

import java.util.LinkedList;

public class ListConcept {
	private LinkedList<Concept> list;
	
	public ListConcept() {
		this.list = new LinkedList<Concept>();
	}

	public LinkedList<Concept> getList() {
		return this.list;
	}

	public void insert(Concept concept) {
		this.list.add(concept);  
	}
	@Override
	public String toString() {
		return "ListConcept [list=\n" + this.getList() + "]";
	}
}
