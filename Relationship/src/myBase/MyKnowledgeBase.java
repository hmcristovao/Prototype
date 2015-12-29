package myBase;

import java.util.HashMap;
import java.util.Map;

public class MyKnowledgeBase {

	Map<String, Links> nodeHash;
	
	public MyKnowledgeBase() {
		this.nodeHash = new HashMap<String,Links>();
	}
	
	public void insert(String node1Desc, String linkDesc, String node2Desc, boolean isNode1Subject) {
		Link link = new Link(linkDesc, node2Desc, isNode1Subject);
		// if node1Desc already exists
		if(this.nodeHash.containsKey(node1Desc)) {
			Links linkList = this.nodeHash.get(node1Desc);
			linkList.add(link);
		}
		// if node1Desc do not exist yet
		else {
			Links newLinkList = new Links();
			newLinkList.add(link);
			this.nodeHash.put(node1Desc,  newLinkList);
		}
	}
	
	public Links get(String nodeDesc) {
		return this.nodeHash.get(nodeDesc);
	}


}
