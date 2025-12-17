package net.foodeals.user.application.dtos.responses;

public record DriverLocationResponseDto(
	    String driverId,
	    LocationDriverDto currentLocation,
	    String lastUpdated,
	    String estimatedArrival
	) {}

	