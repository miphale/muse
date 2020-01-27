package com.muse.its.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "route")
@Table
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	private String routeId;
	@Column
	private String planetOrigin;
	@Column
	private String planetDestination;
	@Column
	private Double distance;
	
	public Route() {		
	}
	
	public Route(String routeId, String planetOrigin, String planetDestination, Double distance) {
		this.routeId = routeId;
		this.planetOrigin = planetOrigin;
		this.planetDestination = planetDestination;
		this.distance = distance;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getPlanetOrigin() {
		return planetOrigin;
	}

	public void setPlanetOrigin(String planetOrigin) {
		this.planetOrigin = planetOrigin;
	}

	public String getPlanetDestination() {
		return planetDestination;
	}

	public void setPlanetDestination(String planetDestination) {
		this.planetDestination = planetDestination;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}
}