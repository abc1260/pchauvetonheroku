package com.david.herokudemo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.david.herokudemo.domain.Planet;

import com.david.herokudemo.repository.PlanetRepository;
import com.david.herokudemo.web.rest.util.HeaderUtil;
import com.david.herokudemo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Planet.
 */
@RestController
@RequestMapping("/api")
public class PlanetResource {

    private final Logger log = LoggerFactory.getLogger(PlanetResource.class);
        
    @Inject
    private PlanetRepository planetRepository;

    /**
     * POST  /planets : Create a new planet.
     *
     * @param planet the planet to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planet, or with status 400 (Bad Request) if the planet has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/planets",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planet> createPlanet(@RequestBody Planet planet) throws URISyntaxException {
        log.debug("REST request to save Planet : {}", planet);
        if (planet.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("planet", "idexists", "A new planet cannot already have an ID")).body(null);
        }
        Planet result = planetRepository.save(planet);
        return ResponseEntity.created(new URI("/api/planets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("planet", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planets : Updates an existing planet.
     *
     * @param planet the planet to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planet,
     * or with status 400 (Bad Request) if the planet is not valid,
     * or with status 500 (Internal Server Error) if the planet couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/planets",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planet> updatePlanet(@RequestBody Planet planet) throws URISyntaxException {
        log.debug("REST request to update Planet : {}", planet);
        if (planet.getId() == null) {
            return createPlanet(planet);
        }
        Planet result = planetRepository.save(planet);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("planet", planet.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planets : get all the planets.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of planets in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/planets",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Planet>> getAllPlanets(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Planets");
        Page<Planet> page = planetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planets");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /planets/:id : get the "id" planet.
     *
     * @param id the id of the planet to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planet, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/planets/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planet> getPlanet(@PathVariable Long id) {
        log.debug("REST request to get Planet : {}", id);
        Planet planet = planetRepository.findOne(id);
        return Optional.ofNullable(planet)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /planets/:id : delete the "id" planet.
     *
     * @param id the id of the planet to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/planets/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlanet(@PathVariable Long id) {
        log.debug("REST request to delete Planet : {}", id);
        planetRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("planet", id.toString())).build();
    }

}
