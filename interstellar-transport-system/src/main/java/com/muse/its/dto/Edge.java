package com.muse.its.dto;

public class Edge {
	private String id;
	private Vertex origin;
	private Vertex destination;
	private Double distance;
	private Double delay;
	
	public Edge() {		
	}
	
	public Edge(String id, Vertex origin, Vertex destination, Double distance) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
	}
	
	public Edge(String id, Vertex origin, Vertex destination, Double distance, Double delay) {
		this.id = id;
		this.origin = origin;
		this.destination = destination;
		this.distance = distance;
		this.delay = delay;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Vertex getOrigin() {
		return origin;
	}

	public void setOrigin(Vertex origin) {
		this.origin = origin;
	}

	public Vertex getDestination() {
		return destination;
	}

	public void setDestination(Vertex destination) {
		this.destination = destination;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Double getDelay() {
		return delay;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}
}
