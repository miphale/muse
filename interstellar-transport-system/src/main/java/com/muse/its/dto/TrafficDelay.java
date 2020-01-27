package com.muse.its.dto;

public class TrafficDelay {
	private String routeId;
	private Vertex origin;
	private Vertex destination;
	private Double delay;
	
	public TrafficDelay() {		
	}
	
	public TrafficDelay(String routeId, Vertex origin, Vertex destination, Double delay) {
		this.routeId = routeId;
		this.origin = origin;
		this.destination = destination;
		this.delay = delay;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
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

	public Double getDelay() {
		return delay;
	}

	public void setDelay(Double delay) {
		this.delay = delay;
	}
}