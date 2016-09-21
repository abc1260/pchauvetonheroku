package com.david.herokudemo.repository;

import com.david.herokudemo.domain.Galaxy;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Galaxy entity.
 */
@SuppressWarnings("unused")
public interface GalaxyRepository extends JpaRepository<Galaxy,Long> {

}
