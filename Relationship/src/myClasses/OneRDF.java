package myClasses;

import com.hp.hpl.jena.rdf.model.Statement;

public class OneRDF {
	
	private Statement statement;
	
	private ItemRDF subject;
	private ItemRDF predicate;
	private ItemRDF object;
	
	public ItemRDF getSubject() {
		return this.subject;
	}
	public ItemRDF getPredicate() {
		return this.predicate;
	}
	public ItemRDF getObject() {
		return this.object;
	}
	public Statement getStatement() {
		return this.statement;
	}
	
	public OneRDF(Statement statement, ItemRDF subject, ItemRDF predicate, ItemRDF object) {
		this.statement = statement;
		this.subject   = subject;
		this.predicate = predicate;
		this.object    = object;
	}

	@Override
	public String toString() {
		return  "\nsubject = " + this.getSubject() + 
				"\npredicate = " + this.getPredicate() + 
				"\nobject = " + this.getObject();
	}
}

