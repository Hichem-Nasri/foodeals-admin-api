package net.foodeals.home.domain.entities;


import jakarta.persistence.*;
import lombok.*;
import net.foodeals.location.domain.entities.City;
import net.foodeals.location.domain.entities.Country;
import net.foodeals.location.domain.entities.State;
import net.foodeals.common.models.AbstractEntity;

import java.util.UUID;

@Entity
@Table(name = "personalized_best_sellers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedBestSeller extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String image;
    private int totalSales;
    private int completedOrders;
    private Float rating;

    @ManyToOne
    private Country country;

    @ManyToOne
    private State state;

    @ManyToOne
    private City city;
}
