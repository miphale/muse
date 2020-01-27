package com.muse.its.dto;

public class Vertex {
	private final String id;
	private final String name;
	
	public Vertex(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + ((id == null) ? 0 : id.hashCode());
	}
	
	@Override
	public boolean equals(Object object) {
		if(this == object) {
			return true;
		}
		if(object == null) {
			return false;
		}
		if(getClass() != object.getClass()) {
			return false;
		}
		
		Vertex other = (Vertex) object;
		if(id == null) {
			if(other.id != null) {
				return false;
			}
		} else if (!id.contentEquals(other.id)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return name;
	}
}