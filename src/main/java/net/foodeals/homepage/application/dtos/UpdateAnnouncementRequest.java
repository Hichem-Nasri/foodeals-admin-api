package net.foodeals.homepage.application.dtos;

public record UpdateAnnouncementRequest(
        String title,
        String message,
        String type,
        Boolean isActive,
        String expiresAt
) {
}
