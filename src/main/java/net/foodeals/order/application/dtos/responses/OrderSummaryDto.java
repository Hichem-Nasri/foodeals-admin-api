package net.foodeals.order.application.dtos.responses;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OrderSummaryDto {
    private UUID id;
    private String ref;
    private List<ProductInfoDto> products;
    private boolean seen;
    private LocalDateTime collectionDate;
    private String partnerName;
    private String partnerEmail;
    private String partnerPhone;
    private String partnerAddress;
    private String clientPhone;
    private String clientEmail;
    private Integer clientId;       // pour PRO, resterait null sauf si tu changes en String/UUID
    private String clientName;
    private String donationType;
    private String orderStatus;
    private String deliveryStatus;
    private String clientType; 
    private Double price;// "client" | "pro"
}