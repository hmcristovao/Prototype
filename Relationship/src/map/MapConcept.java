package map;

public class MapConcept extends MapElement {
	String  hint;           //  abstract and comment (information shown when pointer mouse passes above)
	String  homePage;       // address of the home page 
	int     partition;      // number (0,1,2, ...). Without = -1

	public MapConcept(String label) {
		this(label, null, null, -1);		
	}
	public MapConcept(String label, int partition) {
		this(label, null, null, partition);		
	}
	public MapConcept(String label, String hint, String homePage, int partition) {
		super(label);
		this.hint           = hint;
		this.homePage	    = homePage;    
		this.partition      = partition;		
	}

	public String getHint() {
		return this.hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}
	public String getHomePage() {
		return this.homePage;
	}
	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}
	public int getPartition() {
		return this.partition;
	}
	public void setPartition(int partition) {
		this.partition = partition;
	}

	public String toString() {
		return  "Concept = " + this.label +
				"\nHint (abstact/comment) = " + this.hint +
				"\nHome page = " + this.homePage +
				"\nPartition = " + this.partition;		
	}
}