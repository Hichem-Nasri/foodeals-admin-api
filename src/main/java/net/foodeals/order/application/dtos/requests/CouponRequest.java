package net.foodeals.order.application.dtos.requests;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public record CouponRequest(
        @NotBlank String code,
        @NotBlank String name,
        @NotNull Float discount,
        @NotNull Date endsAt,
        @Nullable UUID subEntityId,
        @NotNull Boolean isEnabled) {
}
