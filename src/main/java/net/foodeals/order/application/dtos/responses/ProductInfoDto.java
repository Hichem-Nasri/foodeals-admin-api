package net.foodeals.order.application.dtos.responses;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductInfoDto {
    private UUID id;
    private String name;
    private String imageUrl; // Chemin de l'image
}