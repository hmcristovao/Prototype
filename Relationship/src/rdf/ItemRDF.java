package rdf;

public class ItemRDF {
	
	private String value;
	private String shortName;
	
	public ItemRDF(String value) {
		this.value = value;
		this.shortName = ItemRDF.doShortName(value);
	}
	public String getValue() {
		return this.value;
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
		return this.getValue() + " (" + this.getShortName() + ")";
	}

}
