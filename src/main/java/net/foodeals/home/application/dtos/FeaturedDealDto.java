package net.foodeals.home.application.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeaturedDealDto {
    private UUID id;
    private String title;
    private String description;
    private String image;
    private Double originalPrice;
    private Double discountedPrice;
    private String restaurant;
    private UUID restaurantId;
    private boolean isActive;
    private Integer order;
    private Instant createdAt;
}

