package user;

import graph.NodeData;

import java.util.ArrayList;
import java.util.HashMap;

import main.Config;
import main.Log;

public class GroupConcept {
	private ArrayList<Concept> list;
	private HashMap<String, Concept> hash;
	
	public GroupConcept() {
		this.list = new ArrayList<Concept>();
		this.hash = new HashMap<String, Concept>(); 
	}

	public ArrayList<Concept> getList() {
		return this.list;
	}
	
	public boolean add(Concept concept) {
		// if there is not repeted key (concept), add in ArrayList too 
		if(!this.hash.containsKey(concept.getBlankName())) {
			this.hash.put(concept.getBlankName(), concept);
			this.list.add(concept);
			// if is concept "Category", add pure concept too (if is also the configuration true)
			if(Config.additionNewConceptWithoutCategory && concept.getCategory() == Config.Category.yes) {
				// create new concept without "Category"
				Concept newConceptWithoutCategory = new Concept(Concept.extractCategoryFullName(concept.getFullName()),
						                                        Concept.extractCategory(concept.getBlankName()), 
						                                        concept.getStatus(), 
						                                        concept.getIteration(),
						                                        Config.Category.was,
						                                        0, // still is not possible to know the quantity RDFs
						                                        concept.getConnectedComponent(concept.getIteration()));
				this.hash.put(newConceptWithoutCategory.getBlankName(), newConceptWithoutCategory);
				this.list.add(newConceptWithoutCategory);				
			}
			return true;
		}
		return false;
	}

	// CALCULATE QUANTITIES ========================================================================
	
	public int size() {
		return this.list.size();
	}
	
	// obsolete because there is absolut consult by WholeSystem.getQuantityOriginalConcepts()
	/*
	public int getQuantityOriginalConcept() {
		int count = 0;
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.originalConcept) {
				count++;
			}
		}		
		return count;
	} */

	// GET GROUP CONCEPTS ========================================================================
		
	public GroupConcept getCurrentConcepts(int iteration) {  // original and selected concepts of all previous iterations (except the current iteration) 
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if( concept.getStatus() == Config.Status.originalConcept)
			   result.add(concept);
			else if(iteration > 0) {
				// get all concepts in each previous iterations
				for(int i=0; i<iteration; i++) {
					if( ( concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept || concept.getStatus() == Config.Status.selectedEigenvectorConcept ) &&
						  concept.getIteration() == i ) {
						result.add(concept);
					}
				}
			}
		}		
		return result;
	}
	public GroupConcept getOriginalConcepts() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.originalConcept) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedBetweennessClosenessConcepts() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedEigenvectorConcepts() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedEigenvectorConcept) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedBetweennessClosenessConcepts(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept &&
			   concept.getIteration() == iteration) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedEigenvectorConcepts(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getStatus() == Config.Status.selectedEigenvectorConcept &&
			   concept.getIteration() == iteration) {
				result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getSelectedBetweennessClosenessConcepts(int iteration, int connectedComponent) {
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
	public GroupConcept getSelectedEigenvectorConcepts(int iteration, int connectedComponent) {
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
	public GroupConcept getSelectedConcepts(int iteration, int connectedComponent) {
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
	public GroupConcept getSelectedConcepts(int iteration) {
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
	public GroupConcept getSelectedConcepts() {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if( concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept ||
				concept.getStatus() == Config.Status.selectedEigenvectorConcept
			   ) {
			   result.add(concept);
			}
		}		
		return result;
	}
	public GroupConcept getConcepts(int iteration) {
		GroupConcept result = new GroupConcept();
		for(Concept concept : this.list) {
			if(concept.getIteration() == iteration) {
				result.add(concept);
			}
		}		
		return result;
	}
	

	// IS... ========================================================================

	public boolean isOriginalConcept(String blankName) {
		Concept concept = this.hash.get(blankName);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.originalConcept;
	}
	public boolean isSelectedBetweennessClosenessConcept(String blankName) {
		Concept concept = this.hash.get(blankName);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept;
	}
	public boolean isSelectedEigenvectorConcept(String blankName) {
		Concept concept = this.hash.get(blankName);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.selectedEigenvectorConcept;
	}
	public boolean isSelectedConcept(String blankName) {
		Concept concept = this.hash.get(blankName);
		if(concept == null)
			return false;
		return concept.getStatus() == Config.Status.selectedBetweennessClosenessConcept ||
			   concept.getStatus() == Config.Status.selectedEigenvectorConcept;
	}
	public boolean isConcept(String blankName) {
		return this.hash.containsKey(blankName);
	}


	// GET ELEMENTS AND INFORMATION ========================================================================
	
	public Concept getConcept(String blankName) {
		return this.hash.get(blankName);
	}
	public int getConnectedComponent(String blankName, int iteration) {
		Concept concept = this.hash.get(blankName);
		if(concept == null)
			return -1;
		return concept.getConnectedComponent(iteration);
	}
	public Config.Status getStatus(String blankName) {
		Concept concept = this.hash.get(blankName);
		if(concept == null)
			return Config.Status.noStatus;
		return concept.getStatus();
	}
	public Concept getConcept(int pos) {
		return this.list.get(pos);
	}
	

	// GET ELEMENTS AND INFORMATION ========================================================================
	
	public boolean removeConcept(String blankName) {
		if(!this.hash.containsKey(blankName))
			return false;
		Concept concept = this.hash.get(blankName);
		this.list.remove(concept);
		this.hash.remove(blankName);
		return true;
	}
	
	// toString's() ========================================================================
	
	public String toStringLong() {
		StringBuffer str = new StringBuffer();
		if(this.list != null)
    		for(Concept x : this.list) {
	    		str.append("- ");
    			str.append(x.toStringLong());
	    		str.append("\n");
		    }
		return str.toString();
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
