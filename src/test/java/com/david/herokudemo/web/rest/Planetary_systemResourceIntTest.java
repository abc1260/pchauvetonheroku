package com.david.herokudemo.web.rest;

import com.david.herokudemo.PchauvetonherokuApp;

import com.david.herokudemo.domain.Planetary_system;
import com.david.herokudemo.repository.Planetary_systemRepository;

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
 * Test class for the Planetary_systemResource REST controller.
 *
 * @see Planetary_systemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PchauvetonherokuApp.class)
public class Planetary_systemResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_STAR = "AAAAA";
    private static final String UPDATED_STAR = "BBBBB";
    private static final String DEFAULT_GALAXY = "AAAAA";
    private static final String UPDATED_GALAXY = "BBBBB";

    @Inject
    private Planetary_systemRepository planetary_systemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restPlanetary_systemMockMvc;

    private Planetary_system planetary_system;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Planetary_systemResource planetary_systemResource = new Planetary_systemResource();
        ReflectionTestUtils.setField(planetary_systemResource, "planetary_systemRepository", planetary_systemRepository);
        this.restPlanetary_systemMockMvc = MockMvcBuilders.standaloneSetup(planetary_systemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Planetary_system createEntity(EntityManager em) {
        Planetary_system planetary_system = new Planetary_system()
                .name(DEFAULT_NAME)
                .star(DEFAULT_STAR)
                .galaxy(DEFAULT_GALAXY);
        return planetary_system;
    }

    @Before
    public void initTest() {
        planetary_system = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlanetary_system() throws Exception {
        int databaseSizeBeforeCreate = planetary_systemRepository.findAll().size();

        // Create the Planetary_system

        restPlanetary_systemMockMvc.perform(post("/api/planetary-systems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(planetary_system)))
                .andExpect(status().isCreated());

        // Validate the Planetary_system in the database
        List<Planetary_system> planetary_systems = planetary_systemRepository.findAll();
        assertThat(planetary_systems).hasSize(databaseSizeBeforeCreate + 1);
        Planetary_system testPlanetary_system = planetary_systems.get(planetary_systems.size() - 1);
        assertThat(testPlanetary_system.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPlanetary_system.getStar()).isEqualTo(DEFAULT_STAR);
        assertThat(testPlanetary_system.getGalaxy()).isEqualTo(DEFAULT_GALAXY);
    }

    @Test
    @Transactional
    public void getAllPlanetary_systems() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);

        // Get all the planetary_systems
        restPlanetary_systemMockMvc.perform(get("/api/planetary-systems?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(planetary_system.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].star").value(hasItem(DEFAULT_STAR.toString())))
                .andExpect(jsonPath("$.[*].galaxy").value(hasItem(DEFAULT_GALAXY.toString())));
    }

    @Test
    @Transactional
    public void getPlanetary_system() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);

        // Get the planetary_system
        restPlanetary_systemMockMvc.perform(get("/api/planetary-systems/{id}", planetary_system.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(planetary_system.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.star").value(DEFAULT_STAR.toString()))
            .andExpect(jsonPath("$.galaxy").value(DEFAULT_GALAXY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPlanetary_system() throws Exception {
        // Get the planetary_system
        restPlanetary_systemMockMvc.perform(get("/api/planetary-systems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlanetary_system() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);
        int databaseSizeBeforeUpdate = planetary_systemRepository.findAll().size();

        // Update the planetary_system
        Planetary_system updatedPlanetary_system = planetary_systemRepository.findOne(planetary_system.getId());
        updatedPlanetary_system
                .name(UPDATED_NAME)
                .star(UPDATED_STAR)
                .galaxy(UPDATED_GALAXY);

        restPlanetary_systemMockMvc.perform(put("/api/planetary-systems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedPlanetary_system)))
                .andExpect(status().isOk());

        // Validate the Planetary_system in the database
        List<Planetary_system> planetary_systems = planetary_systemRepository.findAll();
        assertThat(planetary_systems).hasSize(databaseSizeBeforeUpdate);
        Planetary_system testPlanetary_system = planetary_systems.get(planetary_systems.size() - 1);
        assertThat(testPlanetary_system.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPlanetary_system.getStar()).isEqualTo(UPDATED_STAR);
        assertThat(testPlanetary_system.getGalaxy()).isEqualTo(UPDATED_GALAXY);
    }

    @Test
    @Transactional
    public void deletePlanetary_system() throws Exception {
        // Initialize the database
        planetary_systemRepository.saveAndFlush(planetary_system);
        int databaseSizeBeforeDelete = planetary_systemRepository.findAll().size();

        // Get the planetary_system
        restPlanetary_systemMockMvc.perform(delete("/api/planetary-systems/{id}", planetary_system.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Planetary_system> planetary_systems = planetary_systemRepository.findAll();
        assertThat(planetary_systems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
