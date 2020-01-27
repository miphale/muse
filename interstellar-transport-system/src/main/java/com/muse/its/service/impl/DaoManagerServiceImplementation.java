package com.muse.its.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.muse.its.dao.PlanetDao;
import com.muse.its.dao.RouteDao;
import com.muse.its.dao.TrafficDao;
import com.muse.its.dto.Edge;
import com.muse.its.dto.TrafficDelay;
import com.muse.its.dto.Vertex;
import com.muse.its.entity.Planet;
import com.muse.its.entity.Route;
import com.muse.its.entity.Traffic;
import com.muse.its.service.DaoManagerService;
import com.muse.its.utils.ExcelDataHandler;
import com.muse.its.utils.Graph;
import com.muse.its.utils.InterstellarTransportationSystemConstants;

@Service
public class DaoManagerServiceImplementation implements DaoManagerService {
	private static final Logger log = LogManager.getLogger(DaoManagerServiceImplementation.class);
	private PlanetDao planetDao;
	private RouteDao routeDao;
	private TrafficDao trafficDao;

	@Autowired
	public DaoManagerServiceImplementation(PlanetDao planetDao, RouteDao routeDao, TrafficDao trafficDao) {
		this.planetDao = planetDao;
		this.routeDao = routeDao;
		this.trafficDao = trafficDao;
	}
	
	@Override
	public void persistAllData() {
		try {
			File file = new File(getClass().getClassLoader().getResource(InterstellarTransportationSystemConstants.DATA_EXCEL_FILENAME).getFile());
			
			ExcelDataHandler dataHandler = new ExcelDataHandler(file);
			
			// Load and save Planet data
			List<Planet> planets = dataHandler.readPlanetData();
			if(planets != null && !planets.isEmpty()) {
				for(Planet planet: planets) {
					planetDao.savePlanet(planet);
				}
			}
			
			// Load and save Route data
			List<Route> routes = dataHandler.readRouteData();
			if(routes != null && !routes.isEmpty()) {
				for(Route route: routes) {
					routeDao.saveRoute(route);
				}
			}
			
			// Load and save Route Traffic data
			List<Traffic> traffics = dataHandler.readRouteTrafficData();
			if(traffics != null && !traffics.isEmpty()) {
				for(Traffic traffic: traffics) {
					trafficDao.saveTraffic(traffic);
				}
			}
		} catch (IOException e) {
			log.error("Exception processing data file: " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			log.error("Exception Persisting data: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public Graph initialiseGraph() {
		List<Vertex> vertices = new ArrayList<Vertex>();
		List<Edge> edges = new ArrayList<Edge>();
		List<TrafficDelay> trafficDelays = new ArrayList<TrafficDelay>();
		
		// Retrieve all Planets
		List<Planet> planets = planetDao.findAll();
		if(planets != null && !planets.isEmpty()) {
			for(Planet planet: planets) {
				vertices.add(new Vertex(planet.getPlanetNode(), planet.getPlanetName()));
			}
		}
		
		// Retrieve Routes and other related data
		List<Route> routes = routeDao.findAll();
		if(routes != null && !routes.isEmpty()) {
			for(Route route: routes) {
				Edge edge = new Edge();
				edge.setId(route.getRouteId());
				edge.setDistance(route.getDistance());
				
				Planet originPlanet = planetDao.findByPlanetNode(route.getPlanetOrigin());
				if(originPlanet != null) {
					log.debug("Origin planet: " + originPlanet.getPlanetName() + " : " + originPlanet.getPlanetNode());
					edge.setOrigin(new Vertex(originPlanet.getPlanetNode(), originPlanet.getPlanetName()));
				}
				log.debug("Looking up destination planet: " + route.getPlanetDestination());
				Planet destinationPlanet = planetDao.findByPlanetNode(route.getPlanetDestination());
				if(destinationPlanet != null) {
					log.debug("Destination planet: " + destinationPlanet.getPlanetName() + " : " + destinationPlanet.getPlanetNode());
					edge.setDestination(new Vertex(destinationPlanet.getPlanetNode(), destinationPlanet.getPlanetName()));
				}
				
				//Edge is valid so add it to list
				if(edge.getOrigin() != null && edge.getDestination() != null) {
					edges.add(edge);
				}
			}
		}
		
		// Retrieve Traffic data
		List<Traffic> traffics = trafficDao.findAll();
		if(traffics != null && !traffics.isEmpty()) {
			for(Traffic traffic: traffics) {
				TrafficDelay delay = new TrafficDelay();
				delay.setRouteId(traffic.getRouteId());
				delay.setDelay(traffic.getTrafficDelay());
				
				Planet originPlanet = planetDao.findByPlanetNode(traffic.getPlanetOrigin());
				if(originPlanet != null) {
					delay.setOrigin(new Vertex(originPlanet.getPlanetNode(), originPlanet.getPlanetName()));
				}
				
				Planet destinationPlanet = planetDao.findByPlanetNode(traffic.getPlanetDestination());
				if(destinationPlanet != null) {
					delay.setDestination(new Vertex(destinationPlanet.getPlanetNode(), destinationPlanet.getPlanetName()));
				}
				
				trafficDelays.add(delay);
			}
		}
		
		Graph graph = new Graph(vertices, edges, trafficDelays);
		return graph;
	}

	@Override
	public Planet savePlanet(Planet planet) {
		planetDao.savePlanet(planet);
		return planet;
	}

	@Override
	public Planet updatePlanet(Planet planet) {
		planetDao.updatePlanet(planet);
		return planet;
	}

	@Override
	public void deletePlanet(String node) {
		planetDao.deletePlanet(node);
	}

	@Override
	public List<Planet> getAllPlanets() {
		return planetDao.findAll();
	}

	@Override
	public Planet getPlanetByNode(String node) {
		return planetDao.findByPlanetNode(node);
	}

	@Override
	public Planet getPlanetByName(String name) {
		return planetDao.findByPlanetName(name);
	}

	@Override
	public Boolean planetExists(Planet planet) {
		return planetDao.planetExists(planet) != null;
	}

	@Override
	public Route saveRoute(Route route) {
		routeDao.saveRoute(route);
		return route;
	}

	@Override
	public Route updateRoute(Route route) {
		routeDao.updateRoute(route);
		return route;
	}

	@Override
	public void deleteRoute(String routeId) {
		routeDao.deleteRoute(routeId);
	}

	@Override
	public List<Route> getAllRoutes() {
		return routeDao.findAll();
	}

	@Override
	public Route getRouteById(String routeId) {
		return routeDao.findByRouteId(routeId);
	}

	@Override
	public Boolean routeExists(Route route) {
		return routeDao.routeExists(route) != null;
	}

	@Override
	public Long getRouteMaxRecordId() {
		return routeDao.selectMaxRecordId();
	}

	@Override
	public Traffic saveTraffic(Traffic traffic) {
		trafficDao.saveTraffic(traffic);
		return traffic;
	}

	@Override
	public Traffic updateTraffic(Traffic traffic) {
		trafficDao.updateTraffic(traffic);
		return traffic;
	}

	@Override
	public void deleteTraffic(String routeId) {
		trafficDao.deleteTraffic(routeId);
	}

	@Override
	public List<Traffic> getAllTraffics() {
		return trafficDao.findAll();
	}

	@Override
	public Traffic getTrafficById(String routeId) {
		return trafficDao.findByRouteId(routeId);
	}

	@Override
	public Boolean trafficExists(Traffic traffic) {
		return trafficDao.trafficExists(traffic) != null;
	}

	@Override
	public Long getTrafficMaxRecordId() {
		return trafficDao.selectMaxRecordId();
	}
}