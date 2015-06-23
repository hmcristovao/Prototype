package rdf;

import user.Concept;

public class ItemRDF {
	
	private String longName;           // complete: contains address 
	private String shortUnderlineName; // still contains underline
	private String shortBlankName;     // underline change for blank
	
	public ItemRDF(String longName) {
		this.longName           = longName;
		this.shortUnderlineName = ItemRDF.doShortName(longName);
		this.shortBlankName     = Concept.underlineToBlank(this.shortUnderlineName);
	}
	public String getLongName() {
		return this.longName;
	}
	public String getShortUnderlineName() {
		return this.shortUnderlineName;
	}
	public String getShortBlankName() {
		return this.shortBlankName;
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
		return this.longName + " (" + this.shortBlankName + ")";
	}
}
