package com.muse.its.repository;

import java.util.LinkedList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.muse.its.dto.Vertex;
import com.muse.its.entity.Planet;
import com.muse.its.service.DaoManagerService;
import com.muse.its.service.impl.ShortestPathService;
import com.muse.its.utils.Graph;
import com.muse.its.utils.InterstellarTransportationSystemConstants;

@Component
public class ShortestPathRepository {
	protected PlatformTransactionManager transactionManager;
	private Graph graph;
	private DaoManagerService daoManagerService;
	
	@Autowired
	public ShortestPathRepository(@Qualifier("transactionManager")PlatformTransactionManager transactionManager, DaoManagerService daoManagerService) {
		this.transactionManager = transactionManager;
		this.daoManagerService = daoManagerService;
	}
	
	@PostConstruct
	public void loadData() {
		TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
		transactionTemplate.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				daoManagerService.persistAllData();
			}
		});
	}
	
	public String evaluateShortestPath(String name) {
		graph = daoManagerService.initialiseGraph();
		if(graph == null || graph.getVertices() == null || graph.getVertices().isEmpty()) {
			return InterstellarTransportationSystemConstants.NO_PLANETS_FOUND;
		}
		
		Planet destinationPlanet = daoManagerService.getPlanetByName(name);
		if(destinationPlanet == null) {
			// Find destination planet by node
			destinationPlanet = daoManagerService.getPlanetByNode(name);
			if(destinationPlanet == null) {
				return name + InterstellarTransportationSystemConstants.PLANET_DOES_NOT_EXIST;
			}
		}
		
		ShortestPathService shortestPathService = new ShortestPathService(graph);
		Vertex origin = graph.getVertices().get(0);
		Vertex destination = new Vertex(destinationPlanet.getPlanetNode(), destinationPlanet.getPlanetName());
		
		if(origin != null && destination != null && origin.getId().equals(destination.getId())) {
			return InterstellarTransportationSystemConstants.ALREADY_ON_DESTINATION_PLANET + destination.getName();
		}
		
		shortestPathService.evaluateShortestPath(origin);
		LinkedList<Vertex> paths = shortestPathService.getPath(destination);
		
		StringBuilder path = new StringBuilder();
		if(paths != null) {
			for(Vertex vertex: paths) {
				path.append(vertex.getName());
				path.append("\t");
			}
		} else if (origin != null && destination != null && origin.getId().equals(destination.getId())) {
			path.append(InterstellarTransportationSystemConstants.ALREADY_ON_DESTINATION_PLANET + origin.getName());
		} else {
			path.append(InterstellarTransportationSystemConstants.PATH_NOT_AVAILABLE);
		}
		return path.toString();
	}
}
