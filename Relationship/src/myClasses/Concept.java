package myClasses;

import basic.Token;

public class Concept {
	private String originalConcept;
	private String underlineConcept; // with underlines
	 
	public Concept(String originalConcept) {
		this.originalConcept = originalConcept.trim();
		this.underlineConcept = Concept.underlineToBlank(this.originalConcept);
	}
	public Concept(Token token) {
		this.originalConcept = token.image.trim();
		this.underlineConcept = Concept.underlineToBlank(this.originalConcept);
	}
	
	
	public static String blankToUnderline(String str) {
		return str.replace(" ","_");
	}
	public static String underlineToBlank(String str) {
		return str.replace("_"," ");
	}
	
	public String getOriginalConcept() {
		return this.originalConcept;
	}
	public String getUnderlineConcept() {
		return this.underlineConcept;
	}

	public void setConcept(String originalDescription) {
		this.originalConcept = originalConcept.trim();
		this.underlineConcept = Concept.blankToUnderline(this.originalConcept);
	}

	@Override
	public String toString() {
		return "Concept = " + this.getOriginalConcept() + " / " 
	           + this.getUnderlineConcept() + "]";
	}



}
