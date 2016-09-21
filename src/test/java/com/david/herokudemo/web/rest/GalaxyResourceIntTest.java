package com.david.herokudemo.web.rest;

import com.david.herokudemo.PchauvetonherokuApp;

import com.david.herokudemo.domain.Galaxy;
import com.david.herokudemo.repository.GalaxyRepository;

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
 * Test class for the GalaxyResource REST controller.
 *
 * @see GalaxyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PchauvetonherokuApp.class)
public class GalaxyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";

    @Inject
    private GalaxyRepository galaxyRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restGalaxyMockMvc;

    private Galaxy galaxy;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        GalaxyResource galaxyResource = new GalaxyResource();
        ReflectionTestUtils.setField(galaxyResource, "galaxyRepository", galaxyRepository);
        this.restGalaxyMockMvc = MockMvcBuilders.standaloneSetup(galaxyResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Galaxy createEntity(EntityManager em) {
        Galaxy galaxy = new Galaxy()
                .name(DEFAULT_NAME)
                .type(DEFAULT_TYPE);
        return galaxy;
    }

    @Before
    public void initTest() {
        galaxy = createEntity(em);
    }

    @Test
    @Transactional
    public void createGalaxy() throws Exception {
        int databaseSizeBeforeCreate = galaxyRepository.findAll().size();

        // Create the Galaxy

        restGalaxyMockMvc.perform(post("/api/galaxies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(galaxy)))
                .andExpect(status().isCreated());

        // Validate the Galaxy in the database
        List<Galaxy> galaxies = galaxyRepository.findAll();
        assertThat(galaxies).hasSize(databaseSizeBeforeCreate + 1);
        Galaxy testGalaxy = galaxies.get(galaxies.size() - 1);
        assertThat(testGalaxy.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testGalaxy.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    public void getAllGalaxies() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);

        // Get all the galaxies
        restGalaxyMockMvc.perform(get("/api/galaxies?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(galaxy.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())));
    }

    @Test
    @Transactional
    public void getGalaxy() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);

        // Get the galaxy
        restGalaxyMockMvc.perform(get("/api/galaxies/{id}", galaxy.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(galaxy.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingGalaxy() throws Exception {
        // Get the galaxy
        restGalaxyMockMvc.perform(get("/api/galaxies/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGalaxy() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);
        int databaseSizeBeforeUpdate = galaxyRepository.findAll().size();

        // Update the galaxy
        Galaxy updatedGalaxy = galaxyRepository.findOne(galaxy.getId());
        updatedGalaxy
                .name(UPDATED_NAME)
                .type(UPDATED_TYPE);

        restGalaxyMockMvc.perform(put("/api/galaxies")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedGalaxy)))
                .andExpect(status().isOk());

        // Validate the Galaxy in the database
        List<Galaxy> galaxies = galaxyRepository.findAll();
        assertThat(galaxies).hasSize(databaseSizeBeforeUpdate);
        Galaxy testGalaxy = galaxies.get(galaxies.size() - 1);
        assertThat(testGalaxy.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testGalaxy.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void deleteGalaxy() throws Exception {
        // Initialize the database
        galaxyRepository.saveAndFlush(galaxy);
        int databaseSizeBeforeDelete = galaxyRepository.findAll().size();

        // Get the galaxy
        restGalaxyMockMvc.perform(delete("/api/galaxies/{id}", galaxy.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Galaxy> galaxies = galaxyRepository.findAll();
        assertThat(galaxies).hasSize(databaseSizeBeforeDelete - 1);
    }
}
