package net.foodeals.user.application.dtos.responses;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersClientResponseDto {
    private UUID id;
    private String ref;
    private String name;
    private Double price;
    private String status;
    private LocalDateTime dateBuy;
    private StoreResponseDto store;
    private Double deliveryFee;
    private Double totalAmount;
    private String paymentMethod;
    private String estimatedDelivery;
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
