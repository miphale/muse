package com.muse.its.utils;

import java.util.ArrayList;
import java.util.List;

import com.muse.its.dto.Edge;
import com.muse.its.dto.TrafficDelay;
import com.muse.its.dto.Vertex;

public class Graph {
	private List<Vertex> vertices;
	private List<Edge> edges;
	private List<TrafficDelay> traffics;
	private Boolean undirectedGraph;
	private Boolean trafficAllowed;
	
	public Graph(List<Vertex> vertices, List<Edge> edges, List<TrafficDelay> traffics) {
		this.vertices = vertices;
		this.edges = edges;
		this.traffics = traffics;
	}

	public List<Vertex> getVertices() {
		return vertices;
	}

	public void setVertices(List<Vertex> vertices) {
		this.vertices = vertices;
	}

	public List<Edge> getEdges() {
		return edges;
	}

	public void setEdges(List<Edge> edges) {
		this.edges = edges;
	}

	public List<TrafficDelay> getTraffics() {
		return traffics;
	}

	public void setTraffics(List<TrafficDelay> traffics) {
		this.traffics = traffics;
	}

	public Boolean isUndirectedGraph() {
		return undirectedGraph;
	}

	public void setUndirectedGraph(Boolean undirectedGraph) {
		this.undirectedGraph = undirectedGraph;
	}

	public Boolean isTrafficAllowed() {
		return trafficAllowed;
	}

	public void setTrafficAllowed(Boolean trafficAllowed) {
		this.trafficAllowed = trafficAllowed;
	}
	
	public void processTraffics() {
		if(traffics != null && !traffics.isEmpty()) {
			for (TrafficDelay traffic: traffics) {
				for (Edge edge: edges) {
					// Check if Edge and Traffic data belong to the same route
					if(checkObjectEquals(edge.getId(), traffic.getRouteId())) {
						// Check source and destination are similar for edge and traffic data
						if(checkObjectEquals(edge.getOrigin(), traffic.getOrigin()) && checkObjectEquals(edge.getDestination(), traffic.getDestination())) {
							edge.setDelay(traffic.getDelay());
						}
					}
				}
			}
		}
	}
	
	public List<Edge> getUndirectedEdges() {
		List<Edge> undirectedEdges = new ArrayList<Edge>();
		for (Edge originEdge: edges) {
			Edge destinationEdge = copyAdjacentEdge(originEdge);
			undirectedEdges.add(originEdge);
			undirectedEdges.add(destinationEdge);
		}
		return undirectedEdges;
	}
	
	public Edge copyAdjacentEdge(Edge originEdge) {
		Edge destinationEdge = new Edge();
		destinationEdge.setId(originEdge.getId());
		destinationEdge.setOrigin(originEdge.getOrigin());
		destinationEdge.setDestination(originEdge.getDestination());
		destinationEdge.setDistance(originEdge.getDistance());
		destinationEdge.setDelay(originEdge.getDelay());
		return destinationEdge;
	}
	
	public boolean checkObjectEquals(Object object, Object otherObject) {
		if(object == null && otherObject == null) {
			return true;
		} else if (object == null || otherObject == null) {
			return false;
		} else if (object instanceof String && otherObject instanceof String) {
			return ((String) object).equalsIgnoreCase((String) otherObject);
		} else {
			return object.equals(otherObject);
		}
	}
}
