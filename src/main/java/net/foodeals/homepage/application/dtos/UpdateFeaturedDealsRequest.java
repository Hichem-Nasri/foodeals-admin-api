package net.foodeals.homepage.application.dtos;

import java.util.List;

public record UpdateFeaturedDealsRequest(
        List<String> dealIds,
        List<Integer> order
) {
}
