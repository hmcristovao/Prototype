package map;

import graph.GephiGraphData;
import graph.NodeData;
import graph.NodesTableArray;
import graph.QuantityNodesEdges;
import graph.SystemGraphData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.statistics.plugin.EigenvectorCentrality;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import user.Concept;
import user.Concept.ConceptStatus;
import user.ConceptsGroup;
import main.Constants;
import main.Log;
import main.WholeSystem;

public class ConceptMap {
	private List<Proposition> propositions;
	private Map<String,SimpleConcept> concepts;  // will be filled in fillAttributesOfFileCXL()
	private Map<String,String> links;            // will be filled in fillAttributesOfFileCXL()
	
	public ConceptMap() {
		this.propositions = new ArrayList<Proposition>();	
		this.concepts     = new HashMap<String, SimpleConcept>();  // id, SimpleConcept
		this.links        = new HashMap<String, String>();		   // id, label of link
	}
	
	public List<Proposition> getPropositions() {
		return this.propositions;
	}

	public int size() {
		return this.propositions.size();
	}
	
	public boolean insert(NodeData sourceConcept, String rawLink, NodeData targetConcept) {
		Proposition proposition = new Proposition( sourceConcept, rawLink, targetConcept);
		if(this.isExist(proposition))
			return false;
		this.propositions.add(proposition);
		return true;
	}

	public boolean insert(Concept concept) {
		Proposition proposition = new Proposition(concept);
		if(this.isExist(proposition))
			return false;
		this.propositions.add(proposition);
		return true;
	}
	
	public boolean isExist(Proposition newProposition) {
		for(Proposition proposition : this.propositions) {
			if(proposition.equals(newProposition))
				return true;
		}
		return false;
	}
	
