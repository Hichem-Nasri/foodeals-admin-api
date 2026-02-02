package net.foodeals.homepage.application.dtos;

public record BestSellerDto(
        String name,
        String image,
        int totalSells,
        int completedOrders,
        double rating
) {
}
