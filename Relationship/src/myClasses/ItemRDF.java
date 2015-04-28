package myClasses;

import com.hp.hpl.jena.rdf.model.Statement;

public class ItemRDF {
	
	private SubjectRDF subject;
	private PredicateRDF predicate;
	private ObjectRDF object;
	private Statement statement;
	
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
	
	public ItemRDF(SubjectRDF subject, PredicateRDF predicate, ObjectRDF object) {
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

