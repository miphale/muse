package com.muse.its.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.muse.its.entity.Planet;
import com.muse.its.entity.Route;
import com.muse.its.model.ShortestPathModel;
import com.muse.its.service.DaoManagerService;

@Controller
@CrossOrigin(allowedHeaders = {"*"}, origins = {"*"})
public class RouteController {
	private DaoManagerService daoManagerService;
	
	@Autowired
	public RouteController(DaoManagerService daoManagerService) {
		this.daoManagerService = daoManagerService;
	}
	
	@GetMapping(value = "/routes")
	public String listRoutes(Model model) {
		List<Route> routes = daoManagerService.getAllRoutes();
		model.addAttribute("routes", routes);
		return "routes";
	}
	
	@GetMapping(value = "/route/{routeId}")
	public String showRoute(@PathVariable String routeId, Model model) {
		model.addAttribute("route", daoManagerService.getRouteById(routeId));
		return "showroute";
	}
	
	@GetMapping(value = "/route/delete/{routeId}")
	public String deleteRoute(@PathVariable String routeId) {
		daoManagerService.deleteRoute(routeId);
		return "redirect:/routes";
	}
	
	@GetMapping(value = "/route/new")
	public String addRoute(Model model) {
		ShortestPathModel pathModel = new ShortestPathModel();
		List<Planet> planets = daoManagerService.getAllPlanets();
		model.addAttribute("route", new Route());
		model.addAttribute("routeModel", pathModel);
		model.addAttribute("planetList", planets);
		return "addroute";
	}
	
	@PostMapping(value = "/route")
	public String saveRoute(Route route, @ModelAttribute ShortestPathModel pathModel, Model model) {
		Long routeId = daoManagerService.getRouteMaxRecordId() + 1;
		route.setRouteId(String.valueOf(routeId));
		route.setPlanetOrigin(pathModel.getOriginVertex());
		route.setPlanetDestination(pathModel.getDestinationVertex());
		if(pathModel.getOriginVertex().equals(pathModel.getDestinationVertex())) {
			model.addAttribute("errorMessage", "The route from " + pathModel.getOriginVertex() + " to " + pathModel.getDestinationVertex() + " is an invalid route");
			return "validation";
		}
		if(daoManagerService.routeExists(route)) {
			model.addAttribute("errorMessage", "The route from " + pathModel.getOriginVertex() + " to " + pathModel.getDestinationVertex() + " already exists");
			return "validation";
		}
		daoManagerService.saveRoute(route);
		return "redirect:/route/" + route.getRouteId();
	}
	
	@GetMapping(value = "/route/edit/{routeId}")
	public String editRoute(@PathVariable String routeId, Model model) {
		ShortestPathModel pathModel = new ShortestPathModel();
		List<Planet> planets = daoManagerService.getAllPlanets();
		Route route = daoManagerService.getRouteById(routeId);
		pathModel.setOriginVertex(route.getPlanetOrigin());
		pathModel.setDestinationVertex(route.getPlanetDestination());
		model.addAttribute("route", route);
		model.addAttribute("routeModel", pathModel);
		model.addAttribute("planetList", planets);
		return "updateroute";
	}
	
	@PostMapping(value = "/route/update")
	public String updateRoute(Route route, @ModelAttribute ShortestPathModel pathModel, Model model) {
		route.setPlanetOrigin(pathModel.getOriginVertex());
		route.setPlanetDestination(pathModel.getDestinationVertex());
		if(pathModel.getOriginVertex().equals(pathModel.getDestinationVertex())) {
			model.addAttribute("errorMessage", "The route from " + pathModel.getOriginVertex() + " to " + pathModel.getDestinationVertex() + " is an invalid route");
			return "validation";
		}
		daoManagerService.updateRoute(route);
		return "redirect:/route/" + route.getRouteId();
	}
}
