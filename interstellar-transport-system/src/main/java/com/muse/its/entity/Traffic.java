package com.muse.its.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "traffic")
@Table
public class Traffic implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	private String routeId;
	@Column
	private String planetOrigin;
	@Column
	private String planetDestination;
	@Column
	private Double trafficDelay;
	
	public Traffic() {		
	}
	
	public Traffic(String routeId, String planetOrigin, String planetDestination, Double trafficDelay) {
		this.routeId = routeId;
		this.planetOrigin = planetOrigin;
		this.planetDestination = planetDestination;
		this.trafficDelay = trafficDelay;
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

	public Double getTrafficDelay() {
		return trafficDelay;
	}

	public void setTrafficDelay(Double trafficDelay) {
		this.trafficDelay = trafficDelay;
	}
}