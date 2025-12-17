package net.foodeals.user.application.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderTrackingResponseDto {
    private String orderId;
    private String clientId;
    private String status;
    private String estimatedDeliveryTime;
    private LocationDto currentLocation;
    private DeliveryAddressDto deliveryAddress;
    private DriverDto driver;
    private List<TimelineEventDto> timeline;
    private RestaurantDto restaurant;
}
