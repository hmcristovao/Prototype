package myClasses;

import com.hp.hpl.jena.rdf.model.Statement;

public class ItemRDF {
	
	private Statement statement;
	private SubjectRDF subject;
	private PredicateRDF predicate;
	private ObjectRDF object;
	
	public SubjectRDF getSubject() {
		return this.subject;
	}
	public PredicateRDF getPredicate() {
		return this.predicate;
	}
	public ObjectRDF getObject() {
		return this.object;
	}
	public Statement getStatement() {
		return this.statement;
	}
	
	public ItemRDF(Statement statement, SubjectRDF subject, PredicateRDF predicate, ObjectRDF object) {
		this.statement = statement;
		this.subject   = subject;
		this.predicate = predicate;
		this.object    = object;
	}

	@Override
	public String toString() {
		return this.getSubject()+" -> "
	          +this.getPredicate()+" -> "
	          +this.getPredicate();
	}
}

