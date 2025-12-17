package net.foodeals.user.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {

	private String status;
	private String notes;
	private Float latitude;
	private Float longitude;
}
