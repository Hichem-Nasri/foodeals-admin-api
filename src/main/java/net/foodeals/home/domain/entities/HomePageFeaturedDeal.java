package net.foodeals.home.domain.entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import net.foodeals.common.models.AbstractEntity;

@Entity
@Table(name = "homepage_featured_deal")
@Getter @Setter
public class HomePageFeaturedDeal extends AbstractEntity<UUID> {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String description;
    private String image;

    private Double originalPrice;
    private Double discountedPrice;

    private String restaurant;
    private UUID restaurantId;

    private boolean isActive;
    private Integer classement;

}

