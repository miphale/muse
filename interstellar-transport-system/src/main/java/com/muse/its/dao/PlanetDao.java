package com.muse.its.dao;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.muse.its.entity.Planet;


@Repository
@Transactional
public class PlanetDao {
	private static final Logger log = LogManager.getLogger(PlanetDao.class);
	private SessionFactory sessionFactory;
	
	@Autowired
	public PlanetDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void savePlanet(Planet planet) {
		Session session = sessionFactory.getCurrentSession();
		session.save(planet);
	}
	
	/*public void updatePlanet(Planet planet) {
		Session session = sessionFactory.getCurrentSession();
		session.merge(planet);
	}*/
	
	public void updatePlanet(Planet planet) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Planet p = session.get(Planet.class, planet.getPlanetNode());
			p.setPlanetName(planet.getPlanetName());
			session.merge(p);
		} catch (Exception e) {
			if(session.getTransaction() != null) {
				log.error("Planet update failed. Transaction is being rolled back.");
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}
	}
	
	public void deletePlanet(String planetNode) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Planet planet = new Planet();
			planet.setPlanetNode(planetNode);;
			session.delete(planet);
		} catch (Exception e) {
			log.error("Planet delete failed.");
			e.printStackTrace();
		}
	}
	
	public List<Planet> findAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Planet> criteriaQuery = session.getCriteriaBuilder().createQuery(Planet.class);
		criteriaQuery.from(Planet.class);
		return session.createQuery(criteriaQuery).getResultList();
	}
	
	/*public Planet findByPlanetNode(String planetNode) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Planet> criteriaQuery = builder.createQuery(Planet.class);
		Root<Planet> root = criteriaQuery.from(Planet.class);
		criteriaQuery.where(builder.equal(root.get("planetNode"), planetNode));
		return session.createQuery(criteriaQuery).getSingleResult();
	}*/
	
	@SuppressWarnings("deprecation")
	/*
	 * TODO: replace with a prepared statement or another better way to deal
	 * with planet names string literals
	 */
	public Planet findByPlanetNode(String planetNode) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(Planet.class);
		criteria.add(Restrictions.eq("planetNode", planetNode));
		return (Planet) criteria.uniqueResult();
	}
	
	public Planet findByPlanetName(String planetName) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Planet> criteriaQuery = builder.createQuery(Planet.class);
		Root<Planet> root = criteriaQuery.from(Planet.class);
		criteriaQuery.where(builder.equal(root.get("planetName"), planetName));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
	
	public Planet planetExists(Planet planet) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Planet> criteriaQuery = builder.createQuery(Planet.class);
		Root<Planet> root = criteriaQuery.from(Planet.class);
		criteriaQuery.where(builder.notEqual(root.get("planetNode"), planet.getPlanetNode()));
		criteriaQuery.where(builder.notEqual(root.get("planetName"), planet.getPlanetName()));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
}