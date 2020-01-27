package com.muse.its.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.muse.its.repository.ShortestPathRepository;
import com.muse.its.schema.GetShortestPathRequest;
import com.muse.its.schema.GetShortestPathResponse;

@Endpoint
public class ShortestPathEndPoint {
	private ShortestPathRepository shortestPathRepository;
	
	@Autowired
	public ShortestPathEndPoint(ShortestPathRepository shortestPathRepository) {
		this.shortestPathRepository = shortestPathRepository;
	}
	
	@PayloadRoot(namespace = "http://musecs.com/its/shortest-path", localPart = "getShortestPathRequest")
	@ResponsePayload
	public GetShortestPathResponse getShortestPath(@RequestPayload GetShortestPathRequest request) {
		GetShortestPathResponse response = new GetShortestPathResponse();
		response.setPath(shortestPathRepository.evaluateShortestPath(request.getDestination()));
		return response;
	}
}