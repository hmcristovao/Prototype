package map;

import graph.GephiGraphData;
import graph.NodeData;
import graph.NodesTableArray;
import graph.QuantityNodesEdges;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.io.exporter.api.ExportController;
import org.gephi.io.exporter.spi.GraphExporter;
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
	
	// create the final map concept
	// it only changes the links from vocabularyTable
	public int upgradeConceptMap_withLinkVocabularyTable() {
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
	public int upgradeConceptMap_withHeuristic_1() {
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
	
	
	// create a gephiGraph from concept map and generate a gexf file
	public void buildGexfGraphFileFromConceptMap(String fileGexf) throws Exception {
		// at first, create a new gephi graph
		GephiGraphData gephiGraphData = new GephiGraphData();
		// second: fill this gephi graph with concept map data
 		for(Proposition proposition : this.propositions) {
 			// create the two new gephiNode
			Node nodeSource = gephiGraphData.getGephiGraph().getGraphModel().factory().newNode(proposition.getSourceConcept());
			Node nodeTarget = gephiGraphData.getGephiGraph().getGraphModel().factory().newNode(proposition.getTargetConcept());
 			// create the edge
			Edge edge = gephiGraphData.getGraphModel().factory().newEdge(proposition.getLink(), nodeSource, nodeTarget, 1, true);
			Log.consoleln(nodeSource.toString());
			Log.consoleln(nodeTarget.toString());
			Log.consoleln(edge.getId());
		}		
		// third: create a file from gephi graph
		gephiGraphData.buildGexfGraphFile(fileGexf);
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
