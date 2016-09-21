package com.david.herokudemo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Planetary_system.
 */
@Entity
@Table(name = "planetary_system")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Planetary_system implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "star")
    private String star;

    @Column(name = "galaxy")
    private String galaxy;

    @ManyToOne
    private Galaxy galaxy_relationship;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Planetary_system name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStar() {
        return star;
    }

    public Planetary_system star(String star) {
        this.star = star;
        return this;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getGalaxy() {
        return galaxy;
    }

    public Planetary_system galaxy(String galaxy) {
        this.galaxy = galaxy;
        return this;
    }

    public void setGalaxy(String galaxy) {
        this.galaxy = galaxy;
    }

    public Galaxy getGalaxy_relationship() {
        return galaxy_relationship;
    }

    public Planetary_system galaxy_relationship(Galaxy galaxy) {
        this.galaxy_relationship = galaxy;
        return this;
    }

    public void setGalaxy_relationship(Galaxy galaxy) {
        this.galaxy_relationship = galaxy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Planetary_system planetary_system = (Planetary_system) o;
        if(planetary_system.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, planetary_system.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Planetary_system{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", star='" + star + "'" +
            ", galaxy='" + galaxy + "'" +
            '}';
    }
}
