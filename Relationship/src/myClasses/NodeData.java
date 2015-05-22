package myClasses;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

public class NodeData implements Node {
	private double betweenness;
	private double closeness;
	private int connectedComponent;
	private double eingenvector;

	public double getBetweenness() {
		return this.betweenness;
	}
	public void setBetweenness(double betweenness) {
		this.betweenness = betweenness;
	}
	public double getCloseness() {
		return this.closeness;
	}
	public void setCloseness(double closeness) {
		this.closeness = closeness;
	}
	public int getConnectedComponent() {
		return this.connectedComponent;
	}
	public void setConnectedComponent(int connectedComponent) {
		this.connectedComponent = connectedComponent;
	}
	public double getEingenvector() {
		return this.eingenvector;
	}
	public void setEingenvector(double eingenvector) {
		this.eingenvector = eingenvector;
	}
	public NodeData() {
		super();
	}
	@Override
	public void addAttribute(String arg0, Object... arg1) {
		this.addAttribute(arg0, arg1);		
	}
	@Override
	public void addAttributes(Map<String, Object> arg0) {
		this.addAttributes(arg0);
	}
	@Override
	public void changeAttribute(String arg0, Object... arg1) {
		this.changeAttribute(arg0, arg1);
	}
	@Override
	public void clearAttributes() {
		this.clearAttributes();
	}
	@Override
	public Object[] getArray(String arg0) {
		return this.getArray(arg0);
	}
	@Override
	public <T> T getAttribute(String arg0) {
		return this.getAttribute(arg0);
	}
	@Override
	public <T> T getAttribute(String arg0, Class<T> arg1) {
		return this.getAttribute(arg0, arg1);
	}
	@Override
	public int getAttributeCount() {
		return this.getAttributeCount();
	}
	@Override
	public Iterator<String> getAttributeKeyIterator() {
		return this.getAttributeKeyIterator();
	}
	@Override
	public Collection<String> getAttributeKeySet() {
		return this.getAttributeKeySet();
	}
	@Override
	public Iterable<String> getEachAttributeKey() {
		return this.getEachAttributeKey();
	}
	@Override
	public <T> T getFirstAttributeOf(String... arg0) {
		return this.getFirstAttributeOf(arg0);
	}
	@Override
	public <T> T getFirstAttributeOf(Class<T> arg0, String... arg1) {
		return this.getFirstAttributeOf(arg0, arg1);
	}
	@Override
	public HashMap<?, ?> getHash(String arg0) {
		return this.getHash(arg0);
	}
	@Override
	public String getId() {
		return this.getId();
	}
	@Override
	public int getIndex() {
		return this.getIndex();
	}
	@Override
	public CharSequence getLabel(String arg0) {
		return this.getLabel(arg0);
	}
	@Override
	public double getNumber(String arg0) {
		return this.getNumber(arg0);
	}
	@Override
	public ArrayList<? extends Number> getVector(String arg0) {
		return this.getVector(arg0);
	}
	@Override
	public boolean hasArray(String arg0) {
		return this.hasArray(arg0);
	}
	@Override
	public boolean hasAttribute(String arg0) {
		return this.hasAttribute(arg0);
	}
	@Override
	public boolean hasAttribute(String arg0, Class<?> arg1) {
		return this.hasAttribute(arg0, arg1);
	}
	@Override
	public boolean hasHash(String arg0) {
		return this.hasHash(arg0);
	}
	@Override
	public boolean hasLabel(String arg0) {
		return false;
	}
	@Override
	public boolean hasNumber(String arg0) {
		return false;
	}
	@Override
	public boolean hasVector(String arg0) {
		return false;
	}
	@Override
	public void removeAttribute(String arg0) {

	}
	@Override
	public void setAttribute(String arg0, Object... arg1) {
	}
	@Override
	public Iterator<Edge> iterator() {
		return null;
	}
	@Override
	public <T extends Node> Iterator<T> getBreadthFirstIterator(boolean arg0) {
		return null;
	}
	@Override
	public int getDegree() {
		return 0;
	}
	@Override
	public <T extends Node> Iterator<T> getDepthFirstIterator() {
		return null;
	}
	@Override
	public <T extends Node> Iterator<T> getDepthFirstIterator(boolean arg0) {
		return null;
	}
	@Override
	public <T extends Edge> Iterable<T> getEachEdge() {
		return null;
	}
	@Override
	public <T extends Edge> Iterable<T> getEachEnteringEdge() {
		return null;
	}
	@Override
	public <T extends Edge> Iterable<T> getEachLeavingEdge() {
		return null;
	}
	@Override
	public <T extends Edge> T getEdge(int arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeBetween(String arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeBetween(Node arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeBetween(int arg0) throws IndexOutOfBoundsException {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeFrom(String arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeFrom(Node arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeFrom(int arg0) throws IndexOutOfBoundsException {
		return null;
	}
	@Override
	public <T extends Edge> Iterator<T> getEdgeIterator() {
		return null;
	}
	@Override
	public <T extends Edge> Collection<T> getEdgeSet() {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeToward(String arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeToward(Node arg0) {
		return null;
	}
	@Override
	public <T extends Edge> T getEdgeToward(int arg0) throws IndexOutOfBoundsException {
		return null;
	}
	@Override
	public <T extends Edge> T getEnteringEdge(int arg0) {
		return null;
	}
	@Override
	public <T extends Edge> Iterator<T> getEnteringEdgeIterator() {
		return null;
	}
	@Override
	public <T extends Edge> Collection<T> getEnteringEdgeSet() {
		return null;
	}
	@Override
	public Graph getGraph() {
		return null;
	}
	@Override
	public int getInDegree() {
		return 0;
	}
	@Override
	public <T extends Edge> T getLeavingEdge(int arg0) {
		return null;
	}
	@Override
	public <T extends Edge> Iterator<T> getLeavingEdgeIterator() {
		return null;
	}
	@Override
	public <T extends Edge> Collection<T> getLeavingEdgeSet() {
		return null;
	}
	@Override
	public <T extends Node> Iterator<T> getNeighborNodeIterator() {
		return null;
	}
	@Override
	public <T extends Node> Iterator<T> getBreadthFirstIterator() {
		return null;
	}
	@Override
	public int getOutDegree() {
		return 0;
	}
	@Override
	public boolean hasEdgeBetween(String arg0) {
		return false;
	}
	@Override
	public boolean hasEdgeBetween(Node arg0) {
		return false;
	}
	@Override
	public boolean hasEdgeBetween(int arg0) throws IndexOutOfBoundsException {
		return false;
	}
	@Override
	public boolean hasEdgeFrom(String arg0) {
		return false;
	}
	@Override
	public boolean hasEdgeFrom(Node arg0) {
		return false;
	}
	@Override
	public boolean hasEdgeFrom(int arg0) throws IndexOutOfBoundsException {
		return false;
	}
	@Override
	public boolean hasEdgeToward(String arg0) {
		return this.hasEdgeToward(arg0);
	}
	@Override
	public boolean hasEdgeToward(Node arg0) {
		return this.equals(arg0);
	}
	@Override
	public boolean hasEdgeToward(int arg0) throws IndexOutOfBoundsException {
		return false;
	}
	public String toString() {
		return "";
	}
}
