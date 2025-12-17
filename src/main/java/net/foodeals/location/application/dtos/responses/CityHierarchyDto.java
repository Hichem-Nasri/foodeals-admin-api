package net.foodeals.location.application.dtos.responses;

import java.util.List;
import java.util.UUID;

public record CityHierarchyDto(UUID id, String name, List<RegionDto> regions) {}