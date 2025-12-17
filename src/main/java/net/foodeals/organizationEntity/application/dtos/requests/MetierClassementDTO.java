package net.foodeals.organizationEntity.application.dtos.requests;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetierClassementDTO {
	private UUID id;
    private Integer classement;
}
