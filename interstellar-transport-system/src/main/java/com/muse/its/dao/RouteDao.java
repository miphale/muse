package com.muse.its.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.muse.its.entity.Route;

@Repository
@Transactional
public class RouteDao {
	private static final Logger log = LogManager.getLogger(RouteDao.class);
	private SessionFactory sessionFactory;
	
	@Autowired
	public RouteDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveRoute(Route route) {
		Session session = sessionFactory.getCurrentSession();
		session.save(route);
	}
	
	public void updateRoute(Route route) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Route r = session.get(Route.class, route.getRouteId());
			r.setDistance(route.getDistance());
			r.setPlanetDestination(route.getPlanetDestination());
			r.setPlanetOrigin(route.getPlanetOrigin());
			session.merge(r);
		} catch (Exception e) {
			if(session.getTransaction() != null) {
				log.error("Planet update failed. Transaction is being rolled back.");
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}
	}
	
	public void deleteRoute(String routeId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Route route = new Route();
			route.setRouteId(routeId);
			session.delete(route);
		} catch (Exception e) {
			log.error("Route deletion failed.");
			e.printStackTrace();
		}
	}
	
	public List<Route> findAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Route> criteriaQuery = session.getCriteriaBuilder().createQuery(Route.class);
		criteriaQuery.from(Route.class);
		return session.createQuery(criteriaQuery).getResultList();
	}
	
	public Route findByRouteId(String routeId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Route> criteriaQuery = builder.createQuery(Route.class);
		Root<Route> root = criteriaQuery.from(Route.class);
		criteriaQuery.where(builder.equal(root.get("routeId"), routeId));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
	
	public Route routeExists(Route route) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Route> criteriaQuery = builder.createQuery(Route.class);
		Root<Route> root = criteriaQuery.from(Route.class);
		criteriaQuery.where(builder.notEqual(root.get("routeId"), route.getRouteId()));
		criteriaQuery.where(builder.notEqual(root.get("planetOrigin"), route.getPlanetOrigin()));
		criteriaQuery.where(builder.notEqual(root.get("planetDestination"), route.getPlanetDestination()));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
	
	public Long selectMaxRecordId() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		criteriaQuery.select(builder.count(criteriaQuery.from(Route.class)));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
}