	// remove part of link with #DDD...D
	public int upgradeConceptMap_heuristic_01_removeLinkNumber() {
		int n = 0;
		for( Proposition proposition : this.getPropositions()) {
			// at first, verify whether it is not alone concept 
			if(proposition.getLink() != null)
			{
				// verify whether there is a substring "#DDDD" (D=digit)
				int pos = proposition.getLink().lastIndexOf('#');
				if(pos != -1) {
					if(proposition.getLink().charAt(pos+1) >= '0' && proposition.getLink().charAt(pos+1) <= '9' &&
							proposition.getLink().charAt(pos+2) >= '0' && proposition.getLink().charAt(pos+2) <= '9' &&
							proposition.getLink().charAt(pos+3) >= '0' && proposition.getLink().charAt(pos+3) <= '9') {
						proposition.setLink(proposition.getLink().substring(0, pos));
						n++;
					}
				}
			}
		}
		return n;
	}
	// create the final map concept
	// it only changes the links from vocabularyTable
	public int upgradeConceptMap_heuristic_02_vocabularyTable() {
		int n = 0;
		for( Proposition proposition : this.getPropositions()) {
			String newLink = WholeSystem.getVocabularyTable().get(proposition.getLink());
			if(newLink != null) {
				proposition.setLink(newLink);
				n++;
			}
		}
		return n;
	}
	// the prefix "ConceptCategory:" in target concept is changed to:
	// link: "belongs to"
	// target concept: "... category"
	public int upgradeConceptMap_heuristic_03_categoryInTargetConcept() {
		int n = 0;
		for( Proposition proposition : this.getPropositions()) {
			// at first, verify whether it is not alone concept 
			if(proposition.getLink() != null)
			{
				if(Concept.verifyIfCategory(proposition.getTargetConcept().getLabel())) {
					proposition.setLink("belongs to");
					String newTargetConcept = Concept.extractCategory(proposition.getTargetConcept().getLabel()) + " category";
					proposition.setTargetConcept(newTargetConcept);
					n++;
				}
			}
		}
		return n;
	}
	// the prefix "Category:" in source concept is changed to:
	// source concept: "... category"
	public int upgradeConceptMap_heuristic_04_categoryInSourceConcept() {
		int n = 0;
		for( Proposition proposition : this.getPropositions()) {
			// at first, verify whether it is not alone concept 
			if(proposition.getLink() != null)
			{
				if(Concept.verifyIfCategory(proposition.getSourceConcept().getLabel())) {
					String newSourceConcept = Concept.extractCategory(proposition.getSourceConcept().getLabel()) + " category";
					proposition.setSourcetConcept(newSourceConcept);
					n++;
				}
			}
		}
		return n;
	}
	// remove self references
	public int upgradeConceptMap_heuristic_05_removeSelfReference() {
		int n = 0;
		List<Proposition> excludedPropositions = new ArrayList<Proposition>();
		for( Proposition proposition : this.getPropositions()) {
			// at first, verify whether it is not alone concept 
			if(proposition.getLink() != null)
			{
				if(proposition.getSourceConcept().getLabel().equals(proposition.getTargetConcept().getLabel())) {
					excludedPropositions.add(proposition);
					n++;
				}
			}
		}
		for(Proposition proposition : excludedPropositions) {
			this.propositions.remove(proposition);
		}
		return n;
	}
	public ConceptsGroup upgradeConceptMap_heuristic_06_createOriginalConceptsWithZeroDegree(SystemGraphData currentSystemGraphData) {
		// search in Gephi Graph, alone original concepts 
		ConceptsGroup originalConcepts = new ConceptsGroup();
		for( Node node : currentSystemGraphData.getGephiGraphData().getGephiGraph().getNodes() ) {
			if(currentSystemGraphData.getGephiGraphData().getGephiGraph().getDegree(node) == 0) {
				if(WholeSystem.getConceptsRegister().isOriginalConcept(node.getNodeData().getLabel())) {
					originalConcepts.add(WholeSystem.getConceptsRegister().getConcept(node.getNodeData().getLabel()));
				}
			}
		}
		// insert concepts selected, without link in concept map
		for(Concept concept : originalConcepts.getList()) {
			WholeSystem.getConceptMap().insert(concept);
		}
		return originalConcepts;
	}
	
	
	// create a gephiGraph from concept map and generate a gexf file
	public void buildGexfGraphFileFromConceptMap(String fileGexf) throws Exception {
		// at first, create a new gephi graph
		GephiGraphData gephiGraphData = new GephiGraphData();
		// second: fill this gephi graph with concept map data
		AttributeColumn labelAttributeColumn = gephiGraphData.getAttributeModel().getNodeTable().getColumn("Label");
		int edgeIdNumber = 0;
		for(Proposition proposition : this.propositions) {
 			// create 1� node gephiNode
			Node nodeSource = gephiGraphData.getGraphModel().factory().newNode(proposition.getSourceConcept().getLabel());
			nodeSource.getNodeData().getAttributes().setValue(labelAttributeColumn.getIndex(), proposition.getSourceConcept().getLabel());	
			gephiGraphData.getGephiGraph().addNode(nodeSource);
			
			// whether it not special case (alone concept)
			if(proposition.getTargetConcept() != null) {	
				// create 2� node gephiNode
				Node nodeTarget = gephiGraphData.getGraphModel().factory().newNode(proposition.getTargetConcept().getLabel());
				nodeTarget.getNodeData().getAttributes().setValue(labelAttributeColumn.getIndex(), proposition.getTargetConcept().getLabel());
				gephiGraphData.getGephiGraph().addNode(nodeTarget);

				// create the edge (put different id to each edge because it is not possible same id in the record of getxf) 
				Edge edge = gephiGraphData.getGephiGraph().getGraphModel().factory().newEdge("#"+edgeIdNumber, nodeSource, nodeTarget, 1, true);
				edge.getEdgeData().getAttributes().setValue(labelAttributeColumn.getIndex(), proposition.getLink());				
				gephiGraphData.getGephiGraph().addEdge(edge);
				edgeIdNumber++;
			}
		}
 		// third: calculate measures
 		gephiGraphData.calculateGephiGraphDistanceMeasures();
 		gephiGraphData.calculateGephiGraphEigenvectorMeasure();
 		gephiGraphData.classifyConnectedComponent();
 		
		// fourth: create a file from gephi graph
		gephiGraphData.buildGexfGraphFile(fileGexf);
	}
		
