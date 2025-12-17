package net.foodeals.order.application.dtos.responses;


import java.util.Date;
import java.util.UUID;

public record CouponSummaryDto(
    UUID id,
    String name,
    String code,
    Float discount,
    Boolean isEnabled,
    Date endsAt,
    Long usageCount
) {}
