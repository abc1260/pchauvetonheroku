package com.david.herokudemo.web.rest;

import com.david.herokudemo.PchauvetonherokuApp;

import com.david.herokudemo.domain.Planet;
import com.david.herokudemo.repository.PlanetRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PlanetResource REST controller.
 *
 * @see PlanetResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PchauvetonherokuApp.class)
public class PlanetResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    private static final Float DEFAULT_SURFACE = 1F;
    private static final Float UPDATED_SURFACE = 2F;

    private static final Float DEFAULT_RADIUS = 1F;
    private static final Float UPDATED_RADIUS = 2F;
    private static final String DEFAULT_SYSTEM = "AAAAA";
    private static final String UPDATED_SYSTEM = "BBBBB";

    @Inject
    private PlanetRepository planetRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPlanetMockMvc;

    private Planet planet;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PlanetResource planetResource = new PlanetResource();
        ReflectionTestUtils.setField(planetResource, "planetRepository", planetRepository);
        this.restPlanetMockMvc = MockMvcBuilders.standaloneSetup(planetResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planet createEntity(EntityManager em) {
        Planet planet = new Planet()
                .name(DEFAULT_NAME)
                .surface(DEFAULT_SURFACE)
                .radius(DEFAULT_RADIUS)
                .system(DEFAULT_SYSTEM);
        return planet;
    }

    @Before
    public void initTest() {
        planet = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanet() throws Exception {
        int databaseSizeBeforeCreate = planetRepository.findAll().size();

        // Create the Planet

        restPlanetMockMvc.perform(post("/api/planets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planet)))
                .andExpect(status().isCreated());

        // Validate the Planet in the database
        List<Planet> planets = planetRepository.findAll();
        assertThat(planets).hasSize(databaseSizeBeforeCreate + 1);
        Planet testPlanet = planets.get(planets.size() - 1);
        assertThat(testPlanet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanet.getSurface()).isEqualTo(DEFAULT_SURFACE);
        assertThat(testPlanet.getRadius()).isEqualTo(DEFAULT_RADIUS);
        assertThat(testPlanet.getSystem()).isEqualTo(DEFAULT_SYSTEM);
    }

    @Test
    @Transactional
    public void getAllPlanets() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);

        // Get all the planets
        restPlanetMockMvc.perform(get("/api/planets?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(planet.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].surface").value(hasItem(DEFAULT_SURFACE.doubleValue())))
                .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.doubleValue())))
                .andExpect(jsonPath("$.[*].system").value(hasItem(DEFAULT_SYSTEM.toString())));
    }

    @Test
    @Transactional
    public void getPlanet() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);

        // Get the planet
        restPlanetMockMvc.perform(get("/api/planets/{id}", planet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.surface").value(DEFAULT_SURFACE.doubleValue()))
            .andExpect(jsonPath("$.radius").value(DEFAULT_RADIUS.doubleValue()))
            .andExpect(jsonPath("$.system").value(DEFAULT_SYSTEM.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanet() throws Exception {
        // Get the planet
        restPlanetMockMvc.perform(get("/api/planets/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanet() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);
        int databaseSizeBeforeUpdate = planetRepository.findAll().size();

        // Update the planet
        Planet updatedPlanet = planetRepository.findOne(planet.getId());
        updatedPlanet
                .name(UPDATED_NAME)
                .surface(UPDATED_SURFACE)
                .radius(UPDATED_RADIUS)
                .system(UPDATED_SYSTEM);

        restPlanetMockMvc.perform(put("/api/planets")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlanet)))
                .andExpect(status().isOk());

        // Validate the Planet in the database
        List<Planet> planets = planetRepository.findAll();
        assertThat(planets).hasSize(databaseSizeBeforeUpdate);
        Planet testPlanet = planets.get(planets.size() - 1);
        assertThat(testPlanet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanet.getSurface()).isEqualTo(UPDATED_SURFACE);
        assertThat(testPlanet.getRadius()).isEqualTo(UPDATED_RADIUS);
        assertThat(testPlanet.getSystem()).isEqualTo(UPDATED_SYSTEM);
    }

    @Test
    @Transactional
    public void deletePlanet() throws Exception {
        // Initialize the database
        planetRepository.saveAndFlush(planet);
        int databaseSizeBeforeDelete = planetRepository.findAll().size();

        // Get the planet
        restPlanetMockMvc.perform(delete("/api/planets/{id}", planet.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Planet> planets = planetRepository.findAll();
        assertThat(planets).hasSize(databaseSizeBeforeDelete - 1);
    }
}
