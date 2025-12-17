package net.foodeals.home.application.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class FeaturedDealsOrderRequest {
    private List<UUID> dealIds;
    private List<Integer> order;
}
