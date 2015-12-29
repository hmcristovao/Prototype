package myBase;

public class Link {

	private String  linkDesc;
	private String  nodeDesc;
	private boolean isNodeSubject;
	
	public Link(String descLink, String descNode, boolean nodeSubject) {
		super();
		this.linkDesc     = descLink;
		this.nodeDesc     = descNode;
		this.isNodeSubject = nodeSubject;
	}

	public String getLinkDesc() {
		return this.linkDesc;
	}

	public void setLinkDesc(String linkDesc) {
		this.linkDesc = linkDesc;
	}

	public String getNodeDesc() {
		return this.nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	public boolean isNodeSubject() {
		return this.isNodeSubject;
	}
	
	public void setIsNodeSubject(boolean isNodeSubject) {
		this.isNodeSubject = isNodeSubject;
	}
	
	public String toString() {
		StringBuffer out = new StringBuffer();
		out.append(this.linkDesc);
		if(this.isNodeSubject)
			out.append(" -> ");
		else
			out.append(" <- ");
		out.append(this.nodeDesc);
		return out.toString();
	}
}
