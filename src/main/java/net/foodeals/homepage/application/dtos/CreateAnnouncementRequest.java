package net.foodeals.homepage.application.dtos;

public record CreateAnnouncementRequest(
        String title,
        String message,
        String type,
        Boolean isActive,
        String expiresAt
) {
}
