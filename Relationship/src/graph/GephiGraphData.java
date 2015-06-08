package graph;

import org.gephi.data.attributes.api.AttributeController;
import org.gephi.data.attributes.api.AttributeModel;
import org.gephi.data.attributes.api.AttributeTable;
import org.gephi.graph.api.Graph;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.project.api.ProjectController;
import org.gephi.statistics.plugin.EigenvectorCentrality;
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

	// this metodo calculate the table with all measures: betweenness, closeness and eigenvector
	public void buildGephiGraphTable() {
		GraphDistance graphDistance = new GraphDistance();
		graphDistance.setDirected(false);
		graphDistance.setNormalized(true);
		graphDistance.execute(this.graphModel, this.attributeModel);
		
		EigenvectorCentrality ec = new EigenvectorCentrality();
		ec.execute(this.graphModel, this.attributeModel);

		this.attributeTable = this.attributeModel.getNodeTable();
	}
	
	public void filterDegree(int degree) {
		// olhar código em:
		// https://github.com/gephi/gephi/wiki/How-to-use-filters
				
	}

}
