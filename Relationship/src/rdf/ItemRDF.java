package rdf;

public class ItemRDF {
	
	private String longName;
	private String shortName;
	
	public ItemRDF(String longName) {
		this.longName = longName;
		this.shortName = ItemRDF.doShortName(longName);
	}
	public String getLongName() {
		return this.longName;
	}
	public String getShortName() {
		return this.shortName;
	}
	
	// shortening a name, normally cut the address part and keep on the suffix until the bar
	static private String doShortName(String longName) {
		String shortName;
		int positionLastBar = longName.lastIndexOf("/");
		shortName = longName.substring(positionLastBar+1);
		return shortName;
	}
	
	@Override
	public String toString() {
		return this.getLongName() + " (" + this.getShortName() + ")";
	}

}
