package myClasses;

import org.gephi.data.attributes.api.AttributeColumn;
import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.Node;
import org.gephi.project.api.ProjectController;
//import org.gephi.project.api.Workspace;
import org.gephi.statistics.plugin.GraphDistance;
import org.openide.util.Lookup;

public class GephiGraphData {

	private GraphModel graphModel;
	private AttributeModel attributeModel;
	private AttributeTable attributeTable;
	private Graph gephiGraph;
	
	public GephiGraphData() {
		ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
		projectController.newProject();
		//Workspace workspace = projectController.getCurrentWorkspace();
		this.graphModel =  Lookup.getDefault().lookup(GraphController.class).getModel();
		this.attributeModel = Lookup.getDefault().lookup(AttributeController.class).getModel();
		this.attributeTable  = null;
		this.gephiGraph = this.graphModel.getGraph();
	}
	
	public GraphModel getGraphModel() {
		return this.graphModel;
	}
	public AttributeModel getAttributeModel() {
		return this.attributeModel;
	}
	public AttributeTable getAttributeTable() {
		return this.attributeTable;
	}
	public Graph getGephiGraph() {
		return this.gephiGraph;
	}
	public void setAttributeTable(AttributeTable attributeTable) {
		this.attributeTable = attributeTable;
	}

	public void buildGephiGraphTable() {
		GraphDistance graphDistance = new GraphDistance();
		graphDistance.setDirected(false);
		graphDistance.setNormalized(true);
		graphDistance.execute(this.graphModel, this.attributeModel);
		this.attributeTable = this.attributeModel.getNodeTable();
		//somente para extrair única coluna:
		//AttributeColumn attributeColumn = attributeModel.getNodeTable().getColumn(GraphDistance.CLOSENESS);
	}
	
	
	// Don't used
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
