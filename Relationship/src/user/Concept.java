package user;

import main.Debug;
import basic.Token;

public class Concept {
	private String originalConcept;
	private String underlineConcept; // with underlines
	private boolean isCategory;
	 
	public Concept(String originalConcept) {
		this.originalConcept  = originalConcept.trim();
		this.underlineConcept = Concept.blankToUnderline(this.originalConcept);
		this.isCategory       = Concept.verifyIfCategory(this.originalConcept);
		if(Concept.verifyIfCategory(this.originalConcept)) 
			this.isCategory = true;
		else
			this.isCategory = false;
	}
	public Concept(Token token) {
		this(token.image);
	}
		
	public String getOriginalConcept() {
		return this.originalConcept;
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

	@Override
	public String toString() {
		return this.getOriginalConcept();
	}
}
