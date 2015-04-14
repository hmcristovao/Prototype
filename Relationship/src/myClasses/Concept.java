package myClasses;

//import myJavacc.Token;
import Token;

public class Concept {
	private String description;
	
	public Concept(String description) {
		this.description = description;   // acertar isso !!!!
	}
	public Concept(Token token) {
		this.description = token.image;  // acertar isso !!!!
	}
	
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Concept [description=" + description + "]";
	}



}
