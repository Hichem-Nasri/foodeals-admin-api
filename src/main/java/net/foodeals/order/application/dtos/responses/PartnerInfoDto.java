package net.foodeals.order.application.dtos.responses;

import java.util.UUID;

import lombok.Data;

@Data
public class PartnerInfoDto {
    private UUID id;
    private String name;
    private String avatarUrl;
}