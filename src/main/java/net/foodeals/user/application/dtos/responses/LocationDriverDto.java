package net.foodeals.user.application.dtos.responses;

public record LocationDriverDto(
	    float latitude,
	    float longitude,
	    int heading,
	    int speed,
	    int accuracy
	) {}
