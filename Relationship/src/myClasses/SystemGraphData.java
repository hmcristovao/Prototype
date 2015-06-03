package myClasses;

public class SystemGraphData {

	private StreamGraphData streamGraphData;
	private GephiGraphData gephiGraphData;
	//private arquivo GEXF ...
	private NodesTable nodesTable;
	private Ranks ranks;
	
	public SystemGraphData() {
		this(new StreamGraphData(), new GephiGraphData());
	}

	public SystemGraphData(StreamGraphData streamGraphData, GephiGraphData gephiGraphData) {
		this.streamGraphData = streamGraphData; 
		this.gephiGraphData  = gephiGraphData;
	}

	public StreamGraphData getStreamGraphData() {
		return this.streamGraphData;
	}
	public void setStreamGraphData(StreamGraphData streamGraphData) {
		this.streamGraphData = streamGraphData;
	}
	public GephiGraphData getGephiGraphData() {
		return this.gephiGraphData;
	}
	public void setGephiGraphData(GephiGraphData gephiGraphData) {
		this.gephiGraphData = gephiGraphData;
	}
	public Ranks getRanks() {
		return this.ranks;
	}
	public void setRanks(Ranks ranks) {
		this.ranks = ranks;
	}

	public void buildSystemGraphData(SetQuerySparql setQuerySparql) {
		Debug.err("Building Stream Graph Data...");
		this.streamGraphData.buildStreamGraphData(setQuerySparql);
		Debug.err("Building Gephi Graph Data...");
		this.gephiGraphData.buildGephiGraphData(this.getStreamGraphData());
		Debug.err("Building Nodes Table graph...");
		this.nodesTable.buildNodesTable();
	}
	
	public void buildRanks() {
		
	}
	
	public void analyseGraphData() {
		
	}
	
	public String toString() {
		return  this.getStreamGraphData().toString() +
				this.getGephiGraphData().toString() +
				this.getRanks().toString();
	}


}
