package user;

import graph.NodeData;

import java.util.ArrayList;
import java.util.HashMap;

import main.Config;

public class GroupConcept {
	private ArrayList<Concept> list;
	private HashMap<String, Concept> hash;
	
	public GroupConcept() {
		this.list = new ArrayList<Concept>();
		this.hash = new HashMap<String, Concept>(); 
	}

	public int size() {
		return this.list.size();
	}
	
	public void add(Concept concept) {
		this.list.add(concept);
		this.hash.put(concept.toStringShort(), concept);
	}
	
	public GroupConcept getOriginalNodes() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.originalConcept) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedBetweennessClosenessNodes() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedEigenvectorNodes() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedEigenvectorConcept) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedBetweennessClosenessNodes(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept &&
			   concept.getIteration() == iteration) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedEigenvectorNodes(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedEigenvectorConcept &&
			   concept.getIteration() == iteration) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedBetweennessClosenessNodes(int iteration, int connectedComponent) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept &&
			   concept.getIteration() == iteration &&
			   concept.getConnectedComponent(iteration) == connectedComponent
			  ) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedEigenvector(int iteration, int connectedComponent) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedEigenvectorConcept &&
			   concept.getIteration() == iteration &&
			   concept.getConnectedComponent(iteration) == connectedComponent
			  ) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedNodes(int iteration, int connectedComponent) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if( ( concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept ||
				  concept.getStatus() == Config.Status.selectedEigenvectorConcept
				) &&
			    concept.getIteration() == iteration &&
			    concept.getConnectedComponent(iteration) == connectedComponent
			  ) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedNodes(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if( ( concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept ||
				  concept.getStatus() == Config.Status.selectedEigenvectorConcept
				) &&
				concept.getIteration() == iteration
				) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getNodes(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getIteration() == iteration) {
				result.add(concept);
			}
		}		
		return result;
	}
	
	public boolean isOriginalConcept(String basicConcept) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.originalConcept;
	}
	public boolean isSelectedBetweennessClosenessConcept(String basicConcept) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept;
	}
	public boolean isSelectedEigenvectorConcept(String basicConcept) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.selectedEigenvectorConcept;
	}
	public boolean isConcept(String basicConcept) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return false;
		return concept != null;
	}

	public Concept getConcept(String basicConcept) {
		return this.hash.get(basicConcept);
	}
	public NodeData getNodeData(String basicConcept, int iteration) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return null;
		return concept.getNodeData(iteration);
	}
	public int getConnectedComponent(String basicConcept, int iteration) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return -1;
		return concept.getConnectedComponent(iteration);
	}
	public Config.Status getStatus(String basicConcept) {
		Concept concept = this.hash.get(basicConcept);
		if(concept == null)
			return Config.Status.noStatus;
		return concept.getStatus();
	}
	
	
	
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();
		if(this.list != null)
    		for(Concept x : this.list) {
	    		str.append("- ");
    			str.append(x.toStringShort());
	    		str.append("\n");
		    }
		return str.toString();
	}
}
