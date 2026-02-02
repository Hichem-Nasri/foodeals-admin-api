package net.foodeals.homepage.application.dtos;

public record FeaturedDealDto(
        String id,
        String title,
        String description,
        String image,
        double originalPrice,
        double discountedPrice,
        String restaurant,
        String restaurantId,
        int order,
        String createdAt,
        boolean active
) {
}
