package user;

import main.*;
import basic.Token;

public class Concept {
	private String        fullName;  // with address and underline
	private String        blankName;
	private String        underlineName; // with underline
	private Config.Status status;
	private int           iteration;
	private boolean       isCategory;
	private int           registerConnectedComponent[];  // indexed by iteration number
	
	public Concept(String fullName, String blankName, Config.Status status, int iteration, int connectedComponent) {
		this.fullName                   = fullName;
		this.blankName                  = blankName.trim();
		this.underlineName              = Concept.blankToUnderline(this.blankName);
		this.status                     = status;
		this.iteration                  = iteration;
		this.isCategory                 = Concept.verifyIfCategory(this.blankName);
		this.registerConnectedComponent = new int[Config.maxIteration];
		for(int i=0; i<Config.maxIteration; i++)
			this.registerConnectedComponent[i] = Config.withoutConnectedComponent;
		this.registerConnectedComponent[iteration] = connectedComponent;
	}
	public Concept(Token token) {
		this(Config.originalConceptAddress+Concept.blankToUnderline(token.image), token.image, Config.Status.originalConcept, 0, Config.withoutConnectedComponent);  // this is before the first iteration
	}
		
	public String getFullName() {
		return this.fullName;
	}
	public String getBlankName() {
		return this.blankName;
	}
	public Config.Status getStatus() {
		return this.status;
	}
	public int getIteration() {
		return this.iteration;
	}
	public boolean isOriginal() {
		return this.status == Config.Status.originalConcept;
	}
	public String getUnderlineConcept() {
		return this.underlineName;
	}
	public boolean getIsCategory() {
		return this.isCategory;
	}
	
	public int[] getRegisterNodeData() {
		return this.registerConnectedComponent;
	}
	public int getNodeData(int i) {
		return this.registerConnectedComponent[i];
	}
	public int getConnectedComponent(int iteration) {
		return this.registerConnectedComponent[iteration];
	}

	
	public static String blankToUnderline(String str) {
		return str.trim().replace(" ","_");
	}
	
	public static String underlineToBlank(String str) {
		return str.replace("_"," ");
	}
	
	// verify if exist "Category:" as preceded of the concept
	public static boolean verifyIfCategory(String str) {
		if(str.length() > 9)
			if(str.substring(0, 9).compareTo("Category:") == 0)
				return true;
		return false;
	}
	public static String extractCategory(String shortName) {
		return shortName.substring(9);
	}
	public static String extractCategoryFullName(String fullName) {
		int posCategory    = fullName.indexOf("Category:");
		if(posCategory != -1) {
			String onlyAddress = fullName.substring(0, posCategory);
		    String onlyName    = fullName.substring(posCategory+9); 
		    return onlyAddress + onlyName;
		}
		return fullName;
	}

	public static String statusToString(Config.Status status) {
		if(status == Config.Status.commonConcept)
			return "common";
		else if(status == Config.Status.originalConcept)
			return "original";
		else if(status == Config.Status.selectedBetweennessClosenessConcept)
			return "betweenness+closeness";
		else if(status == Config.Status.selectedEigenvectorConcept)
			return "eigenvector";
		else if(status == Config.Status.selected)
			return "selected";
		else 
			return "";
	}
	public static Config.Status stringToStatus(String str) {
		if(str.equals("common"))
			return Config.Status.commonConcept;
		else if(str.equals("original"))
			return Config.Status.originalConcept;
		else if(str.equals("betweenness+closeness"))
			return Config.Status.selectedBetweennessClosenessConcept;
		else if(str.equals("eigenvector"))
			return Config.Status.commonConcept;
		else if(str.equals("selected"))
			return Config.Status.selected;
		else 
			return Config.Status.noStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Concept))
			return false;
		Concept other = (Concept) obj;
		if (this.fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!this.fullName.equals(other.fullName))
			return false;
		return true;
	}

	public String toStringShort() {
		return this.blankName +
	           " - "  + Concept.statusToString(this.status) +
	           " (it: " + this.iteration + ")";
	}
	public String toStringLong() {
		String out = "[fullName: "  + this.fullName +    "]" +
					 "[blankName: " + this.blankName +    "]" +
					 "[underline: " + this.underlineName +"]" + 
				     "[category: "  + (this.isCategory ? "true" : "false") + "]" +
					 "[status: "    + Concept.statusToString(this.status) + "]" +
				     "[iteration: " + this.iteration + "]" +
					 "[connected components: ";
		for(int i=0; i<this.registerConnectedComponent.length; i++) 
			out += this.registerConnectedComponent[i] + " ";
		out += "]\n";
		return out;
	}
	@Override
	public String toString() {
		String out = this.underlineName + 
				     " / "  + this.blankName +
		             " - "  + Concept.statusToString(this.status) +
		             " (i: " + this.iteration + ")";
		if(this.isCategory)
			out += " - (category)";
		return out;
	}
}
