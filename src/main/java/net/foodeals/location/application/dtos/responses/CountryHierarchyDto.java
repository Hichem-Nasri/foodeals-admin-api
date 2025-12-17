package net.foodeals.location.application.dtos.responses;

import java.util.List;
import java.util.UUID;

public record CountryHierarchyDto(UUID id, String name, List<StateHierarchyDto> states) {}
