package rdf;

import main.Config;

public class NodeRDF extends ItemRDF {
	private Config.Level status;   
	
	public NodeRDF(String value, SetQuerySparql setQuerySparql) {
		super(value);
		this.status = this.qualifyStatus(setQuerySparql);
	}
	public Config.Level getStatus() {
		return this.status;
	}
	
	// verify if the node belongs to original concepts list
	private Config.Level qualifyStatus(SetQuerySparql setQuerySparql) {
		for(QuerySparql x: setQuerySparql.getListQuerySparql()) {
			if(Config.ignoreCaseConcept) {
				if(this.getShortName().equalsIgnoreCase(x.getConcept().getUnderlineConcept())) 
					return Config.Level.originalConcept;
			}
			else {
				if(this.getShortName().equals(x.getConcept().getUnderlineConcept())) 
					return Config.Level.originalConcept;
			}
		}
		return Config.Level.commonConcept;
	}
	
prob detectado:
	os novos conceitos são lidos (pelo metodo acima) como originais.
	Os antigos originais tem o RDF devidametne marcada (pois eles foram copiados)
Solução:
	modificar o método acima....acima talvez colocar um status no conceito para classifica-lo como 
	original ou added 
	
	outro problema: colocar a pasta de log fora do projeto, está fincando muito grande
	criar também a pasta para GDX ????
	
	
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(super.toString());
		if(this.status == Config.Level.originalConcept) 
			out.append(" - \"original concept\"");
		return out.toString();
	}

}
