package myClasses;

public class NodeRDF extends ItemRDF {
	private int level;   
	// level can be:
	// Contants.Level.originalConcept
	// Constants.Level.commonConcept
	
	public NodeRDF(String value, int level) {
		super(value);
		this.level = level;
	}
	public int getLevel() {
		return this.level;
	}

}
