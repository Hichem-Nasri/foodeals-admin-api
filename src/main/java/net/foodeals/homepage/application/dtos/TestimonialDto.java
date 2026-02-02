package net.foodeals.homepage.application.dtos;

public record TestimonialDto(
        String id,
        String customerName,
        double rating,
        String comment,
        String avatar,
        int order,
        String createdAt,
        boolean active
) {
}
