package myClasses;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

public class GephiGraphData {

	private GraphModel graphModel;
	private AttributeModel attributeModel;
	private Graph gephiGraph;
	
	public GephiGraphData() {
		
		//ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
		//pc.newProject();
		//Workspace workspace = pc.getCurrentWorkspace();
		
		this.graphModel =  Lookup.getDefault().lookup(GraphController.class).getModel();
		this.attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		
		this.gephiGraph = this.graphModel.getGraph();
	}
	
	public Graph getGephiGraph() {
		return this.gephiGraph;
	}
	
	// copy all graph from Stream to Gephi format
	public void init(StreamGraphData streamGraphData) {
		org.graphstream.graph.Graph streamGraph = streamGraphData.getStreamGraph();
		for( org.graphstream.graph.Node streamNode : streamGraph.getEachNode() ) {
			String idNode = streamNode.toString();
			Node gephiNode = graphModel.factory().newNode(idNode);
			this.gephiGraph.addNode(gephiNode);
		}
		for( org.graphstream.graph.Edge streamEdge : streamGraph.getEachEdge() ) {
			String idNode0 = streamEdge.getNode0().toString();
			String idNode1 = streamEdge.getNode1().toString();
			Node gephiNode0 = this.gephiGraph.getNode(idNode0);
			Node gephiNode1 = this.gephiGraph.getNode(idNode1);
			Edge gephiEdge = graphModel.factory().newEdge(streamEdge.toString(), gephiNode0, gephiNode1, 1, true);
			this.gephiGraph.addEdge(gephiEdge);
		}
		
	}

	
	// using gephi-toolkit 
	public void computeClosenessCentrality() {
		GraphDistance graphDistance = new GraphDistance();
		graphDistance.setDirected(false);
		graphDistance.setNormalized(true);
		graphDistance.execute(this.graphModel, this.attributeModel);
		
		AttributeColumn attributeColumn = this.attributeModel.getNodeTable().getColumn(GraphDistance.CLOSENESS);
		
		for(Node gephiNode: this.gephiGraph.getNodes()) {
			Double closeness = (Double)gephiNode.getNodeData().getAttributes().getValue(attributeColumn.getIndex());
			System.out.println(closeness);
		}
	}
}
