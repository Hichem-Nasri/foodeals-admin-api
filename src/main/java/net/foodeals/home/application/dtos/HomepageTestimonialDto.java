package net.foodeals.home.application.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageTestimonialDto {
    private UUID id;
    private String customerName;
    private int rating;
    private String comment;
    private String avatar;
    private boolean isActive;
    private Integer order;
    private Instant createdAt;
}

