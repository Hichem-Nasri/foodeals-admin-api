package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrowthMetricsData {
    private String name;
    private double revenueGrowth;
    private double partnerGrowth;
    private double orderGrowth;
}
