package net.foodeals.homepage.application.dtos;

public record UpdateTestimonialRequest(
        String customerName,
        double rating,
        String comment,
        String avatar,
        Boolean isActive,
        Integer order
) {
}
