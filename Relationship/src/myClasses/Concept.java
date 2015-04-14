package myClasses;

import basic.Token;

public class Concept {
	private String description;
	 
	public Concept(String description) {
		this.description = description;   
	}
	public Concept(Token token) {
		this.description = token.image.trim(); 
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
