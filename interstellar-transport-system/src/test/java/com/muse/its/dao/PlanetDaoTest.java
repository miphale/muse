package com.muse.its.dao;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.transaction.annotation.Transactional;

import com.muse.its.config.DataSourceConfig;
import com.muse.its.config.PersistenceConfig;
import com.muse.its.entity.Planet;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Planet.class, PlanetDao.class, DataSourceConfig.class, PersistenceConfig.class},
loader = AnnotationConfigContextLoader.class)
public class PlanetDaoTest {
	
	@Autowired
	private SessionFactory sessionFactory;
	private PlanetDao planetDao;
	
	@Before
	public void initialize() throws Exception {
		planetDao = new PlanetDao(sessionFactory);
	}
	
	@Test
	public void verifySavePlanet() throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Planet planetToSave = new Planet("GF", "Gallifrey");
		planetDao.savePlanet(planetToSave);
		
		Planet savedPlanet = session.get(Planet.class, planetToSave.getPlanetNode());
		
		assertNotNull(savedPlanet);
		assertEquals("GF", savedPlanet.getPlanetNode());
		
		//Rollback in test mode
		session.getTransaction().rollback();
	}
	
	@Test
	public void verifyUpdatePlanet() throws Exception {
		Session session = sessionFactory.getCurrentSession();
		Planet p = new Planet("GF", "Gallifrey");
		planetDao.savePlanet(p);
		
		Planet planeToUpdate = new Planet("GF", "Yodush");
		
		planetDao.updatePlanet(planeToUpdate);
		
		Planet updatedPlanet = session.get(Planet.class, planeToUpdate.getPlanetNode());
		
		assertEquals(planeToUpdate.getPlanetNode(), updatedPlanet.getPlanetNode());
		assertEquals(planeToUpdate.getPlanetName(), updatedPlanet.getPlanetName());
		
		//Rollback in test mode
		session.getTransaction().rollback();
	}
}