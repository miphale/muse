package com.muse.its.model;

import java.io.Serializable;

public class ShortestPathModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String selectedVertex;
	private String selectedVertexName;
	private String vertexId;
	private String vertexName;
	private String path;
	private String originVertex;
	private String destinationVertex;
	private Boolean undirectedGrapth;
	private Boolean trafficAllowed;
	
	public ShortestPathModel() {		
	}

	public String getSelectedVertex() {
		return selectedVertex;
	}

	public void setSelectedVertex(String selectedVertex) {
		this.selectedVertex = selectedVertex;
	}

	public String getSelectedVertexName() {
		return selectedVertexName;
	}

	public void setSelectedVertexName(String selectedVertexName) {
		this.selectedVertexName = selectedVertexName;
	}

	public String getVertexId() {
		return vertexId;
	}

	public void setVertexId(String vertexId) {
		this.vertexId = vertexId;
	}

	public String getVertexName() {
		return vertexName;
	}

	public void setVertexName(String vertexName) {
		this.vertexName = vertexName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOriginVertex() {
		return originVertex;
	}

	public void setOriginVertex(String originVertex) {
		this.originVertex = originVertex;
	}

	public String getDestinationVertex() {
		return destinationVertex;
	}

	public void setDestinationVertex(String destinationVertex) {
		this.destinationVertex = destinationVertex;
	}

	public Boolean getUndirectedGrapth() {
		return undirectedGrapth;
	}

	public void setUndirectedGrapth(Boolean undirectedGrapth) {
		this.undirectedGrapth = undirectedGrapth;
	}

	public Boolean getTrafficAllowed() {
		return trafficAllowed;
	}

	public void setTrafficAllowed(Boolean trafficAllowed) {
		this.trafficAllowed = trafficAllowed;
	}
}