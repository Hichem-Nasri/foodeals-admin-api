package net.foodeals.product.application.dtos.requests;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CreatedByDto {
    private String id;
    private String type;   // ADMIN | PARTNER_SB
    private String email;
    private String phone;
    private String name;
}