package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegionalDistributionData {
    private String region;
    private int partnerCount;
    private double percentage;
    private double averageRevenue;
    private double growthRate;
}