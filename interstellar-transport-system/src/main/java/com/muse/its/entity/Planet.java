package com.muse.its.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "planet")
@Table
public class Planet implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column
	private String planetNode;
	@Column
	private String planetName;
	
	public Planet() {		
	}
	
	public Planet(String planetNode, String planetName) {
		this.planetNode = planetNode;
		this.planetName = planetName;
	}
	
	public String getPlanetNode() {
		return planetNode;
	}

	public void setPlanetNode(String planetNode) {
		this.planetNode = planetNode;
	}

	public String getPlanetName() {
		return planetName;
	}

	public void setPlanetName(String planetName) {
		this.planetName = planetName;
	}
}