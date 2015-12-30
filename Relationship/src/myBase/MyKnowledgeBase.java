package myBase;

import java.util.HashMap;
import java.util.Map;

public class MyKnowledgeBase {

	private Map<String, Links> nodeHash;
	
	public MyKnowledgeBase() {
		this.nodeHash = new HashMap<String,Links>();
	}
	
	public int size() {
		return this.nodeHash.size(); 
	}
	public void insert(String nodeSubject, String predicate, String nodeObject) {
		Link link;
		
		link = new Link(predicate, nodeObject, true);
		this.insertElement(nodeSubject, link);
		
		link = new Link(predicate, nodeSubject, false);
		this.insertElement(nodeObject, link);
		
	}
	
	public void insertElement(String nodeDesc, Link link) {
		// if node1Desc already exists
		if(this.nodeHash.containsKey(nodeDesc)) {
			Links linkList = this.nodeHash.get(nodeDesc);
			linkList.add(link);
		}
		// if node1Desc do not exist yet
		else {
			Links newLinkList = new Links();
			newLinkList.add(link);
			this.nodeHash.put(nodeDesc,  newLinkList);
		}
	}

	
	public Links get(String nodeDesc) {
		return this.nodeHash.get(nodeDesc);
	}


}
