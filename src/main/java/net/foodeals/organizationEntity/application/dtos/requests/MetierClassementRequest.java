package net.foodeals.organizationEntity.application.dtos.requests;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MetierClassementRequest {
    private List<MetierClassementDTO> metiers;
}
