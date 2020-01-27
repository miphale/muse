package com.muse.its.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.muse.its.entity.Planet;
import com.muse.its.entity.Traffic;
import com.muse.its.model.ShortestPathModel;
import com.muse.its.service.DaoManagerService;
import com.muse.its.utils.InterstellarTransportationSystemConstants;

@Controller
public class TrafficController {
	private DaoManagerService daoManagerService;
	
	@Autowired
	public TrafficController(DaoManagerService daoManagerService) {
		this.daoManagerService = daoManagerService;
	}
	
	@GetMapping(value = "/traffics")
	public String listTraffics(Model model) {
		List<Traffic> traffics = daoManagerService.getAllTraffics();
		model.addAttribute("traffics", traffics);
		return "traffics";
	}
	
	@GetMapping(value = "/traffic/{routeId}")
	public String showTraffic(@PathVariable String routeId, Model model) {
		model.addAttribute("traffic", daoManagerService.getTrafficById(routeId));
		return "showtraffic";
	}
	
	@GetMapping(value = "/traffic/delete/{routeId}")
	public String deleteTraffic(@PathVariable String routeId) {
		daoManagerService.deleteTraffic(routeId);
		return "redirect:/traffics";
	}
	
	@GetMapping(value = "/traffic/new")
	public String addTraffic(Model model) {
		ShortestPathModel pathModel = new ShortestPathModel();
		List<Planet> planets = daoManagerService.getAllPlanets();
		model.addAttribute("traffic", new Traffic());
		model.addAttribute("trafficModel", pathModel);
		model.addAttribute("planetList", planets);
		return "addtraffic";
	}
	
	@PostMapping(value = "/traffic")
	public String saveTraffic(Traffic traffic, @ModelAttribute ShortestPathModel pathModel, Model model) {
		Long routeId = daoManagerService.getTrafficMaxRecordId() + 1;
		traffic.setRouteId(String.valueOf(routeId));
		traffic.setPlanetOrigin(pathModel.getOriginVertex());
		traffic.setPlanetDestination(pathModel.getDestinationVertex());
		if(pathModel.getOriginVertex().equals(pathModel.getDestinationVertex())) {
			model.addAttribute("errorMessage", "The traffic from " + pathModel.getOriginVertex() + " to " + pathModel.getDestinationVertex() + " already exists");
			return "validation";
		}
		if(daoManagerService.trafficExists(traffic)) {
			model.addAttribute("errorMessage", InterstellarTransportationSystemConstants.DUPLICATE_TRAFFIC);
			return "validation";
		}
		daoManagerService.saveTraffic(traffic);
		return "redirect:/traffic/" + traffic.getRouteId();
	}
	
	@GetMapping(value = "/traffic/edit/{routeId}")
	public String editTraffic(@PathVariable String routeId, Model model) {
		ShortestPathModel pathModel = new ShortestPathModel();
		List<Planet> planets = daoManagerService.getAllPlanets();
		Traffic traffic = daoManagerService.getTrafficById(routeId);
		pathModel.setOriginVertex(traffic.getPlanetOrigin());
		pathModel.setDestinationVertex(traffic.getPlanetDestination());
		model.addAttribute("traffic", traffic);
		model.addAttribute("trafficModel", pathModel);
		model.addAttribute("planetList", planets);
		return "updatetraffic";
	}
	
	@PostMapping(value = "/traffic/update")
	public String updateTraffic(Traffic traffic, @ModelAttribute ShortestPathModel pathModel, Model model) {
		traffic.setPlanetOrigin(pathModel.getOriginVertex());
		traffic.setPlanetDestination(pathModel.getDestinationVertex());
		if(pathModel.getOriginVertex().equals(pathModel.getDestinationVertex())) {
			model.addAttribute("errorMessage", "The traffic from " + pathModel.getOriginVertex() + " to " + pathModel.getDestinationVertex() + " already exists");
			return "validation";
		}
		daoManagerService.updateTraffic(traffic);
		return "redirect:/traffic/" + traffic.getRouteId();
	}
}