package net.foodeals.statistics.application.responses;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesAnalyticsData {
    private String name;
    private double sales;
    private double growth;
    private double conversionRate;
    private double customerRetention;
}