package myClasses;

import basic.Token;

public class Concept {
	private String originalConcept;
	private String formatedConcept; // with two points and underlines
	 
	public Concept(String originalConcept) {
		this.originalConcept = originalConcept.trim();
		this.formatedConcept = Concept.format(this.originalConcept);
	}
	public Concept(Token token) {
		this.originalConcept = token.image.trim();
		this.formatedConcept = Concept.format(this.originalConcept);
	}
	
	// put ":" at first and replace blank for underline
	public static String format(String str) {
		return ":" + str.replace(" ","_");
	}
	
	public String getOriginalConcept() {
		return this.originalConcept;
	}
	public String getFormatedConcept() {
		return this.formatedConcept;
	}

	public void setConcept(String originalDescription) {
		this.originalConcept = originalConcept.trim();
		this.formatedConcept = Concept.format(this.originalConcept);
	}

	@Override
	public String toString() {
		return "Concept = " + this.getOriginalConcept() + " / " 
	           + this.getFormatedConcept() + "]";
	}



}
