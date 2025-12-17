package net.foodeals.location.application.dtos.responses;

import java.util.List;

public record LocationHierarchyResponse(
	    List<CountryHierarchyDto> hierarchy,
	    LocationStatsDto stats
	) {}