package net.foodeals.user.application.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponseDto {
    private String id;
    private String name;
    private String logo;
    private String address;
}