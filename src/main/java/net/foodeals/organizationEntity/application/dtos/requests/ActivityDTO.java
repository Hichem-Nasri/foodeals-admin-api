package net.foodeals.organizationEntity.application.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDTO {
    private String name;
    private String observation;
    private Integer classement ;
}