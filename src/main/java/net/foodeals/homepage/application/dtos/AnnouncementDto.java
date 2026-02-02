package net.foodeals.homepage.application.dtos;

public record AnnouncementDto(
        String id,
        String title,
        String message,
        String type,
        String expiresAt,
        String createdAt,
        boolean active
) {
}
