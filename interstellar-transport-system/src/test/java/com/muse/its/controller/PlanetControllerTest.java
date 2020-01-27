package com.muse.its.controller;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.ModelResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.muse.its.entity.Planet;
import com.muse.its.service.DaoManagerService;

@RunWith(SpringJUnit4ClassRunner.class)
public class PlanetControllerTest {
	@Mock
	View mockView;
	@InjectMocks
	private PlanetController controller;
	@Mock
	private DaoManagerService daoManagerService;
	private MockMvc mockMvc;
	private List<Planet> planets;
	
	@Before
	public void initialize() throws Exception {
		Planet vertex1 = new Planet("A", "Earth");
		Planet vertex2 = new Planet("B", "Moon");
		Planet vertex3 = new Planet("C", "Jupiter");
		Planet vertex4 = new Planet("D", "Venus");
		Planet vertex5 = new Planet("E", "Mars");

		planets = new ArrayList<>();
		planets.add(vertex1);
		planets.add(vertex2);
		planets.add(vertex3);
		planets.add(vertex4);
		planets.add(vertex5);
		
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setSingleView(mockView).build();
	}
	
	@Test
	public void verifyListPlanetsView() throws Exception {
		Mockito.when(daoManagerService.getAllPlanets()).thenReturn(planets);
		setUp();
		mockMvc.perform(MockMvcRequestBuilders.get("/planets"))
			.andExpect(MockMvcResultMatchers.model().attribute("planets", planets))
			.andExpect(MockMvcResultMatchers.view().name("planets"));
	}
	
	public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new PlanetController(daoManagerService))
        		.setViewResolvers(getInternalResourceViewResolver())
                .build();
    }
	
	private InternalResourceViewResolver getInternalResourceViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}
