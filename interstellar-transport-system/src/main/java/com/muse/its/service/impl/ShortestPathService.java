package com.muse.its.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.muse.its.dto.Edge;
import com.muse.its.dto.Vertex;
import com.muse.its.utils.Graph;

@Service
public class ShortestPathService {
	private List<Vertex> vertices;
	private List<Edge> edges;
	private Set<Vertex> visitedVertices;
	private Set<Vertex> unvisitedVertices;
	private Map<Vertex, Vertex> previousPaths;
	private Map<Vertex, Double> distance;
	
	public ShortestPathService() {		
	}
	
	public ShortestPathService(Graph graph) {
		this.vertices = graph.getVertices();
		if (graph.isTrafficAllowed()) {
			graph.processTraffics();
		}
		if(graph.isUndirectedGraph()) {
			this.edges = graph.getUndirectedEdges();
		} else {
			this.edges = graph.getEdges();
		}
	}
	
	public void initializeGraphData(Graph graph) {
		this.vertices = new ArrayList<Vertex>(graph.getVertices());
		if (graph.isTrafficAllowed()) {
			graph.processTraffics();
		}
		if(graph.isUndirectedGraph()) {
			this.edges = graph.getUndirectedEdges();
		} else {
			this.edges = graph.getEdges();
		}
	}
	
	public void evaluateShortestPath(Vertex origin) {
		distance = new HashMap<>();
		previousPaths = new HashMap<>();
		visitedVertices = new HashSet<>();
		unvisitedVertices = new HashSet<>();
		distance.put(origin, new Double(0));
		unvisitedVertices.add(origin);
		
		while(unvisitedVertices.size() > 0) {
			Vertex currentVertex = getClosestVertex(unvisitedVertices);
			visitedVertices.add(currentVertex);
			unvisitedVertices.remove(currentVertex);
			evaluateNeighboursWithMinimalDistances(currentVertex);
		}
	}
	
	private Vertex getClosestVertex(Set<Vertex> vertices) {
		Vertex closestVertex = null;
		for(Vertex vertex: vertices) {
			if(closestVertex == null) {
				closestVertex = vertex;
			} else if (getShortesDistance(vertex) < getShortesDistance(closestVertex)) {
				closestVertex = vertex;
			}
		}
		return closestVertex;
	}
	
	private Double getShortesDistance(Vertex destination) {
		Double d = distance.get(destination);
		if(d == null) {
			return Double.POSITIVE_INFINITY;
		} else {
			return d;
		}
	}
	
	private void evaluateNeighboursWithMinimalDistances(Vertex currentVertex) {
		List<Vertex> neighbouringVertices = getNeighbouringVertices(currentVertex);
		for(Vertex target: neighbouringVertices) {
			Double alternateDistance = getShortesDistance(currentVertex) + getDistance(currentVertex, target);
			if(alternateDistance < getShortesDistance(target)) {
				
				distance.put(target, alternateDistance);
				previousPaths.put(target, currentVertex);
				unvisitedVertices.add(target);
			}
		}
	}
	
	private List<Vertex> getNeighbouringVertices(Vertex currentVertex) {
		List<Vertex> neighbours = new ArrayList<>();
		for(Edge edge: edges) {
			if(edge.getDestination() != null ) {
				Vertex destination = fromId(edge.getDestination().getId());
				if(edge.getOrigin().getId().equals(currentVertex.getId()) && !isVisted(destination)) {
					neighbours.add(destination);
				}
			}
		}
		return neighbours;
	}
	
	private Vertex fromId(String destinationId) {
		for (Vertex vertex: vertices) {
			if(vertex.getId().equalsIgnoreCase(destinationId)) {
				return vertex;
			}
		}
		
		return new Vertex(destinationId, "Isolated " + destinationId);		
	}
	
	private Boolean isVisted(Vertex vertex) {
		return visitedVertices.contains(vertex);
	}
	
	private Double getDistance(Vertex origin, Vertex target) {
		for (Edge edge: edges) {
			if(edge.getOrigin().getId().equals(origin.getId()) && edge.getDestination().getId().equals(target.getId())) {
				return edge.getDistance()!=null?edge.getDistance():new Double(0) + (edge.getDelay()!=null?edge.getDelay():new Double(0));
			}
		}
		throw new RuntimeException("Something went wrong");
	}
	
	public LinkedList<Vertex> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<>();
		Vertex step = target;
		
		if(previousPaths.get(step) == null) {
			return null;
		}
		
		path.add(step);
		while (previousPaths.get(step) != null) {
			step = previousPaths.get(step);
			path.add(step);
		}
		
		Collections.reverse(path);
		return path;
	}
}