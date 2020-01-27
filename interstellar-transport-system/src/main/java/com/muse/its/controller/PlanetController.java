package com.muse.its.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.muse.its.entity.Planet;
import com.muse.its.service.DaoManagerService;

@Controller
@CrossOrigin(allowedHeaders = {"*"}, origins = {"*"})
public class PlanetController {
	private DaoManagerService daoManagerService;
	
	@Autowired
	public PlanetController(DaoManagerService daoManagerService) {
		this.daoManagerService = daoManagerService;
	}
	
	@GetMapping(value = "/planets")
	public String listPlanets(Model model) {
		List<Planet> planets = daoManagerService.getAllPlanets();
		model.addAttribute("planets", planets);
		return "planets";
	}
	
	@GetMapping(value = "/planet/{planetId}")
	public String showPlanet(@PathVariable String planetId, Model model) {
		model.addAttribute("planet", daoManagerService.getPlanetByNode(planetId));
		return "showplanet";
	}
	
	@GetMapping(value = "/planet/delete/{planetId}")
	public String deleteRoute(@PathVariable String planetId) {
		daoManagerService.deletePlanet(planetId);
		return "redirect:/planets";
	}
	
	@GetMapping(value = "/planet/new")
	public String addPlanet(Model model) {
		model.addAttribute("planet", new Planet());
		return "addplanet";
	}
	
	@PostMapping(value = "/planet")
	public String savePlanet(Planet planet, Model model) {
		if(daoManagerService.planetExists(planet)) {
			model.addAttribute("errorMessage", "The planet " + planet.getPlanetName() + " already exists");
			return "validation";
		}
		daoManagerService.savePlanet(planet);
		return "redirect:/planet/" + planet.getPlanetNode();
	}
	
	@GetMapping(value = "/planet/edit/{planetId}")
	public String editPlanet(@PathVariable String planetId, Model model) {
		model.addAttribute("planet", daoManagerService.getPlanetByNode(planetId));
		return "updateplanet";
	}
	
	@PostMapping(value = "/planet/update")
	public String updatePlanet(Planet planet) {
		daoManagerService.updatePlanet(planet);
		return "redirect:/planet/" + planet.getPlanetNode();
	}
}