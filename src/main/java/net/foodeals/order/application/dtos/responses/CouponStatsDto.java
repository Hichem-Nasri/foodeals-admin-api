package net.foodeals.order.application.dtos.responses;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
public class CouponStatsDto {
    private long total;
    private long totalEnabled;
    private long totalDisabled;
}

