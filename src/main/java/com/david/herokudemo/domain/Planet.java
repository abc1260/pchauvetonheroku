package com.david.herokudemo.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Planet.
 */
@Entity
@Table(name = "planet")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Planet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "surface")
    private Float surface;

    @Column(name = "radius")
    private Float radius;

    @Column(name = "system")
    private String system;

    @ManyToOne
    private Planetary_system planetary_system;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Planet name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getSurface() {
        return surface;
    }

    public Planet surface(Float surface) {
        this.surface = surface;
        return this;
    }

    public void setSurface(Float surface) {
        this.surface = surface;
    }

    public Float getRadius() {
        return radius;
    }

    public Planet radius(Float radius) {
        this.radius = radius;
        return this;
    }

    public void setRadius(Float radius) {
        this.radius = radius;
    }

    public String getSystem() {
        return system;
    }

    public Planet system(String system) {
        this.system = system;
        return this;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Planetary_system getPlanetary_system() {
        return planetary_system;
    }

    public Planet planetary_system(Planetary_system planetary_system) {
        this.planetary_system = planetary_system;
        return this;
    }

    public void setPlanetary_system(Planetary_system planetary_system) {
        this.planetary_system = planetary_system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Planet planet = (Planet) o;
        if(planet.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, planet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Planet{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", surface='" + surface + "'" +
            ", radius='" + radius + "'" +
            ", system='" + system + "'" +
            '}';
    }
}