	// create a TXT file from concept map
	// use tab ('\t') to separate concepts and links
	public void buildTxtFileFromConceptMap(String fileTxt) throws Exception {
		StringBuffer str = new StringBuffer();
		for(Proposition proposition : this.propositions) {
 			str.append(proposition.getSourceConcept().getLabel());
 			str.append('\t');
 			// verify whether special case (alone concept)
 			if(proposition.getLink() != null) {
 				str.append(proposition.getLink());
 				str.append('\t');
 				str.append(proposition.getTargetConcept().getLabel());
 			}
 			str.append('\r');
			str.append('\n');
		}
		BufferedWriter outFile = new BufferedWriter(new FileWriter(fileTxt)); 
		outFile.write(str.toString());
		outFile.close();
	}
		
	// create attributes of CXL file from Propositions in ConceptMap (after processing of all heuristics)
	// return quantity of joins
	private int fillAttributesOfFileCXL() {
		int numConcept       = 1;  
		int numJoin          = 1;     
		int numLinkingPhrase = 1;  

		String idFoundConcept;  
		String idFoundJoin;
		String idFoundLink;

		for(int i=0; i < this.propositions.size(); i++) {
			Proposition prop_i = this.propositions.get(i);
			
			// special case: concept alone
			if(prop_i.getTargetConcept() == null) {
				idFoundConcept = "c"+numConcept;
				numConcept++;
				prop_i.setIdSourceConcept(idFoundConcept);
				this.concepts.put(idFoundConcept, prop_i.getSourceConcept());
				continue;
			}
			
			// figure out the source concept
			idFoundConcept = null;
			for(int j=0; j < i; j++) {
				Proposition prop_j = this.propositions.get(j);
				// search in the source concepts
				if(prop_i.getSourceConcept().getLabel().equals(prop_j.getSourceConcept().getLabel())) {
					idFoundConcept = prop_j.getIdSourceConcept();
					break;
				}
				// search in the target concepts
				if(prop_i.getSourceConcept().getLabel().equals(prop_j.getTargetConcept().getLabel())) {
					idFoundConcept = prop_j.getIdTargetConcept();
					break;
				}
			}
			// if did not find:
			if(idFoundConcept == null) {
				idFoundConcept = "c"+numConcept;
				numConcept++;
			}
			prop_i.setIdSourceConcept(idFoundConcept);
			this.concepts.put(idFoundConcept, prop_i.getSourceConcept());
		
			
			// figure out the target concept
			idFoundConcept = null;
			for(int j=0; j < i; j++) {
				Proposition prop_j = this.propositions.get(j);
				// search in the source concepts
				if(prop_i.getTargetConcept().getLabel().equals(prop_j.getSourceConcept().getLabel())) {
					idFoundConcept = prop_j.getIdSourceConcept();
					break;
				}
				// search in the target concepts
				if(prop_i.getTargetConcept().getLabel().equals(prop_j.getTargetConcept().getLabel())) {
					idFoundConcept = prop_j.getIdTargetConcept();
					break;
				}
			}
			// if did not find:
			if(idFoundConcept == null) {
				idFoundConcept = "c"+numConcept;
				numConcept++;
			}
			prop_i.setIdTargetConcept(idFoundConcept);
			this.concepts.put(idFoundConcept, prop_i.getTargetConcept());		
		
			// figure out the join and link (source)
			idFoundLink = null;
			idFoundJoin = null;
			for(int j=0; j < i; j++) {
				Proposition prop_j = this.propositions.get(j);
				// search for source concept and link equals
				if(prop_i.getSourceConcept().getLabel().equals(prop_j.getSourceConcept().getLabel()) &&
				   prop_i.getLink().equals(prop_j.getLink())) {
					idFoundJoin = prop_j.getIdSourceJoin();
					idFoundLink = prop_j.getIdLinkingPhrase();
					break;
				}
			}
			// if found:
			if(idFoundJoin != null) {
				prop_i.setIdSourceJoin(idFoundJoin);
				prop_i.setIdLinkingPhrase(idFoundLink);
				prop_i.setIdTargetJoin(numJoin);
				numJoin++;
			}
			// if did not find, then will figure out the join and link (target):
			else {
				idFoundJoin = "j"+numJoin;
				prop_i.setIdSourceJoin(idFoundJoin);
				numJoin++;
				
				idFoundJoin = null;
				idFoundLink = null;
				for(int j=0; j < i; j++) {
					Proposition prop_j = this.propositions.get(j);
					// search for target concept and link equals
					if(prop_i.getTargetConcept().getLabel().equals(prop_j.getTargetConcept().getLabel()) &&
							prop_i.getLink().equals(prop_j.getLink())) {
						idFoundJoin = prop_j.getIdTargetJoin();
						idFoundLink = prop_j.getIdLinkingPhrase();
						break;
					}
				}
				// if did not find:
				if(idFoundJoin == null) {
					idFoundJoin = "j"+numJoin;
					numJoin++;
					idFoundLink = "l"+numLinkingPhrase;
					numLinkingPhrase++;
				}
				prop_i.setIdTargetJoin(idFoundJoin);
				prop_i.setIdLinkingPhrase(idFoundLink);
			}
			this.links.put(idFoundLink, prop_i.getLink());
		}
		return numJoin-1;
	}

