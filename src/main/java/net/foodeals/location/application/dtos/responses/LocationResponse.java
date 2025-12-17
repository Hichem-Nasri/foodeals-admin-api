package net.foodeals.location.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LocationResponse {
	
	private String country;
	private String city;
	private String region;
	private String state ;

}
