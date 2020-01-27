package com.muse.its.service;

import java.util.List;

import com.muse.its.entity.Planet;
import com.muse.its.entity.Route;
import com.muse.its.entity.Traffic;
import com.muse.its.utils.Graph;

public interface DaoManagerService {
	/*
	 * Interstellar transport graph data service calls
	 */
	void persistAllData();
	Graph initialiseGraph();
	
	/*
	 * Planet DAO service calls
	 */
	Planet savePlanet(Planet planet);
	Planet updatePlanet(Planet planet);
	void deletePlanet(String node);
	List<Planet> getAllPlanets();
	Planet getPlanetByNode(String node);
	Planet getPlanetByName(String name);
	Boolean planetExists(Planet planet);
	
	/*
	 * Route DAO service calls
	 */
	Route saveRoute(Route route);
	Route updateRoute(Route route);
	void deleteRoute(String routeId);
	List<Route> getAllRoutes();
	Route getRouteById(String routeId);
	Boolean routeExists(Route route);
	Long getRouteMaxRecordId();
	
	/*
	 * Traffic DAO service calls
	 */
	Traffic saveTraffic(Traffic traffic);
	Traffic updateTraffic(Traffic traffic);
	void deleteTraffic(String routeId);
	List<Traffic> getAllTraffics();
	Traffic getTrafficById(String routeId);
	Boolean trafficExists(Traffic traffic);
	Long getTrafficMaxRecordId();
}