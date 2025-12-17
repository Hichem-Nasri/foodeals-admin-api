package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CityPerformanceData {
    private String city;
    private String region;
    private int partnerCount;
    private double totalRevenue;
    private double averagePerformanceScore;
    private double marketPenetration;
}