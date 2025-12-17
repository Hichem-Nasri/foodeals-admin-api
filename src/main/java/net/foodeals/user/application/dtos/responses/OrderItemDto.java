package net.foodeals.user.application.dtos.responses;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {
    private String id;
    private String name;
    private int quantity;
    private Double price;
}