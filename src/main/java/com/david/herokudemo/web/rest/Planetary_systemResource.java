package com.david.herokudemo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.david.herokudemo.domain.Planetary_system;

import com.david.herokudemo.repository.Planetary_systemRepository;
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
 * REST controller for managing Planetary_system.
 */
@RestController
@RequestMapping("/api")
public class Planetary_systemResource {

    private final Logger log = LoggerFactory.getLogger(Planetary_systemResource.class);
        
    @Inject
    private Planetary_systemRepository planetary_systemRepository;

    /**
     * POST  /planetary-systems : Create a new planetary_system.
     *
     * @param planetary_system the planetary_system to create
     * @return the ResponseEntity with status 201 (Created) and with body the new planetary_system, or with status 400 (Bad Request) if the planetary_system has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/planetary-systems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planetary_system> createPlanetary_system(@RequestBody Planetary_system planetary_system) throws URISyntaxException {
        log.debug("REST request to save Planetary_system : {}", planetary_system);
        if (planetary_system.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("planetary_system", "idexists", "A new planetary_system cannot already have an ID")).body(null);
        }
        Planetary_system result = planetary_systemRepository.save(planetary_system);
        return ResponseEntity.created(new URI("/api/planetary-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("planetary_system", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /planetary-systems : Updates an existing planetary_system.
     *
     * @param planetary_system the planetary_system to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated planetary_system,
     * or with status 400 (Bad Request) if the planetary_system is not valid,
     * or with status 500 (Internal Server Error) if the planetary_system couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/planetary-systems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planetary_system> updatePlanetary_system(@RequestBody Planetary_system planetary_system) throws URISyntaxException {
        log.debug("REST request to update Planetary_system : {}", planetary_system);
        if (planetary_system.getId() == null) {
            return createPlanetary_system(planetary_system);
        }
        Planetary_system result = planetary_systemRepository.save(planetary_system);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("planetary_system", planetary_system.getId().toString()))
            .body(result);
    }

    /**
     * GET  /planetary-systems : get all the planetary_systems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of planetary_systems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/planetary-systems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Planetary_system>> getAllPlanetary_systems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Planetary_systems");
        Page<Planetary_system> page = planetary_systemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/planetary-systems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /planetary-systems/:id : get the "id" planetary_system.
     *
     * @param id the id of the planetary_system to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the planetary_system, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/planetary-systems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Planetary_system> getPlanetary_system(@PathVariable Long id) {
        log.debug("REST request to get Planetary_system : {}", id);
        Planetary_system planetary_system = planetary_systemRepository.findOne(id);
        return Optional.ofNullable(planetary_system)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /planetary-systems/:id : delete the "id" planetary_system.
     *
     * @param id the id of the planetary_system to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/planetary-systems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePlanetary_system(@PathVariable Long id) {
        log.debug("REST request to delete Planetary_system : {}", id);
        planetary_systemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("planetary_system", id.toString())).build();
    }

}
