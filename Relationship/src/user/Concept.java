package user;

import main.*;
import basic.Token;

public class Concept {
	private String basicConcept;
	private String underlineConcept; // with underlines
	private boolean isCategory;
	private boolean isOriginal;
	 
	public Concept(String originalConcept, boolean isOriginal) {
		this.basicConcept     = originalConcept.trim();
		this.isOriginal       = isOriginal;
		this.underlineConcept = Concept.blankToUnderline(this.basicConcept);
		this.isCategory       = Concept.verifyIfCategory(this.basicConcept);
		if(Concept.verifyIfCategory(this.basicConcept)) 
			this.isCategory = true;
		else
			this.isCategory = false;
	}
	public Concept(Token token) {
		this(token.image, true);
	}
		
	public String getBasicConcept() {
		return this.basicConcept;
	}
	public boolean getIsOriginal() {
		return this.isOriginal;
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
		if(this.isOriginal)
		   return this.basicConcept + " (original)";
		else
			return this.basicConcept;
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
		String out = this.basicConcept;
		out += " / " + this.underlineConcept;
		if(this.isOriginal)
		   out += " - (original)";
		if(this.isCategory)
			out += " - (category)";
		return out;
	}
}
