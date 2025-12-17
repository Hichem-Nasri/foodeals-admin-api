package net.foodeals.home.application.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageHeroDto {
    private UUID id;
    private String title;
    private String subtitle;
    private String backgroundImage;
    private String ctaText;
    private String ctaLink;
    private boolean isActive;
    private Instant updatedAt;
}

