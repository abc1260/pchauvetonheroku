package com.david.herokudemo.repository;

import com.david.herokudemo.domain.Planetary_system;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Planetary_system entity.
 */
@SuppressWarnings("unused")
public interface Planetary_systemRepository extends JpaRepository<Planetary_system,Long> {

}
