package net.foodeals.user.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryAddressDto {
    private double latitude;
    private double longitude;
    private String address;
    private String recipientName;
    private String recipientPhone;
}