package net.foodeals.location.application.dtos.responses;

import java.util.List;
import java.util.UUID;

public record StateHierarchyDto(UUID id, String name, List<CityHierarchyDto> cities) {}