package net.foodeals.order.application.dtos.responses;

import java.util.Date;
import java.util.UUID;

public record CouponResponse(
    UUID id,
    String name,
    String code,
    Float discount,
    Date startsAt,
    Date endsAt,
    Boolean isEnabled) {
}
