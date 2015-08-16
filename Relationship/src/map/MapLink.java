package map;

public class MapLink extends MapElement {

	public MapLink(String label) {
		super(label);		
	}

	public String toString() {
		return "Link = " + this.label;
	}
}