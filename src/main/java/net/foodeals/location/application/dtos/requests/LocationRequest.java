package net.foodeals.location.application.dtos.requests;

import net.foodeals.processors.annotations.Processable;

public record LocationRequest(
	
	 String country,
      String stateName,
      String cityName,
     String regionName
) {
}
