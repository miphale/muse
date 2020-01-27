package com.muse.its.controller;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.muse.its.dto.Vertex;
import com.muse.its.entity.Planet;
import com.muse.its.model.ShortestPathModel;
import com.muse.its.service.DaoManagerService;
import com.muse.its.service.impl.ShortestPathService;
import com.muse.its.utils.Graph;
import com.muse.its.utils.InterstellarTransportationSystemConstants;

@Controller
public class ShortestPathController {
	private static final Logger log = LogManager.getLogger(ShortestPathController.class);
	private DaoManagerService daoManagerService;
	private ShortestPathService pathService;
	
	@Autowired
	public ShortestPathController(DaoManagerService daoManagerService, ShortestPathService pathService) {
		this.daoManagerService = daoManagerService;
		this.pathService = pathService;
	}
	
	@GetMapping(value = "/shortestpath")
	public String initForm(Model model) {
		ShortestPathModel pathModel = new ShortestPathModel();
		List<Planet> planets = daoManagerService.getAllPlanets();
		if(planets == null || planets.isEmpty()) {
			model.addAttribute("errorMessage", InterstellarTransportationSystemConstants.NO_PLANETS_FOUND);
			return "validation";
		}
		pathModel.setVertexName(planets.get(0).getPlanetName());
		model.addAttribute("pathModel", pathModel);
		model.addAttribute("planetList", planets);
		return "shortest";
	}
	
	@PostMapping(value = "/shortestpath")
	public String calculateShortestPath(@ModelAttribute ShortestPathModel pathModel, Model model) {
		Graph graph = daoManagerService.initialiseGraph();
		//if(pathModel.getTrafficAllowed() != null && pathModel.getTrafficAllowed()) {
			graph.setTrafficAllowed(pathModel.getTrafficAllowed());
		//}
		//if(pathModel.getUndirectedGrapth()) {
			graph.setUndirectedGraph(pathModel.getUndirectedGrapth());
		//}
		log.debug("Graph vertices: " + graph.getVertices().size());
		log.debug("Graph edges: " + graph.getEdges().size());
		log.debug("Graph traffic: " + graph.getTraffics().size());
		pathService.initializeGraphData(graph);
		
		log.debug("Selected Vertex Node: "+pathModel.getSelectedVertex());
		
		Planet planetOrigin = daoManagerService.getPlanetByName(pathModel.getVertexName());
		Planet planetDestination = daoManagerService.getPlanetByNode(pathModel.getSelectedVertex());
		
		if(planetOrigin == null && planetDestination == null) {
			model.addAttribute("errorMessage", InterstellarTransportationSystemConstants.INVALID_REQUEST);
			return "validation";
		}
		
		Vertex origin = new Vertex(planetOrigin.getPlanetNode(), planetOrigin.getPlanetName());
		Vertex destinantion = new Vertex(planetDestination.getPlanetNode(), planetDestination.getPlanetName());
		
		pathService.evaluateShortestPath(origin);
		LinkedList<Vertex> paths = pathService.getPath(destinantion);
		
		StringBuilder sb = new StringBuilder();
		if(paths != null) {
			for(Vertex vertex: paths) {
				sb.append(vertex.getName());
				sb.append("\t");
			}
		} else if (origin != null && destinantion != null && origin.getId().equals(destinantion.getId())) {
			sb.append(InterstellarTransportationSystemConstants.ALREADY_ON_DESTINATION_PLANET + origin.getName());
		} else {
			sb.append(InterstellarTransportationSystemConstants.PATH_NOT_AVAILABLE);
		}
		
		pathModel.setPath(sb.toString());
		pathModel.setSelectedVertexName(destinantion.getName());
		model.addAttribute("pathModel", pathModel);
		return "shortestpath";
	}
}
