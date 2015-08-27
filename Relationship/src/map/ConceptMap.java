package map;

import graph.GephiGraphData;
import graph.NodeData;
import graph.NodesTableArray;
import graph.QuantityNodesEdges;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
import org.gephi.statistics.plugin.EigenvectorCentrality;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

import user.Concept;
import main.Config;
import main.Log;
import main.WholeSystem;

public class ConceptMap {
	private List<Proposition> propositions;

	public ConceptMap() {
		this.propositions = new ArrayList<Proposition>();		
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

	// the prefix "Category:" in target concept is changed to:
	// link: "belongs to"
	// target concept: "... category"
	public int upgradeConceptMap_heuristic_03_categoryInTargetConcept() {
		int n = 0;
		for( Proposition proposition : this.getPropositions()) {
			if(Concept.verifyIfCategory(proposition.getTargetConcept())) {
				proposition.setLink("belongs to");
				String newTargetConcept = Concept.extractCategory(proposition.getTargetConcept()) + " category";
				proposition.setTargetConcept(newTargetConcept);
				n++;
			}
		}
		return n;
	}
	
	// the prefix "Category:" in source concept is changed to:
	// source concept: "... category"
	public int upgradeConceptMap_heuristic_04_categoryInSourceConcept() {
		int n = 0;
		for( Proposition proposition : this.getPropositions()) {
			if(Concept.verifyIfCategory(proposition.getSourceConcept())) {
				String newSourceConcept = Concept.extractCategory(proposition.getSourceConcept()) + " category";
				proposition.setSourceConcept(newSourceConcept);
				n++;
			}
		}
		return n;
	}
	
	// create a gephiGraph from concept map and generate a gexf file
	public void buildGexfGraphFileFromConceptMap(String fileGexf) throws Exception {
		// at first, create a new gephi graph
		GephiGraphData gephiGraphData = new GephiGraphData();
		// second: fill this gephi graph with concept map data
		AttributeColumn labelAttributeColumn = gephiGraphData.getAttributeModel().getNodeTable().getColumn("Label");
		int edgeIdNumber = 0;
		for(Proposition proposition : this.propositions) {
 			// create 1º node gephiNode
			Node nodeSource = gephiGraphData.getGraphModel().factory().newNode(proposition.getSourceConcept());
			nodeSource.getNodeData().getAttributes().setValue(labelAttributeColumn.getIndex(), proposition.getSourceConcept());	
			gephiGraphData.getGephiGraph().addNode(nodeSource);
			
			// create 2º node gephiNode
			Node nodeTarget = gephiGraphData.getGraphModel().factory().newNode(proposition.getTargetConcept());
			nodeTarget.getNodeData().getAttributes().setValue(labelAttributeColumn.getIndex(), proposition.getTargetConcept());
			gephiGraphData.getGephiGraph().addNode(nodeTarget);
			
			// create the edge (put different id to each edge because it is not possible same id in the record of getxf) 
			Edge edge = gephiGraphData.getGephiGraph().getGraphModel().factory().newEdge("#"+edgeIdNumber, nodeSource, nodeTarget, 1, true);
			edge.getEdgeData().getAttributes().setValue(labelAttributeColumn.getIndex(), proposition.getLink());				
			gephiGraphData.getGephiGraph().addEdge(edge);
			edgeIdNumber++;
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
		BufferedWriter outFile = new BufferedWriter(new FileWriter(fileTxt)); 
		for(Proposition proposition : this.propositions) {
 			outFile.write(proposition.getSourceConcept());
 			outFile.write('\t');
 			outFile.write(proposition.getLink());
 			outFile.write('\t');
 			outFile.write(proposition.getTargetConcept());	
 			outFile.write('\r');
			outFile.write('\n');
		}
		outFile.close();
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
