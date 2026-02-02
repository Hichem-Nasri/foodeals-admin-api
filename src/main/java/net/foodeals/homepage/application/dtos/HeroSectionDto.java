package net.foodeals.homepage.application.dtos;

public record HeroSectionDto(
        String id,
        String title,
        String subtitle,
        String backgroundImage,
        String ctaText,
        String ctaLink,
        String updatedAt,
        boolean active
) {
}
