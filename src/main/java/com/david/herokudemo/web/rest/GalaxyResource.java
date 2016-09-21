package com.david.herokudemo.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.david.herokudemo.domain.Galaxy;

import com.david.herokudemo.repository.GalaxyRepository;
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
 * REST controller for managing Galaxy.
 */
@RestController
@RequestMapping("/api")
public class GalaxyResource {

    private final Logger log = LoggerFactory.getLogger(GalaxyResource.class);
        
    @Inject
    private GalaxyRepository galaxyRepository;

    /**
     * POST  /galaxies : Create a new galaxy.
     *
     * @param galaxy the galaxy to create
     * @return the ResponseEntity with status 201 (Created) and with body the new galaxy, or with status 400 (Bad Request) if the galaxy has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/galaxies",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Galaxy> createGalaxy(@RequestBody Galaxy galaxy) throws URISyntaxException {
        log.debug("REST request to save Galaxy : {}", galaxy);
        if (galaxy.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("galaxy", "idexists", "A new galaxy cannot already have an ID")).body(null);
        }
        Galaxy result = galaxyRepository.save(galaxy);
        return ResponseEntity.created(new URI("/api/galaxies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("galaxy", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /galaxies : Updates an existing galaxy.
     *
     * @param galaxy the galaxy to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated galaxy,
     * or with status 400 (Bad Request) if the galaxy is not valid,
     * or with status 500 (Internal Server Error) if the galaxy couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/galaxies",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Galaxy> updateGalaxy(@RequestBody Galaxy galaxy) throws URISyntaxException {
        log.debug("REST request to update Galaxy : {}", galaxy);
        if (galaxy.getId() == null) {
            return createGalaxy(galaxy);
        }
        Galaxy result = galaxyRepository.save(galaxy);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("galaxy", galaxy.getId().toString()))
            .body(result);
    }

    /**
     * GET  /galaxies : get all the galaxies.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of galaxies in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/galaxies",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Galaxy>> getAllGalaxies(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Galaxies");
        Page<Galaxy> page = galaxyRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/galaxies");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /galaxies/:id : get the "id" galaxy.
     *
     * @param id the id of the galaxy to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the galaxy, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/galaxies/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Galaxy> getGalaxy(@PathVariable Long id) {
        log.debug("REST request to get Galaxy : {}", id);
        Galaxy galaxy = galaxyRepository.findOne(id);
        return Optional.ofNullable(galaxy)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /galaxies/:id : delete the "id" galaxy.
     *
     * @param id the id of the galaxy to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/galaxies/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteGalaxy(@PathVariable Long id) {
        log.debug("REST request to delete Galaxy : {}", id);
        galaxyRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("galaxy", id.toString())).build();
    }

}
