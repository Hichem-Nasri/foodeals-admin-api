package net.foodeals.home.domain.entities;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.State;

@Entity
@Table(name = "home_sorting")
@Getter @Setter
@NoArgsConstructor
public class HomeSorting extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String name;  // "deal", "box", etc.

    private Integer rank; // Classement (class 1, 2, ...)

    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @ManyToOne(fetch = FetchType.LAZY)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    private City city;
    
    private Integer orderClass;
}
