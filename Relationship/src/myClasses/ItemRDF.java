package myClasses;

public class ItemRDF {
	
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

