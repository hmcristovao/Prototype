package myClasses;

public class NodeRDF extends ItemRDF {
	private Constants.Level level;   
	// level can be:
	// Contants.Level.originalConcept
	// Constants.Level.commonConcept
	
	public NodeRDF(String value, Dataset dataset) {
		super(value);
		this.level = this.qualifyLevel(dataset);
	}
	public Constants.Level getLevel() {
		return this.level;
	}
	
	public Constants.Level qualifyLevel(Dataset dataset) {
		for(QuerySparql x: dataset.getListQuerySparql().getList()) {
			if(Constants.ignoreCaseConcept) {
				if(this.getShortName().equalsIgnoreCase(x.getConcept().getUnderlineConcept())) 
					return Constants.Level.originalConcept;
			}
			else {
				if(this.getShortName().equals(x.getConcept().getUnderlineConcept())) 
					return Constants.Level.originalConcept;
			}
		}
		return Constants.Level.commonConcept;
	}

}
