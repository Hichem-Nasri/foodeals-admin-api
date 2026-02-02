package net.foodeals.homepage.application.dtos;

public record UpdateHeroRequest(
        String title,
        String subtitle,
        String backgroundImage,
        String ctaText,
        String ctaLink,
        Boolean isActive
) {
}
