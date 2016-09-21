package com.david.herokudemo.repository;

import com.david.herokudemo.domain.Planet;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Planet entity.
 */
@SuppressWarnings("unused")
public interface PlanetRepository extends JpaRepository<Planet,Long> {

}
