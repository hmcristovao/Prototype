package user;

import main.*;
import basic.Token;

public class Concept {
	private String basicConcept;
	private String underlineConcept; // with underlines
	private boolean isCategory;
	private Config.Status status;
	 
	public Concept(String originalConcept, Config.Status status) {
		this.basicConcept     = originalConcept.trim();
		this.status           = status;
		this.underlineConcept = Concept.blankToUnderline(this.basicConcept);
		this.isCategory       = Concept.verifyIfCategory(this.basicConcept);
		if(Concept.verifyIfCategory(this.basicConcept)) 
			this.isCategory = true;
		else
			this.isCategory = false;
	}
	public Concept(Token token) {
		this(token.image, Config.Status.originalConcept);
	}
		
	public String getBasicConcept() {
		return this.basicConcept;
	}
	public Config.Status getStatus() {
		return this.status;
	}
	public boolean isOriginal() {
		return this.status == Config.Status.originalConcept;
	}
	public String getUnderlineConcept() {
		return this.underlineConcept;
	}
	public boolean getIsCategory() {
		return this.isCategory;
	}
	
	public static String blankToUnderline(String str) {
		return str.replace(" ","_");
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
	public static String extractCategory(String str) {
		return str.substring(9);
	}

	public String toStringShort() {
		return this.basicConcept + " ("+Concept.statusToString(this.status)+")";
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
		if (basicConcept == null) {
			if (other.basicConcept != null)
				return false;
		} else if (!basicConcept.equals(other.basicConcept))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		String out = this.underlineConcept;
		out += " / " + this.toStringShort();
		if(this.isCategory)
			out += " - (category)";
		return out;
	}
}