	// create a CLX file from concept map
	// return content of CLX file
	public String buildCxlFileFromConceptMap(String fileClx) throws Exception {
		// at firt, fill attributes of Proposition class to create CLX file
		int countJ = this.fillAttributesOfFileCXL();

		// buffer to store the content that will be stored in CLX file
		StringBuffer str = new StringBuffer();
	
		str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		str.append("<cmap xmlns:dcterms=\"http://purl.org/dc/terms/\"\r\n");
		str.append("xmlns=\"http://cmap.ihmc.us/xml/cmap/\"\r\n");
		str.append("xmlns:dc=\"http://purl.org/dc/elements/1.1/\"\r\n");
		str.append("xmlns:vcard=\"http://www.w3.org/2001/vcard-rdf/3.0#\">\r\n");
		
		str.append("<map>\r\n");
		
		// section: 
		str.append("<concept-list>\r\n");
		for(int i=1; i <= this.concepts.size(); i++) {
			SimpleConcept simpleConcept = this.concepts.get("c"+i); // get id's: c1, c2, c3, ...
			str.append("\t<concept id=\"");
			str.append(simpleConcept.getIdConcept());
			str.append("\" label=\"");
			str.append(simpleConcept.getLabel());
			str.append("\"");
			if(simpleConcept.getNodeData().getAbstractAttribute() != null) {
				str.append(" short-comment=\"");
				str.append(simpleConcept.getNodeData().getAbstractAttribute().replaceAll("\\\"", "'").replace("\\",""));
				str.append(" (font: DBPEDIA)");
				str.append("\" long-comment=\"\"");
			}
			else if(simpleConcept.getNodeData().getCommentAttribute() != null) {
				str.append(" short-comment=\"");
				str.append(simpleConcept.getNodeData().getCommentAttribute().replaceAll("\\\"", "'").replace("\\",""));
				str.append(" (font: DBPEDIA)");
				str.append("\" long-comment=\"\"");
			}
			str.append("/>\r\n");
		}
		str.append("</concept-list>\r\n");
		
		// section: <linking-phrase-list>
		str.append("<linking-phrase-list>\r\n");
		for(int i=1; i <= this.links.size(); i++) {
			String idLink = "l"+i;   // get id's: l1, l2, l3, ...
			String labelLink = this.links.get(idLink); 
			str.append("\t<linking-phrase id=\"");
			str.append(idLink);
			str.append("\" label=\"");
			str.append(labelLink);
			str.append("\"/>\r\n");
		}
		str.append("</linking-phrase-list>\r\n");
		
		// section: <connection-list>
		str.append("<connection-list>\r\n");
		for(Proposition proposition : this.propositions) {
			// special case: concept alone
			if(proposition.getTargetConcept() == null) {
				continue;
			}
			// source join
			str.append("\t<connection id=\"");
			str.append(proposition.getIdSourceJoin());
			str.append("\" from-id=\"");
			str.append(proposition.getIdSourceConcept());
			str.append("\" to-id=\"");
			str.append(proposition.getIdLinkingPhrase());
			str.append("\"/>\r\n");
			// target join
			str.append("\t<connection id=\"");
			str.append(proposition.getIdTargetJoin());
			str.append("\" from-id=\"");
			str.append(proposition.getIdLinkingPhrase());
			str.append("\" to-id=\"");
			str.append(proposition.getIdTargetConcept());
			str.append("\"/>\r\n");
		}
		str.append("</connection-list>\r\n");

		// section: <concept-appearance-list>
		str.append("<concept-appearance-list>\r\n");
		for(int i=1; i <= this.concepts.size(); i++) {
			SimpleConcept simpleConcept = this.concepts.get("c"+i); // get id's: c1, c2, c3, ...
			str.append("\t<concept-appearance id=\"");
			str.append(simpleConcept.getIdConcept());
			str.append("\" ");
			if(simpleConcept.getNodeData().getStatus() == ConceptStatus.originalConcept) {
				str.append("background-color=\"");
				str.append(WholeSystem.configTable.getString("backGroundcolorOriginalConcept").replace(".", ","));
				str.append("\" ");
			}
			if(simpleConcept.getNodeData().getAbstractAttribute() != null || simpleConcept.getNodeData().getCommentAttribute() != null) {
				str.append("border-thickness=\"");
				str.append(WholeSystem.configTable.getString("borderThicknessConceptWithHint"));
				str.append("\" ");
			}
			str.append("/>\r\n");
		}
		str.append("</concept-appearance-list>\r\n");

		
		// section: <connection-appearance-list>
		str.append("<connection-appearance-list>\r\n");
		for(int i=1; i <= countJ; i++) {
			str.append("\t<connection-appearance id=\"");
			str.append("j"+i);
			str.append("\" from-pos=\"center\" to-pos=\"center\" type=\"straight\" arrowhead=\"yes\"/>\r\n");
		}
		str.append("</connection-appearance-list>\r\n");
		
		str.append("</map>\r\n");
		str.append("</cmap>\r\n");		
	
		// save in file
		String cxlFileContent = str.toString();
		BufferedWriter outFile = new BufferedWriter(new FileWriter(fileClx)); 
		outFile.write(cxlFileContent);
		outFile.close();
		
		return cxlFileContent;
	}


	public String toStringComplete() {
		StringBuffer out = new StringBuffer();
		out.append("Total propositions: ");
		out.append(this.propositions.size());
		out.append("\n");
		int i =1;
		for(Proposition p : this.propositions) {
			out.append("\nProposition ");
			out.append(i);
			out.append(":\n");
			out.append(p.toStringComplete());
			out.append("\n");
			i++;
		}
		return out.toString();
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		for(Proposition p : this.propositions) {
			out.append("   ");
			out.append(p.toString());
			out.append("\n");
		}
		return out.toString();
	}
}

