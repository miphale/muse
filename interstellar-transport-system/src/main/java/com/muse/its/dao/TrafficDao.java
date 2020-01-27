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

import com.muse.its.entity.Traffic;

@Repository
@Transactional
public class TrafficDao {
	private static final Logger log = LogManager.getLogger(TrafficDao.class);
	private SessionFactory sessionFactory;
	
	@Autowired
	public TrafficDao(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public void saveTraffic(Traffic traffic) {
		Session session = sessionFactory.getCurrentSession();
		session.save(traffic);
	}
	
	public void updateTraffic(Traffic traffic) {
		Session session = sessionFactory.getCurrentSession();
		try {
			Traffic t = session.get(Traffic.class, traffic.getRouteId());
			t.setTrafficDelay(traffic.getTrafficDelay());
			t.setPlanetDestination(traffic.getPlanetDestination());
			t.setPlanetOrigin(traffic.getPlanetOrigin());
			session.merge(t);
		} catch (Exception e) {
			if(session.getTransaction() != null) {
				log.error("Traffic update failed. Transaction is being rolled back.");
				session.getTransaction().rollback();
			}
			e.printStackTrace();
		}
	}
	
	public void deleteTraffic(String routeId) {
		try {
			Session session = sessionFactory.getCurrentSession();
			Traffic traffic = new Traffic();
			traffic.setRouteId(routeId);
			session.delete(traffic);
		} catch (Exception e) {
			log.error("Traffic deletion failed.");
			e.printStackTrace();
		}		
	}
	
	public List<Traffic> findAll() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaQuery<Traffic> criteriaQuery = session.getCriteriaBuilder().createQuery(Traffic.class);
		criteriaQuery.from(Traffic.class);
		return session.createQuery(criteriaQuery).getResultList();
	}
	
	public Traffic findByRouteId(String routeId) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Traffic> criteriaQuery = builder.createQuery(Traffic.class);
		Root<Traffic> root = criteriaQuery.from(Traffic.class);
		criteriaQuery.where(builder.equal(root.get("routeId"), routeId));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
	
	public Traffic trafficExists(Traffic traffic) {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Traffic> criteriaQuery = builder.createQuery(Traffic.class);
		Root<Traffic> root = criteriaQuery.from(Traffic.class);
		criteriaQuery.where(builder.notEqual(root.get("routeId"), traffic.getRouteId()));
		criteriaQuery.where(builder.notEqual(root.get("planetOrigin"), traffic.getPlanetOrigin()));
		criteriaQuery.where(builder.notEqual(root.get("planetDestination"), traffic.getPlanetDestination()));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
	
	public Long selectMaxRecordId() {
		Session session = sessionFactory.getCurrentSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Long> criteriaQuery = builder.createQuery(Long.class);
		criteriaQuery.select(builder.count(criteriaQuery.from(Traffic.class)));
		return session.createQuery(criteriaQuery).getSingleResult();
	}
}