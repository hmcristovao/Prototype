package myClasses;

import org.gephi.data.attributes.api.AttributeTable;

public class SystemGraph {

	private StreamGraphData streamGraphData;
	private GephiGraphData gephiGraphData;
	private AttributeTable attributeTable;
	private Ranks ranks;
	private SetNodes setNodes;
	
	
	public SystemGraph(StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		this.streamGraphData = streamGraphData; 
		this.gephiGraphData  = gephiGraphData;
		this.attributeTable  = null;
		this.ranks           = new Ranks();
		this.setNodes        = new SetNodes(); 
	}

	
	
}
