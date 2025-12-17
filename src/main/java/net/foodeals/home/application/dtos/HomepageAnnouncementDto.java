package net.foodeals.home.application.dtos;

import java.time.Instant;
import java.util.UUID;

import lombok.*;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HomepageAnnouncementDto {
    private UUID id;
    private String title;
    private String message;
    private String type;
    private boolean isActive;
    private Instant expiresAt;
    private Instant createdAt;
}
