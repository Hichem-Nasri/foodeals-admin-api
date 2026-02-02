package net.foodeals.homepage.application.dtos;

public record CategoryDto(
        String id,
        String name,
        String icon,
        int order,
        int dealCount,
        boolean active
) {
}
