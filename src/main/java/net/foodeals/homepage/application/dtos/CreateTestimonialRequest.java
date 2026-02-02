package net.foodeals.homepage.application.dtos;

public record CreateTestimonialRequest(
        String customerName,
        double rating,
        String comment,
        String avatar,
        Boolean isActive,
        Integer order
) {
}
